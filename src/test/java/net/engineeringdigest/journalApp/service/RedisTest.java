package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Disabled
    @Test
    void testRedisGetSet() {
        redisTemplate.opsForValue().set("developer_name", "Nomesh Vyas");
        Object value = redisTemplate.opsForValue().get("developer_name");

        assertEquals("Nomesh Vyas", value);
    }
}
