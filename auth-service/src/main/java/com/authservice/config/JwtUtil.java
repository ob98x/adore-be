package com.authservice.config;

import com.authservice.auth.dto.GetTokenInfo;
import com.authservice.auth.dto.MemberInfoDto;
import com.authservice.global.CustomException;
import com.authservice.global.ResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_exp}") long accessTokenExpTime,
            @Value("${jwt.refresh_exp}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }


    public String createAccessToken(MemberInfoDto member) {
        log.info("[ JwtUtil - createAccessToken ] 액세스 토큰을 생성합니다. memberId: {}", member.getId());
        return createToken(member, accessTokenExpTime);
    }

    public String createRefreshToken(MemberInfoDto member) {
        log.info("[ JwtUtil - createRefreshToken ] 리프레시 토큰을 생성합니다. memberId: {}", member.getId());
        return createToken(member, refreshTokenExpTime);
    }


    private String createToken(MemberInfoDto member, long expireTime) {
        log.info("[ JwtUtil - createToken ] 토큰을 생성합니다. memberId: {}, memberName: {}, memberRole: {}", member.getId(), member.getName(), member.getRole());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .claim("memberName", member.getName())
                .claim("memberId", member.getId())
                .claim("role", member.getRole())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }


    public Long getMemberId(String token) {
        log.info("[ JwtUtil - getMemberId ] 토큰에서 memberId를 추출합니다. token: {}", token);
        return parseClaims(token).get("memberId", Long.class);
    }

    public GetTokenInfo getTokenInfo(String accessToken) {
        log.info("[ JwtUtil - getTokenInfo ] 토큰 정보를 추출합니다. accessToken: {}", accessToken);
        Claims claims = parseClaims(accessToken);
        return GetTokenInfo.builder()
                .memberName(claims.get("memberName", String.class))
                .memberRole(claims.get("role", String.class))
                .memberId(claims.get("memberId", Long.class))
                .build();
    }

    public void validateToken(String token) {
        log.info("[ JwtUtil - validateToken ] 토큰을 검증합니다. token: {}", token);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            log.info("[ JwtUtil - validateToken ] 토큰 검증 성공. token: {}", token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("[ JwtUtil - validateToken ] 토큰 검증 실패 사유: 변조되거나 잘못된 토큰 token: {}", token);
            throw new CustomException(ResponseCode.INVALID_TOKEN); // Invalid JWT signature
        } catch (ExpiredJwtException e) {
            log.error("[ JwtUtil - validateToken ] 토큰 검증 실패 사유: 만료된 토큰 token: {}", token);
            throw new CustomException(ResponseCode.EXPIRED_TOKEN); // Expired JWT token
        } catch (UnsupportedJwtException e) {
            log.error("[ JwtUtil - validateToken ] 토큰 검증 실패 사유: 지원되지 않는 토큰 token: {}", token);
            throw new CustomException(ResponseCode.UNSUPPORTED_TOKEN); // Unsupported JWT token
        } catch (IllegalArgumentException e) {
            log.error("[ JwtUtil - validateToken ] 토큰 검증 실패 사유: 잘못된 토큰 token: {}", token);
            throw new CustomException(ResponseCode.INVALID_HEADER_OR_COMPACT_JWT); // JWT token compact of handler are invalid
        }
    }

    public Claims parseClaims(String accessToken) {
        log.info("[ JwtUtil - parseClaims ] 토큰에서 클레임을 추출합니다. accessToken: {}", accessToken);
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}