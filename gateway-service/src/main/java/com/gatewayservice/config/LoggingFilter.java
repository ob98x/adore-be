package com.gatewayservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        log.info("[ gateway-service ] 게이트웨이로 요청이 들어왔습니다. URI: {}", request.getURI().getPath());

        String userAgent = request.getHeaders().getFirst("User-Agent");
        String proxyIp = request.getHeaders().getFirst("X-Forwarded-For");
        InetSocketAddress address = request.getRemoteAddress();
        String originIp = proxyIp != null ? proxyIp : (address != null ? address.toString() : "UNKNOWN SOURCE");
        String fullPath = request.getURI().getPath() + (request.getURI().getQuery() != null ? "?" + request.getURI().getQuery() : "");

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId = route != null ? route.getId() : "UNKNOWN_ROUTE";
        String routeUri = route != null ? route.getUri().toString() : "UNKNOWN_URI";
        String requestId = request.getId();



        return chain.filter(exchange).doOnSuccess(resVoid -> {
            long executionTime = System.currentTimeMillis() - startTime;
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("[ gateway-service ] 요청 ID: {}, HTTP Method: {}, URI: {}, 요청 IP: {}, HTTP Status: {}, 실행 시간: {}ms, UserAgent: {}, 요청 서비스: {}, 요청 서비스 URI: {}",
                        requestId, request.getMethod(), fullPath, originIp,
                        response.getStatusCode(), executionTime, userAgent, routeId, routeUri);
            }
            else {
                log.info("[ gateway-service ] 요청 ID: {}, HTTP Method: {}, URI: {}, 요청 IP: {}, HTTP Status: {}, 실행 시간: {}ms, UserAgent: {}, 요청 서비스: {}, 요청 서비스 URI: {}",
                        requestId, request.getMethod(), fullPath, originIp,
                        response.getStatusCode(), executionTime, userAgent, routeId, routeUri);
            }
        });
    }
}
