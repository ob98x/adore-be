package com.authservice.auth.service;

import com.authservice.global.CustomResponseCode;
import com.authservice.auth.dto.*;
import com.authservice.auth.entitiy.Member;
import com.authservice.auth.entitiy.MemberRole;
import com.authservice.auth.entitiy.MemberState;
import com.authservice.auth.repository.MemberRepository;
import com.authservice.global.CustomException;
import com.authservice.config.JwtUtil;
import com.authservice.global.RedisUtil;
import com.authservice.global.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final Duration expireTime = Duration.ofSeconds(864000); // 2 weeks

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByEmailAndState(loginRequestDto.getEmail(), MemberState.ACTIVE)
                .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        log.info("login processing, member: {}", member.getId());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ResponseCode.PASSWORD_INCORRECT);
        }
        log.info("login processing, password correct: {}", member.getId());

        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);
        log.info("login processing, memberInfo: {}", memberInfo.getId());

        String refreshToken = jwtUtil.createRefreshToken(memberInfo);
        String accessToken = jwtUtil.createAccessToken(memberInfo);
        log.info("login processing, refreshToken: {}", refreshToken);
        log.info("login processing, accessToken: {}", accessToken);

        redisUtil.setValues(memberInfo.getId().toString(), refreshToken, expireTime);
        log.info("login end, set redis key: {}", accessToken);

        return LoginResponseDto.of(accessToken, refreshToken, member.getName(), member.getRole());
    }

    @Override
    public ReissueResponseDto reissue(String refreshToken) {

        jwtUtil.validateToken(refreshToken);
        log.info("reissue start: {}", jwtUtil.getMemberId(refreshToken));

        Member member = memberRepository.findByIdAndState(jwtUtil.getMemberId(refreshToken), MemberState.ACTIVE)
                .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        log.info("reissue processing, member: {}", member.getId());

        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);
        log.info("reissue processing, memberInfo: {}", memberInfo.getId());

        String newRefreshToken = jwtUtil.createRefreshToken(memberInfo);
        String newAccessToken = jwtUtil.createAccessToken(memberInfo);
        log.info("reissue processing, newRefreshToken: {}", newRefreshToken);
        log.info("reissue processing, newAccessToken: {}", newAccessToken);

        redisUtil.deleteValue(jwtUtil.getMemberId(refreshToken).toString());
        redisUtil.setValues(memberInfo.getId().toString(), newRefreshToken, expireTime);
        log.info("reissue end, delete exist redis key, set new redis key: {}", newAccessToken);

        return ReissueResponseDto.of(newAccessToken, newRefreshToken);
    }

    @Override
    public ResponseEntity<CustomResponseCode> logout(String accessToken) {
        log.info("logout start: {}", jwtUtil.getMemberId(accessToken));
        jwtUtil.validateToken(accessToken);

        log.info("logout end, redis token delete: {}", jwtUtil.getMemberId(accessToken));
        redisUtil.deleteValue(jwtUtil.getMemberId(accessToken).toString());

        return ResponseEntity.ok(CustomResponseCode.LOGOUT_SUCCESS);
    }

    @Override
    public String checkEmailDuplicate(String email) {
        if (memberRepository.findMemberByEmailAndState(email, MemberState.ACTIVE).isPresent()) {
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        }
        return email;
    }

    @Override
    public String checkNicknameDuplicate(String nickname) {
        if (memberRepository.findMemberByNicknameAndState(nickname, MemberState.ACTIVE).isPresent()) {
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        }
        return nickname;
    }

    @Override
    @Transactional
    public String signUp(SignUpRequestDto signUpRequestDto) {

        checkEmailDuplicate(signUpRequestDto.getEmail());
        log.info("signUp processing, email: {}", signUpRequestDto.getEmail());

        checkNicknameDuplicate(signUpRequestDto.getNickname());
        log.info("signUp processing, nickname: {}", signUpRequestDto.getNickname());

        if (!signUpRequestDto.isAgreeTerms()) {
            throw new CustomException(ResponseCode.TERMS_NOT_AGREED);
        }
        log.info("signUp processing, agreeTerms: {}", signUpRequestDto.isAgreeTerms());

        Member member = Member.of(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getBirthDate(),
                signUpRequestDto.getInflow(),
                signUpRequestDto.getGender(),
                signUpRequestDto.getNickname(),
                MemberRole.USER,
                MemberState.ACTIVE
        );

        memberRepository.save(member);
        return member.getEmail();
    }


    @Override
    public ResponseEntity<CustomResponseCode> send(String email, String subject, String text, int code) {
        // 이메일 요청 횟수 확인
        long count = getEmailRequestCount(email);
        if (count >= 5) {
            throw new CustomException(ResponseCode.EMAIL_COUNT_EXCEED);
        }

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message); // 동기적으로 메일 전송

        // 인증 코드 저장 및 이메일 요청 횟수 증가
        saveVerificationCode(email, String.valueOf(code));
        increaseEmailRequestCount(email);

        return ResponseEntity.ok(CustomResponseCode.EMAIL_SEND_SUCCESS);
    }

    @Override
    public ResponseEntity<CustomResponseCode> verificationEmail(String code, String savedCode) {
        // 입력된 코드와 저장된 코드 비교
        if (!code.equals(savedCode)) {
            throw new CustomException(ResponseCode.EMAIL_AUTHORIZATION_FAIL);
        }
        else return ResponseEntity.ok(CustomResponseCode.EMAIL_AUTHORIZATION_SUCCESS);
    }

    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 1, TimeUnit.MINUTES); // 1분 타임아웃
    }

    public void increaseEmailRequestCount(String email) {
        String key = "email_request_count:" + email;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) {
            count = 1L; // 처음 사용할 경우 1로 초기화
            redisTemplate.opsForValue().set(key, "1");
        }
        if (count == 5) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }


    public long getEmailRequestCount(String email) {
        String key = "email_request_count:" + email;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }

}
