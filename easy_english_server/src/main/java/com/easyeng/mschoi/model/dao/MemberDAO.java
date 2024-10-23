package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyeng.mschoi.model.dto.Member;
import com.easyeng.mschoi.model.dto.WordData;

public interface MemberDAO extends JpaRepository<Member, Integer>{
	
	// JPA 메소드 이름으로 쿼리 생성
	// MemberId 로 Member 조회
	Member findByMemberId(String memberId);

	/** memberNo 로 WordData 조회
	 * @param memberNo
	 * @return
	 * 
	 * JPA 에서는 "객체 자체(클래스이름 = 엔티티이름)" 를 SELECT 문에 써야하며, 해당 결과를 받을 수 있다.
	 */
	@Query("SELECT m.wordData FROM Member m WHERE m.memberNo = :memberNo")
	WordData findCurrentWordDataByMemberId(@Param("memberNo") int memberNo);
	
	/** 유저의 현재 공부단어레벨 업데이트
	 * @param memberNo
	 * @param nextWordId
	 * 
	 * JPA 에서 @QUERY 어노테이션은 기본적으로 SELECT 이다. 
	 * DML 구문을 수행하려면 @Modifying 어노테이션을 추가로 사용한다.
	 */
	@Modifying
	@Query("UPDATE Member m SET m.wordData.wordId = :nextWordId WHERE m.memberNo = :memberNo")
	void updateMemberWordId(@Param("memberNo") int memberNo, @Param("nextWordId") int nextWordId);

	/** 유저의 오늘의 학습 횟수 업데이트
	 * @param memberNo
	 * @param learnCnt
	 */
	@Modifying
	@Query("UPDATE Member m SET m.learnCnt = :learnCnt WHERE m.memberNo = :memberNo")
	void updateMemberLearnCnt(@Param("memberNo") int memberNo, @Param("learnCnt") int learnCnt);

	/** 유저의 오늘의 복습 횟수 업데이트
	 * @param memberNo
	 * @param learnCnt
	 */
	@Modifying
	@Query("UPDATE Member m SET m.reviewCnt = :reviewCnt WHERE m.memberNo = :memberNo")
	void updateMemberReviewCnt(@Param("memberNo") int memberNo, @Param("reviewCnt") int reviewCnt);
	
	
}
