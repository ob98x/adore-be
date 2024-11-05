package com.authservice.auth.service;

import com.authservice.auth.dto.LoginRequestDto;
import com.authservice.auth.dto.LoginResponseDto;
import com.authservice.auth.dto.ReissueResponseDto;
import com.authservice.auth.dto.SignUpRequestDto;
import com.authservice.global.CustomResponseCode;
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
}