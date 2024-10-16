package com.easyeng.mschoi.utils;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {
	
	// 서명을 위한 시크릿 키 생성 size >= 512 bits, Base64 Encoding
	private String SECRET_KEY = Base64.getEncoder().encodeToString("kjhqwehaksjdhkzasdaasdasdasdasdasdsdasdjxchqkjwehkljhlkasjdlkaslkdjqwljk".getBytes());
	
	// 모든 클레임 추출 
	public Claims extractClaimsAll(String jwt) {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
		return jwtParser.parseClaimsJws(jwt).getBody();
	}
	
	// 개별 클레임 추출
	public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
		final Claims claims = extractClaimsAll(jwt);
		return claimsResolver.apply(claims);
	}
	
	// 토큰에서 사용자 이름 추출
	public String extractMemberId(String jwt) {
		return extractClaim(jwt, Claims::getSubject);	// Claims 의 getSubject 메서드를 사용해 subject 추출
	}
	
	// 토큰에서 만료시간 추출
	public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Claims 의 getExpiration 메서드를 사용해 만료시간 추출
    }
	
	// 토큰 만료 여부 추출 (만료시간이 현재 날짜 이전인지 확인)
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());	// before 메서드 : 날짜가 그 이전인지 확인
	}
	
	// JWT 토큰 생성
	public String createJWT(Map<String, Object> claims, String subject) {
		return Jwts.builder()
					.setClaims(claims)	// claim("userId", userid).claim("userEmail", userEmail) 처럼도 가능
					.setSubject(subject)
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
					.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
					.compact();
	}
	
	// JWT 토큰 검증
	public Boolean validateToken(String jwt, String memberId) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwt);
			return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT Token" + e);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token" + e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token" + e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty." + e);
        }
        return false;
	}
}
