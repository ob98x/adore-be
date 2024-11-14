package com.authservice.auth.service;

import com.authservice.auth.dto.*;
import com.authservice.global.CustomResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    ReissueResponseDto reissue(String refreshToken);
    ResponseEntity<CustomResponseCode> logout(String accessToken);
    CustomResponseCode resetPassword(ResetPasswordDto resetPasswordDto);
    String signUp(SignUpRequestDto signInRequestDto);

    ResponseEntity<CustomResponseCode> send(String email, String subject, String text, int code);
    ResponseEntity<CustomResponseCode> verificationEmail(String code, String savedCode);
    String getVerificationCode(String email);
    void saveVerificationCode(String email, String code);
    void increaseEmailRequestCount(String email);
    long getEmailRequestCount(String email);

    ResponseCookie createTokenCookie(String cookieName, String token, boolean isHttpOnly, int maxAge);
    String returnRefreshToken(HttpServletRequest request);
    GetTokenInfo getTokenInfo(String accessToken);

    String checkEmailDuplicate(String email);
    String checkNicknameDuplicate(String nickname);
}