package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj == null) return null;
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(obj.toString(), entityClass);
        } catch (Exception e) {
            log.error("Exception in redis service (get) ", e);
            return null;
        }
    }

    public void set(String key, Object value, Long ttl) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception in redis service (set)", e);
        }
    }
}
