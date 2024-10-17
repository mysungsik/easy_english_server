package com.easyeng.mschoi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.easyeng.mschoi.filters.JwtRequestFilter;
import com.easyeng.mschoi.service.AuthService;
import com.easyeng.mschoi.service.CustomUserDetailsService;
import com.easyeng.mschoi.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, authService);
		
		
		http
		.csrf((csrfConfig)-> csrfConfig.disable())	// csrf 검증 제거
		.headers((headerConfig) ->					// 헤더설정
				headerConfig.frameOptions(frameOptionConfig -> frameOptionConfig.disable()))
		.authorizeHttpRequests((authorizeRequest) -> // 인증 설정
				authorizeRequest
					.requestMatchers("/api/signup/**", "/api/login/**").permitAll()
					.requestMatchers("/**").permitAll()	// 일단 전부 허용하도록. 테스트목적
					.anyRequest().authenticated());		// 나머지는 전부 확인 필요
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	// Spring Security 에서 사용자 인증을 처리하는 인터페이스
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); // CustomUserDetailsService 설정
        authProvider.setPasswordEncoder(passwordEncoder()); // BCryptPasswordEncoder 설정
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // AuthenticationManager 설정
    }
}
