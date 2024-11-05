package com.authservice.auth.controller;

import com.authservice.auth.dto.*;
import com.authservice.auth.service.AuthService;
import com.authservice.global.CustomResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Tag(name = "[사용자] 인증 및 인가 관련 API", description = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final Random random = new Random();
    private final AuthService authService;

    @Operation(summary = "이메일 전송 API", description = "이메일을 전송합니다.")
    @PostMapping("/email-send")
    public ResponseEntity<CustomResponseCode> sendEmail(@RequestBody EmailSendDto email) {
        String subject = "회원가입 인증 메일입니다.";
        int code = this.random.nextInt(9000) + 1000;
        String text = "인증 코드는 " + code + "입니다.";

        return authService.send(email.getEmail(), subject, text, code);
    }

    @Operation(summary = "이메일 인증 API", description = "이메일 인증을 수행합니다.")
    @PostMapping("/email-verify")
    public ResponseEntity<CustomResponseCode> verifyEmail(@RequestBody EmailVerificationDto emailVerificationDto) {
        return authService.verificationEmail(
                emailVerificationDto.getCode(),
                authService.getVerificationCode(emailVerificationDto.getEmail())
        );
    }

    @Operation(summary = "로그인 API", description = "로그인을 수행합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @Operation(summary = "닉네임 중복 체크 API", description = "닉네임 중복을 체크합니다.")
    @GetMapping("/check-duplicate/nickname")
    public ResponseEntity<String> checkDuplicateNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(authService.checkNicknameDuplicate(nickname));
    }

    @Operation(summary = "이메일 중복 체크 API", description = "이메일 중복을 체크합니다.")
    @GetMapping("/check-duplicate/email")
    public ResponseEntity<String> checkDuplicateEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }

    @Operation(summary = "토큰 재발급 API", description = "토큰을 재발급합니다.")
    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.reissue(refreshToken.substring(7)));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 수행합니다.")
    @GetMapping("/logout")
    public ResponseEntity<CustomResponseCode> logout(@RequestHeader("Authorization") String accessToken) {
        return authService.logout(accessToken.substring(7));
    }

    @Operation(summary = "회원 가입 API", description = "회원 가입을 수행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok(authService.signUp(signUpRequestDto));
    }

}
