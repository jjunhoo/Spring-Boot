package com.apress.spring.springbootjournal.domain;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

// Redis의 BASE(KEY) 이름
// 예 : partnerId : b89098cf-aa5f-4e3c-adae-a4f213e0a0fd 형식
@RedisHash("partnerId")
public class RedisAccount {
    @Id
    String id;
    private String username;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
