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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
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
        Member member = checkConflictMember(loginRequestDto.getEmail());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            log.error("[ Auth Service - login ] email: {} 비밀번호가 일치하지 않습니다.", loginRequestDto.getEmail());
            throw new CustomException(ResponseCode.PASSWORD_INCORRECT);
        }

        log.info("[ Auth Service - login ] email: {} 로그인합니다.", loginRequestDto.getEmail());
        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);

        String refreshToken = jwtUtil.createRefreshToken(memberInfo);
        String accessToken = jwtUtil.createAccessToken(memberInfo);

        redisUtil.setValues(memberInfo.getId().toString(), refreshToken, expireTime);

        log.info("[ Auth Service - login ] 마지막 로그인 일자를 변경합니다.");
        member.setLastLoginAt(LocalDate.now());
        memberRepository.save(member);

        return LoginResponseDto.of(accessToken, refreshToken, member.getName(), member.getRole());
    }

    @Override
    public ReissueResponseDto reissue(String refreshToken) {

        jwtUtil.validateToken(refreshToken);

        Member member = memberRepository.findByIdAndState(jwtUtil.getMemberId(refreshToken), MemberState.ACTIVE)
                .orElseThrow(() -> {
                    log.error("[ Auth Service - reissue ] refreshToken: {} 유저가 없습니다.", refreshToken);
                    return new CustomException(ResponseCode.MEMBER_NOT_FOUND);
                });
        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);

        String newRefreshToken = jwtUtil.createRefreshToken(memberInfo);
        String newAccessToken = jwtUtil.createAccessToken(memberInfo);

        redisUtil.deleteValue(jwtUtil.getMemberId(refreshToken).toString());
        redisUtil.setValues(memberInfo.getId().toString(), newRefreshToken, expireTime);

        return ReissueResponseDto.of(newAccessToken, newRefreshToken);
    }

    @Override
    public ResponseEntity<CustomResponseCode> logout(String accessToken) {
        log.info("[ Auth Service - logout ] accessToken: {} 로그아웃합니다.", accessToken);

        jwtUtil.validateToken(accessToken);

        if (redisUtil.getValue(jwtUtil.getMemberId(accessToken).toString()) == null) {
            log.error("[ Auth Service - logout ] accessToken: {} 이미 로그아웃된 사용자입니다.", accessToken);
            throw new CustomException(ResponseCode.ALREADY_LOGOUT);
        }

        redisUtil.deleteValue(jwtUtil.getMemberId(accessToken).toString());

        return ResponseEntity.ok(CustomResponseCode.LOGOUT_SUCCESS);
    }

    @Override
    @Transactional
    public CustomResponseCode resetPassword(ResetPasswordDto resetPasswordDto) {
        log.info("[ Auth Service - resetPassword ] email: {} 비밀번호를 재설정합니다.", resetPasswordDto.getEmail());

        Member member = checkConflictMember(resetPasswordDto.getEmail());

        if ( resetPasswordDto.getNewPassword() == null || resetPasswordDto.getNewPasswordConfirm() == null || resetPasswordDto.getEmailVerify() == null) {
            log.error("[ Auth Service - resetPassword ] email: {} 비밀번호를 입력하지 않았습니다.", resetPasswordDto.getEmail());
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getNewPasswordConfirm())) {
            log.error("[ Auth Service - resetPassword ] email: {} 비밀번호가 일치하지 않습니다.", resetPasswordDto.getEmail());
            throw new CustomException(ResponseCode.PASSWORD_NOT_MATCH);
        }

        if ( passwordEncoder.encode(resetPasswordDto.getNewPassword()).equals(member.getPassword())) {
            log.error("[ Auth Service - resetPassword ] email: {} 이전 비밀번호와 동일합니다.", resetPasswordDto.getEmail());
            throw new CustomException(ResponseCode.PASSWORD_SAME);
        }

        log.info("[ Auth Service - resetPassword ] email: {} 비밀번호를 변경합니다.", resetPasswordDto.getEmail());
        member.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        memberRepository.save(member);
        return CustomResponseCode.PASSWORD_RESET_SUCCESS;
    }

    @Override
    @Transactional
    public String signUp(SignUpRequestDto signUpRequestDto) {
        checkEmailDuplicate(signUpRequestDto.getEmail());
        checkNicknameDuplicate(signUpRequestDto.getNickname());

        if (!signUpRequestDto.isAgreeTerms()) {
            log.error("[ Auth Service - signUp ] 이용약관에 동의하지 않았습니다.");
            throw new CustomException(ResponseCode.TERMS_NOT_AGREED);
        }

        if (!signUpRequestDto.isEmailVerify()) {
            log.error("[ Auth Service - signUp ] 이메일 인증을 하지 않았습니다.");
            throw new CustomException(ResponseCode.EMAIL_AUTHORIZATION_FAIL);
        }

        Member member = Member.of(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getBirthDate(),
                signUpRequestDto.getInflow(),
                signUpRequestDto.getGender(),
                signUpRequestDto.getNickname(),
                MemberRole.USER,
                MemberState.ACTIVE,
                LocalDate.now()
        );

        memberRepository.save(member);
        return member.getEmail();
    }


    @Override
    public ResponseEntity<CustomResponseCode> send(String email, String subject, String text, int code) {
        log.info("[ Auth Service - send ] email: {} 인증 코드를 전송합니다.", email);

        log.info("[ Auth Service - send ] email: {} 인증 코드 전송 제한 횟수를 확인합니다.", email);
        long count = getEmailRequestCount(email);
        if (count >= 5) {
            log.error("[ Auth Service - send ] email: {} 인증 코드 전송 제한 횟수를 초과했습니다.", email);
            throw new CustomException(ResponseCode.EMAIL_COUNT_EXCEED);
        }


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
        log.info("[ Auth Service - verificationEmail ] code: {} 인증 코드를 확인합니다.", code);
        if (!code.equals(savedCode)) {
            log.error("[ Auth Service - verificationEmail ] code: {} 인증 코드가 일치하지 않습니다.", code);
            throw new CustomException(ResponseCode.EMAIL_AUTHORIZATION_FAIL);
        }
        else return ResponseEntity.ok(CustomResponseCode.EMAIL_AUTHORIZATION_SUCCESS);
    }

    @Override
    public String getVerificationCode(String email) {
        log.info("[ Auth Service - getVerificationCode ] email: {} Redis 에서 인증 코드를 가져옵니다.", email);
        return redisTemplate.opsForValue().get(email);
    }

    @Override
    public void saveVerificationCode(String email, String code) {
        log.info("[ Auth Service - saveVerificationCode ] email: {} 인증 코드를 Redis 에 저장합니다.", email);
        redisTemplate.opsForValue().set(email, code, 1, TimeUnit.MINUTES); // 1분 타임아웃
    }

    @Override
    public void increaseEmailRequestCount(String email) {
        log.info("[ Auth Service - increaseEmailRequestCount ] email: {} 이메일 요청 횟수를 증가합니다.", email);
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

    @Override
    public long getEmailRequestCount(String email) {
        log.info("[ Auth Service - getEmailRequestCount ] email: {} 이메일 요청 횟수를 가져옵니다.", email);
        String key = "email_request_count:" + email;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }

    @Override
    public String returnRefreshToken(HttpServletRequest request) {
        log.info("[ Auth Service - returnRefreshToken ] refreshToken 을 반환합니다.");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new CustomException(ResponseCode.NOT_FOUND_REFRESH_TOKEN);
    }

    @Override
    public ResponseCookie createTokenCookie(String cookieName, String token, boolean isHttpOnly, int maxAge) {
        log.info("[ Auth Service - createTokenCookie ] {} 쿠키를 생성합니다.", cookieName);
        return ResponseCookie.from(cookieName, token)
                .httpOnly(isHttpOnly)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .secure(true)
                .build();
    }

    @Override
    public GetTokenInfo getTokenInfo(String token) {
        return jwtUtil.getTokenInfo(token);
    }


    public Member checkConflictMember(String email) {
        log.info("[ Auth Service - resetPassword ] email: {} 유저가 있는 지 확인합니다.", email);
        return memberRepository.findMemberByEmailAndState(email, MemberState.ACTIVE)
                .orElseThrow(
                        () -> {
                            log.error("[ Auth Service - checkConflictMember ] email: {} 유저가 없습니다.", email);
                            return new CustomException(ResponseCode.MEMBER_NOT_FOUND);
                        });
    }

    @Override
    public String checkEmailDuplicate(String email) {
        log.info("[ Auth Service - checkEmailDuplicate ] email: {} 중복 확인합니다.", email);
        if (memberRepository.findMemberByEmailAndState(email, MemberState.ACTIVE).isPresent()) {
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        } else return email;
    }

    @Override
    public String checkNicknameDuplicate(String nickname) {
        log.info("[ Auth Service - checkNicknameDuplicate ] nickname: {} 중복 확인합니다.", nickname);
        if (memberRepository.findMemberByNicknameAndState(nickname, MemberState.ACTIVE).isPresent()) {
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        } else return nickname;
    }



}
