package com.strayanimal.petservice.common.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    public String createAnonymousToken(String ip, String userAgent, String fingerprint) {

        String fingerprintHash = DigestUtils.sha256Hex(fingerprint);

        return Jwts.builder()
                .claim("type", "ANONYMOUS")
                .claim("fp", fingerprintHash)
                .claim("ua", DigestUtils.sha256Hex(userAgent))
                .claim("ip", DigestUtils.sha256Hex(ip))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (long) expiration * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

}