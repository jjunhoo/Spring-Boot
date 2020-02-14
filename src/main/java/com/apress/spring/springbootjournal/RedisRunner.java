package com.apress.spring.springbootjournal;

import com.apress.spring.springbootjournal.domain.RedisAccount;
import com.apress.spring.springbootjournal.repository.RedisAccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import sun.rmi.runtime.Log;

import java.util.Optional;

@Component
public class RedisRunner implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(RedisRunner.class);

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisAccountRepository redisAccountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Redis(Single Data 적재) - String Type 적재
        // JPA > save 메소드 없이 즉시 적재
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set("name", "KJH");              // Key(BASE), Value(String)
        values.set("framework", "Spring Boot"); // Key(BASE), Value(String)
        values.set("message", "Hello World");   // Key(BASE), Value(String)

        // JPA(Entity Data 적재) - Hash Type 적재
        RedisAccount account1 = new RedisAccount();
        account1.setUsername("KJH");
        account1.setEmail("test@naver.com");

        RedisAccount account2 = new RedisAccount();
        account2.setUsername("KCS");
        account2.setEmail("kcs@daum.net");

        // JPA - Redis 적재
        redisAccountRepository.save(account1);

        Optional<RedisAccount> RedisAccountData1 = redisAccountRepository.findById(account1.getId());
        log.info("[Redis Data - 1 ] : " + RedisAccountData1.orElse(new RedisAccount()).getUsername());
        log.info("[Redis Data - 1 ] : " + RedisAccountData1.orElse(new RedisAccount()).getEmail());

        redisAccountRepository.save(account2);
        Optional<RedisAccount> RedisAccountData2 = redisAccountRepository.findById(account2.getId());
        log.info("[Redis Data - 2 ] : " + RedisAccountData2.orElse(new RedisAccount()).getUsername());
        log.info("[Redis Data - 2 ] : " + RedisAccountData2.orElse(new RedisAccount()).getEmail());
    }
}
