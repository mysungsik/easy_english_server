package com.easyeng.mschoi.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyeng.mschoi.model.dto.WordData;


public interface AdminDAO extends JpaRepository<WordData, Integer>{
	Optional<WordData> findByWordSpell(String wordSpell);
}
