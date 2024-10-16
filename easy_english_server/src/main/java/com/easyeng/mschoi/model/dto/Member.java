package com.easyeng.mschoi.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = true)
    private WordData wordData;

    @Column(name = "word_level")
    private Integer wordLevel;
    
    @Column(name = "member_auth", length = 20)
    @ColumnDefault("'ROLE_ADMIN'")
    private String memberAuth;
    
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