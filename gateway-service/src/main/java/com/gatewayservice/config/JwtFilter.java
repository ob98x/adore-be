package com.gatewayservice.config;

import com.gatewayservice.global.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Map;

@Component
@Slf4j
public class JwtFilter extends AbstractGatewayFilterFactory<Object> {

    @Value("${jwt.secret}") String secretKey;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return returnError(exchange, ErrorCode.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
            if (token == null) {
                return returnError(exchange, ErrorCode.UNAUTHORIZED);
            }

            return validateAndForwardToken(exchange, chain, token);
        };
    }

    private Mono<Void> validateAndForwardToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            String userId = claims.get("user_id", String.class);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("member_id", userId).build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (ExpiredJwtException e) {
            return returnError(exchange, ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            return returnError(exchange, ErrorCode.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            return returnError(exchange, ErrorCode.UNAUTHORIZED);
        }
    }

    private Mono<Void> returnError(ServerWebExchange exchange, ErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(errorCode.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> errorMap = Map.of("code", errorCode.getCode(), "message", errorCode.getMessage());

        return response.writeWith(Mono.fromSupplier(() -> new Jackson2JsonEncoder().encodeValue(errorMap,
                response.bufferFactory(),
                ResolvableType.forInstance(errorMap),
                MediaType.APPLICATION_JSON,
                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())
        )));
    }
}