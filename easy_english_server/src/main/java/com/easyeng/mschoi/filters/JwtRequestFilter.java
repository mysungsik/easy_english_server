package com.easyeng.mschoi.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.easyeng.mschoi.service.AuthService;
import com.easyeng.mschoi.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

public class JwtRequestFilter extends UsernamePasswordAuthenticationFilter{

	private JwtUtil jwtUtil;
	
	private AuthService authService;
	
    // AuthenticationManager를 Setter로 주입
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 헤더값에서 JWT 추출
		HttpServletRequest req = (HttpServletRequest)request;
		final String authHeader = req.getHeader("Authorization");
		String memberId = null;
		String memberEmail = null;
		String memberAuth = null;
		String jwt = null;
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7); // Bearer 을 제외한 헤더 (jwt)
			memberId = jwtUtil.extractMemberId(jwt);	// jwt 에서 멤버Id 추출
			memberEmail = jwtUtil.extractMemberEmail(jwt);	// jwt 에서 멤버Id 추출
			memberAuth = jwtUtil.extractMemberAuth(jwt);	// jwt 에서 멤버 권한 추출
		}
		
//		// JWT 확인
//		// 만약 JWT 에 멤버이름이 들어있지만, 아직 인증되지 않은 요청일 경우 인증을 진행
//		if (memberId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//			Member memberInfo = authService.getMemberInfoById(memberId);
//			
//			// 토큰 검증
//			if (jwtUtil.validateToken(jwt, memberInfo.getMemberId())) {
//				UsernamePasswordAuthenticationToken authToken 
//					= new UsernamePasswordAuthenticationToken(memberInfo, null, memberInfo.)
//			}
//		}
		
		super.doFilter(request, response, chain);
	}
}
