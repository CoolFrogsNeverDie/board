package com.board.security;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.board.dto.MemberLoginRes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig.java
 * 
 * Spring Security 설정
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	/**
	 * 비밀번호 암호화
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Http 보안 설정
	 * 
	 * @param http
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// CSRF 비활성화
			.csrf(csrf -> csrf.disable())

			// 모든 요청을 로그인 인증 없이 허용
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			)

			// H2 콘솔용 설정
			.headers(headers -> headers
				.frameOptions(frame -> frame.sameOrigin()) // H2 콘솔에서 iframe 사용 허용
			)

			// 로그인 설정
			.formLogin(form -> form
				.loginPage("/member/login")  // 로그인 페이지 경로 설정
				.loginProcessingUrl("/login/proc") // 로그인 처리 URL
				.successHandler(authenticationSuccessHandler()) // 성공 핸들러 등록
				.failureHandler(authenticationFailureHandler()) // 실패 핸들러 등록
				.permitAll()
			)

			// 로그아웃 설정
			.logout(logout -> logout
				.logoutUrl("/logout") // 로그아웃 URL
				.logoutSuccessUrl("/member/login") // 로그아웃 성공 후 이동할 URL
			);

		return http.build();
	}

	/**
	 * AuthenticationManager 설정
	 * 
	 * @param http
	 * @param passwordEncoder
	 * @return AuthenticationManager 인증 담당
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			HttpSecurity http
			, PasswordEncoder passwordEncoder) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // DaoAuthenticationProvider 생성
		authProvider.setUserDetailsService(inMemoryUserDetailsManager()); // 사용자 정보 - 인메모리 방식
		authProvider.setPasswordEncoder(passwordEncoder); // 비밀번호 암호화 설정

		return new ProviderManager(authProvider);
	}

	/**
	 * 인메모리 사용자 설정
	 * 
	 * @return InMemoryUserDetailsManager (인메모리 사용자 관리 객체)
	 */
	@Bean
	public UserDetailsService inMemoryUserDetailsManager() {
		UserDetails user1 = User.withUsername("test1")
			.password(passwordEncoder().encode("1234"))
			.roles("USER")
			.build();

		UserDetails user2 = User.withUsername("test2")
			.password(passwordEncoder().encode("1234"))
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(user1, user2); //인메모리 사용자 관리자 반환
	}

	/**
	 * 로그인 성공 핸들러 - 세션 유저 정보 저장 후 /board 리디렉션
	 * @return AuthenticationSuccessHandler
	 */
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) ->{
			// 인증 성공 시 사용자 정보를 세션에 저장
			HttpSession session = request.getSession();
			String name = authentication.getName();
			session.setAttribute("currentUser", new MemberLoginRes(name, "인메모리")); // 로그인 유저 정보 session 저장
			response.sendRedirect("/board/list"); // 성공 후 /board 페이지로 리디렉션
		};
	}

	/**
	 * 로그인 실패 핸들러 - 로그인 실패 페이지 리디렉션
	 * @return AuthenticationFailureHandler
	 */
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return (request, response, exception) -> {
			response.sendRedirect("/member/login?isFail=true");
		};
	}

}
