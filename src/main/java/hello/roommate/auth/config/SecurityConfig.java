package hello.roommate.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import hello.roommate.auth.jwt.CustomLogoutFilter;
import hello.roommate.auth.jwt.JWTFilter;
import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.auth.service.RefreshEntityService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JWTUtil jwtUtil;
	private final RefreshEntityService refreshEntityService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
		Exception {

		http
			.csrf((auth) -> auth.disable());

		http
			.formLogin((auth) -> auth.disable());

		http
			.httpBasic((auth) -> auth.disable());

		http
			.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		http
			.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshEntityService), LogoutFilter.class);

		//경로별 인가 작업
		http
			.authorizeHttpRequests(
				(auth) -> auth
					.requestMatchers("/auth/token").permitAll()
					.requestMatchers("/auth/reissue").permitAll()
					.requestMatchers("/ws/chat").permitAll()
					.requestMatchers("/actuator/**").permitAll()
					.anyRequest().authenticated());

		//세션 설정. state less로 설정
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
