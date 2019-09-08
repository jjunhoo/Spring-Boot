package com.apress.spring.springbootjournal.repository;

import com.apress.spring.springbootjournal.domain.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository는 marker interface로 Spring Data Repository 엔진이 자동 인지
// 기본 CRUD뿐만 아니라 커스텀 메소드 구현에 필요한 프록시 클래스를 적용하기 위해 사용
// JpaRepository는 데이터 정렬, 페이징 같은 액션을 추가할 때 간편한 구현 로직을 지니고 있음
public interface JournalRepository extends JpaRepository<Journal, Long> { }
