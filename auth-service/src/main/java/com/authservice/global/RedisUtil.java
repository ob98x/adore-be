package com.authservice.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String value) {
        log.info("[ RedisUtil - setValues ] key: {}, value: {}", key, value);
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    public void setValues(String key, String value, Duration duration) {
        log.info("[ RedisUtil - setValues ] key: {}, value: {}, duration: {}", key, value, duration);
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    public String getValue(String key) {
        log.info("[ RedisUtil - getValue ] key: {}", key);
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) return "";
        return String.valueOf(values.get(key));
    }

    public void deleteValue(String key) {
        log.info("[ RedisUtil - deleteValue ] key: {}", key);
        redisTemplate.delete(key);
    }
}