package com.strayanimal.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final List<String> allowUrl = Arrays.asList(

            "/token/issue",
            "/scheduler/**"

    );

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.info("요청 path: {}", path);

            AntPathMatcher antPathMatcher = new AntPathMatcher();

            //  허용 경로와 현재 요청 path가 일치하는지 확인
            boolean isAllowed = allowUrl.stream()
                    .anyMatch(url -> antPathMatcher.match(url, path));

            log.info("isAllowed:{}", isAllowed);

            if (isAllowed || path.startsWith("/actuator")) {
                return chain.filter(exchange);
            }

            //  인증이 필요한 요청 처리함.
            String authorizationHeader = exchange.getRequest()
                    .getHeaders().getFirst("Authorization");
            log.info("authorizationHeader: {}", authorizationHeader);


            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return onError(exchange, "TOKEN_REQUIRED", HttpStatus.UNAUTHORIZED);
            }

            String token = authorizationHeader.replace("Bearer ", "");
            log.info("token: {}", token);

            Claims claims;

            try {
                claims = validateJwt(token, secretKey);
            } catch (RuntimeException e) {
                return onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            // 4️ 토큰 타입 확인
            String type = claims.get("type", String.class);
            if (!"ANONYMOUS".equals(type)) {
                return onError(exchange, "INVALID_TOKEN_TYPE", HttpStatus.UNAUTHORIZED);
            }

            // 5 fingerprint hash 확인
            String fpHash = claims.get("fp", String.class);
            if (fpHash == null || fpHash.isBlank()) {
                return onError(exchange, "INVALID_FINGERPRINT", HttpStatus.UNAUTHORIZED);
            }

            // 6️ 내부 서비스로 필요한 정보만 전달
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Fingerprint-Hash", fpHash)
                    .build();

            log.info("Gateway 통과 - clientId 전달 완료");

            return chain.filter(
                    exchange.mutate().request(mutatedRequest).build()
            );

        };
    }
    
    // 에러 발생 시 확인하기 위한 메소드
    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(msg);

        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private Claims validateJwt(String token, String secretKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨: {}", e.getMessage());
            throw new RuntimeException("EXPIRED_TOKEN"); // 사용자 정의 예외 메시지
        } catch (Exception e) {
            log.error("JWT 파싱 실패: {}", e.getMessage());
            throw new RuntimeException("INVALID_TOKEN"); // 다른 예외는 따로
        }
    }
}