package com.example.EventSphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private static  final int LIMIT=2;
    private static final int WINDOW=60;
    public boolean isAllowed(String key){
        Long now =System.currentTimeMillis();;
        Long windowStart=now-(WINDOW*1000);
        redisTemplate.opsForZSet().removeRangeByScore(key,0,windowStart);
        Long count=redisTemplate.opsForZSet().zCard(key);
        if(count != null && count>=LIMIT){
return false;
        }
        redisTemplate.opsForZSet().add(key,String.valueOf(now),now);
        redisTemplate.expire(key,java.time.Duration.ofSeconds(WINDOW));
        return true;
//        Long count=redisTemplate.opsForValue().increment(key);
//        if(count != null && count==1){
//            redisTemplate.expire(key,java.time.Duration.ofSeconds(WINDOW));
//
//        }
//        return  count !=null && count<=LIMIT;
    }
}
