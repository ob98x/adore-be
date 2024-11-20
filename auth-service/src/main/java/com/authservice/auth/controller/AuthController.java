package com.authservice.auth.controller;

import com.authservice.auth.dto.*;
import com.authservice.auth.service.AuthService;
import com.authservice.global.CustomResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Tag(name = "[사용자] 인증 및 인가 관련 API", description = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final Random random = new Random();
    private final AuthService authService;

    @Value("${jwt.access_exp}")
    private int accessExp;

    @Value("${jwt.refresh_exp}")
    private int refreshExp;

    @Operation(summary = "이메일 전송 API", description = "이메일을 전송합니다.")
    @PostMapping("/email-send")
    public ResponseEntity<CustomResponseCode> sendEmail(@Parameter(description = "인증 코드를 보낼 이메일을 입력합니다.") @RequestParam String email) {
        log.info("[ Auth Controller - sendEmail ] email: {} 에 이메일 전송을 요청합니다.", email);

        String subject = "회원가입 인증 메일입니다.";
        int code = this.random.nextInt(9000) + 1000;
        String text = "인증 코드는 " + code + "입니다.";
        log.info("[ Auth Controller - sendEmail ] 제목: {}, 코드: {}, 내용: {}", subject, code, text);

        return authService.send(email, subject, text, code);
    }

    @Operation(summary = "이메일 인증 API", description = "이메일 인증을 수행합니다.")
    @PostMapping("/email-verify")
    public ResponseEntity<CustomResponseCode> verifyEmail(@RequestBody EmailVerificationDto emailVerificationDto) {
        log.info("[ Auth Controller - verifyEmail ] 이메일 인증 요청을 수행합니다. email: {}, request code: {}", emailVerificationDto.getEmail(), emailVerificationDto.getCode());
        return authService.verificationEmail(
                emailVerificationDto.getCode(),
                authService.getVerificationCode(emailVerificationDto.getEmail())
        );
    }

    @Operation(summary = "로그인 API", description = "로그인을 수행합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("[ Auth Controller - login ] 로그인 요청을 수행합니다. email: {}", loginRequestDto.getEmail());

        LoginResponseDto response = authService.login(loginRequestDto);
        log.info("[ Auth Controller - login ] 로그인 시도에 성공했습니다. email: {}", loginRequestDto.getEmail());

        log.info("[ Auth Controller - login ] 액세스 토큰: {}, 리프레시 토큰: {}", response.getAccessToken(), response.getRefreshToken());
        ResponseCookie accessCookie = authService.createTokenCookie("accessToken", response.getAccessToken(), false, accessExp);
        ResponseCookie refreshCookie = authService.createTokenCookie("refreshToken", response.getRefreshToken(), true, refreshExp);

        return ResponseEntity.ok().headers(headers -> {
            headers.add("Set-Cookie", accessCookie.toString());
            headers.add("Set-Cookie", refreshCookie.toString());
        }).body(response);
    }

    @Operation(summary = "닉네임 중복 체크 API", description = "닉네임 중복을 체크합니다.")
    @GetMapping("/check-duplicate/nickname")
    public ResponseEntity<String> checkDuplicateNickname(@Parameter(description = "중복 확인할 닉네임을 입력합니다") @RequestParam String nickname) {
        log.info("[ Auth Controller - checkDuplicateNickname ] 닉네임 중복 확인 요청을 수행합니다. nickname: {}", nickname);
        String result = authService.checkNicknameDuplicate(nickname) + " 사용 가능한 닉네임입니다.";
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "이메일 중복 체크 API", description = "이메일 중복을 체크합니다.")
    @GetMapping("/check-duplicate/email")
    public ResponseEntity<String> checkDuplicateEmail(@Parameter(description = "중복 확인할 이메일을 입력합니다") @RequestParam String email) {
        log.info("[ Auth Controller - checkDuplicateEmail ] 이메일 중복 확인 요청을 수행합니다. email: {}", email);
        String result = authService.checkEmailDuplicate(email) + " 사용 가능한 이메일입니다.";
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "토큰 재발급 API", description = "토큰을 재발급합니다.")
    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(HttpServletRequest request) {
        log.info("[ Auth Controller - reissue ] 토큰 재발급 요청을 수행합니다.");

        String refreshToken = authService.returnRefreshToken(request);
        log.info("[ Auth Controller - reissue ] 리프레시 토큰: {}", refreshToken);

        ReissueResponseDto response = authService.reissue(refreshToken);
        log.info("[ Auth Controller - reissue ] 재발급에 성공했습니다. 액세스 토큰: {}, 리프레시 토큰: {}", response.getAccessToken(), response.getRefreshToken());

        log.info("[ Auth Controller - reissue ] 액세스 토큰: {}, 리프레시 토큰: {}", response.getAccessToken(), response.getRefreshToken());
        ResponseCookie accessCookie = authService.createTokenCookie("accessToken", response.getAccessToken(), false, accessExp);
        ResponseCookie refreshCookie = authService.createTokenCookie("refreshToken", response.getRefreshToken(), true, refreshExp);

        return ResponseEntity.ok().headers(headers -> {
            headers.add("Set-Cookie", accessCookie.toString());
            headers.add("Set-Cookie", refreshCookie.toString());
        }).body(response);
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 수행합니다.")
    @GetMapping("/logout")
    public ResponseEntity<CustomResponseCode> logout(@RequestHeader("Authorization") String accessToken) {
        log.info("[ Auth Controller - logout ] 로그아웃 요청을 수행합니다. accessToken: {}", accessToken);
        String token = accessToken.substring(7);
        return authService.logout(token);
    }

    @Operation(summary = "회원 가입 API", description = "회원 가입을 수행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("[ Auth Controller - signUp ] 회원 가입 요청을 수행합니다. email: {}", signUpRequestDto.getEmail());
        String response = authService.signUp(signUpRequestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "비밀 번호 재설정 API", description = "비밀 번호를 재설정합니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<CustomResponseCode> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        log.info("[ Auth Controller - resetPassword ] 비밀번호 재설정 요청을 수행합니다. email: {}", resetPasswordDto.getEmail());
        CustomResponseCode response = authService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "JWT 토큰으로 정보 반환하는 API", description = "JWT 토큰으로 정보를 반환합니다.")
    @GetMapping("/token")
    public GetTokenInfo getInfo(@RequestParam String token) {
        return authService.getTokenInfo(token);
    }

}
