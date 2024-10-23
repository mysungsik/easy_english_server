package com.easyeng.mschoi.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyeng.mschoi.model.dto.WordData;


public interface WordDataDAO extends JpaRepository<WordData, Integer>{
	Optional<WordData> findByWordSpell(String wordSpell);

	// 예문이 있는 데이터, Limit
	@Query(value = "SELECT * FROM word_data WHERE example_mean != '' AND word_id < :limitWordId order by RAND() LIMIT 1", nativeQuery = true)
	WordData getRandomWordForReviewByMember(@Param("limitWordId") int limitWordId);
}
