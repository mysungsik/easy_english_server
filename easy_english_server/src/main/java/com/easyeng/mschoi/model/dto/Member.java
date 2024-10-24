package com.easyeng.mschoi.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "member")
// 기존에 사용하는 Member 에 SpringSecurity 화를 위해 UserDetails 를 구현
public class Member implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "member_id", length = 30, nullable = false)
    private String memberId;

    @Column(name = "member_pw", length = 400, nullable = false)
    private String memberPw;

    @Column(name = "member_email", length = 100, nullable = false)
    private String memberEmail;

    @Column(name = "member_nickname", length = 100, nullable = false)
    private String memberNickname;

    @Column(name = "member_profile", length = 500)
    private String memberProfile;

    @Column(name = "member_created_dt", nullable = true)
    @CreationTimestamp		// INSERT 시 자동 TIMESTAMP 생성
    private Timestamp memberCreatedDt;

    @Column(name = "member_deleted_dt")
    @ColumnDefault("(current_date)")
    private LocalDate memberDeletedDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_word_id", referencedColumnName = "word_id", nullable = true)
    private WordData wordData;

    // current_word_id 의 Default 값을 지정해주기 위한 메서드
    @PrePersist // DB 저장 직전에 실행되는 메서드
    public void setDefaultCurrentWord() {
        if(this.wordData == null) {
            WordData defaultWord = new WordData();
            defaultWord.setWordId(1);
            this.wordData = defaultWord;
        }
    }
    
    // 추가된 필드: word_level을 직접 매핑하지 않고, Getter를 통해 가져오기
    @Transient // DB에 저장하지 않음. 단순 계산용 필드역할
    public Integer getCurrentWordLevel() {
    	return wordData != null ? wordData.getWordId() : null;
    }

    @Column(name = "member_auth", length = 20)
    @ColumnDefault("'ROLE_ADMIN'")			// member객체에 해당 key 자체가 없어야 Default 적용
    private String memberAuth = "ROLE_ADMIN";	// 따라서, 코드레벨에서 기본값 설정
    
    @Column(name="learn_cnt", nullable = false)
    private Integer learnCnt = 0;	// 하루제한 20개
    
    @Column(name="review_cnt", nullable = false)
    private Integer reviewCnt = 0;	// 하루제한 20개
    
    // Spring Security 에서 사용하기 위한 Authority
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(memberAuth));
	}

	// Spring Security 에서 사용하기 위한 password
	@Override
	public String getPassword() {
		return memberPw;
	}

	// Spring Security 에서 사용하기 위한 id
	@Override
	public String getUsername() {
		return memberId;
	}
}