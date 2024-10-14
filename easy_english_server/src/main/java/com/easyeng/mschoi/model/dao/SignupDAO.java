package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyeng.mschoi.model.dto.Member;

// JpaRepository<Entity, 프라이머리키타입>
// Repository 애노테이션 생략 가능
// 기본적으로 save (insert 혹은 update)
public interface SignupDAO extends JpaRepository<Member, Integer>{
}
