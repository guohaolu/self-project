package org.example.redis.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 缓存的读写
 */
@RestController
public class CacheController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/put")
    public String put(@RequestParam String key, @RequestParam String value) {
        // 存活20秒
        redisTemplate.opsForValue().set(key, value, 20, TimeUnit.SECONDS);
        return "SUCCESS";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
