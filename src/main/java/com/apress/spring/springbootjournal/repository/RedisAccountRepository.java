package com.apress.spring.springbootjournal.repository;

import com.apress.spring.springbootjournal.domain.RedisAccount;

import org.springframework.data.repository.CrudRepository;

public interface RedisAccountRepository extends CrudRepository<RedisAccount, String> {

}
