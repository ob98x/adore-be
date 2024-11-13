package com.authservice.auth.service;

import com.authservice.auth.dto.*;
import com.authservice.global.CustomResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    ReissueResponseDto reissue(String refreshToken);
    ResponseEntity<CustomResponseCode> logout(String accessToken);
    String signUp(SignUpRequestDto signInRequestDto);
    String checkEmailDuplicate(String email);
    String checkNicknameDuplicate(String nickname);
    ResponseEntity<CustomResponseCode> send(String email, String subject, String text, int code);
    String getVerificationCode(String email);
    void saveVerificationCode(String email, String code);
    void increaseEmailRequestCount(String email);
    long getEmailRequestCount(String email);
    ResponseEntity<CustomResponseCode> verificationEmail(String code, String savedCode);
    CustomResponseCode resetPassword(ResetPasswordDto resetPasswordDto);
    ResponseCookie createTokenCookie(String cookieName, String token, boolean isHttpOnly, int maxAge);
    public String returnRefreshToken(HttpServletRequest request);
}