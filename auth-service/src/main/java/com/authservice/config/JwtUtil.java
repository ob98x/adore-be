package com.authservice.config;

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
        return createToken(member, accessTokenExpTime);
    }

    public String createRefreshToken(MemberInfoDto member) {
        return createToken(member, refreshTokenExpTime);
    }


    private String createToken(MemberInfoDto member, long expireTime) {
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
        return parseClaims(token).get("memberId", Long.class);
    }


    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(ResponseCode.INVALID_TOKEN); // Invalid JWT signature
        } catch (ExpiredJwtException e) {
            throw new CustomException(ResponseCode.EXPIRED_TOKEN); // Expired JWT token
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ResponseCode.UNSUPPORTED_TOKEN); // Unsupported JWT token
        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.INVALID_HEADER_OR_COMPACT_JWT); // JWT token compact of handler are invalid
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}