package com.strayanimal.petservice.pet.controller;

import com.strayanimal.petservice.common.auth.JwtTokenProvider;
import com.strayanimal.petservice.pet.service.TokenSecurityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenProvider tokenProvider;
    private final TokenSecurityService tokenSecurityService;

    @GetMapping("/issue")
    public ResponseEntity<?> issueToken(HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String fingerprint = request.getHeader("X-Fingerprint");

        // 1️⃣ 최소 검증
        if (userAgent == null || userAgent.isBlank()) {
            return ResponseEntity.status(403).body("INVALID_USER_AGENT");
        }

        if (fingerprint == null || fingerprint.isBlank()) {
            return ResponseEntity.status(400).body("FINGERPRINT_REQUIRED");
        }

        // 2️⃣ Redis 기반 보안 검증
        try {
            tokenSecurityService.validateIssueRequest(ip, fingerprint);
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }

        // 3️⃣ 토큰 발급 (fingerprint hash 포함)
        String token = tokenProvider.createAnonymousToken(
                ip,
                userAgent,
                fingerprint // 👈 중요
        );

        return ResponseEntity.ok(token);

    }

}
