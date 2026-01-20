package com.strayanimal.mapservice.common.auth;


import com.strayanimal.mapservice.common.auth.TokenUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    public String createAnonymousToken(String ip, String userAgent) {

        Claims claims = Jwts.claims();
        claims.put("type", "ANONYMOUS");
        claims.put("clientId", UUID.randomUUID().toString());

        // 선택: IP를 그대로 넣지 말고 해시
        claims.put("ipHash", DigestUtils.sha256Hex(ip));

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public TokenUserInfo validateAnonymousToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!"ANONYMOUS".equals(claims.get("type"))) {
            throw new RuntimeException("Invalid token type");
        }

        return TokenUserInfo.builder()
                .clientId(claims.get("clientId", String.class))
                .ipHash(claims.get("ipHash", String.class))
                .build();
    }


}