package com.easyeng.mschoi.filters;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.easyeng.mschoi.service.AuthService;
import com.easyeng.mschoi.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtRequestFilter extends UsernamePasswordAuthenticationFilter{

	private JwtUtil jwtUtil;
	
    // AuthenticationManager를 Setter로 주입
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
    }
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 헤더값에서 JWT 추출
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		// WebConfig 의 WebMvcConfigurer 보다 먼저 필터를거치기 때문에
		// response 를 하기 위해 Origin 을 재설정 해주어야한다.
        res.setHeader("Access-Control-Allow-Origin", "http://localhost");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        
		
		final String authHeader = req.getHeader("Authorization");
		String memberId = null;
		String memberEmail = null;
		String memberAuth = null;
		String jwt = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7); // Bearer 을 제외한 헤더 (jwt)
			
			try {
				// 토큰이 만료되었을경우 401 return
				if (jwt != null && jwtUtil.isTokenExpired(jwt)) {
					res.setStatus(401);
		            res.getWriter().write("세션이 만료되었습니다");
					return;
				}
				
				// 정상적일 경우 Auth 에 관한 로직 생성
				if (authHeader != null && authHeader.startsWith("Bearer ") ) {
					memberId = jwtUtil.extractMemberId(jwt);	// jwt 에서 멤버Id 추출
					memberEmail = jwtUtil.extractMemberEmail(jwt);	// jwt 에서 멤버Id 추출
					memberAuth = jwtUtil.extractMemberAuth(jwt);	// jwt 에서 멤버 권한 추출
				}
				
			} catch (Exception e) {
				res.setStatus(401);
				res.getWriter().write("정상적인 세션이 아닙니다");
				return;
			}
			
		}
		
		super.doFilter(request, response, chain);
	}
}
