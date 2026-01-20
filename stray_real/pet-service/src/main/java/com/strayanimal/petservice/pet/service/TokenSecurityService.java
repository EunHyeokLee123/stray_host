package com.strayanimal.petservice.pet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenSecurityService {

    private final StringRedisTemplate redisTemplate;

    // 추후에 횟수는 어느정도로 설정할지 고민하자.
    private static final int LIMIT_PER_MINUTE = 1000;

    public void validateIssueRequest(String ip, String fingerprint) {

        // 1️⃣ block 확인
        if (isBlocked(ip, fingerprint)) {
            throw new RuntimeException("BLOCKED_CLIENT");
        }

        // 2️⃣ rate limit
        String key = "token:req:" + ip + ":" + fingerprint;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }

        if (count > LIMIT_PER_MINUTE) {
            blockFingerprint(fingerprint);
            throw new RuntimeException("TOO_MANY_REQUESTS");
        }
    }

    private boolean isBlocked(String ip, String fingerprint) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("token:block:ip:" + ip))
                || Boolean.TRUE.equals(redisTemplate.hasKey("token:block:fp:" + fingerprint));
    }

    private void blockFingerprint(String fingerprint) {
        redisTemplate.opsForValue()
                .set("token:block:fp:" + fingerprint, "1", Duration.ofMinutes(30));
    }

}
