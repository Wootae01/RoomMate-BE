package hello.roommate;

import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.interceptor.AuthInterceptor;
import hello.roommate.interceptor.LogInterceptor;
import hello.roommate.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	private final JWTUtil jwtUtil;
	private final MemberService memberService;

	public WebConfig(JWTUtil jwtUtil, MemberService memberService) {
		this.jwtUtil = jwtUtil;
		this.memberService = memberService;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOriginPatterns("*")
					.allowedMethods("*")
					.allowCredentials(true);
			}
		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**", "/*.ico", "/error");

		registry.addInterceptor(new AuthInterceptor(jwtUtil, memberService))
				.order(2)
				.addPathPatterns(
					"/members/{memberId}/resign",
					"/members/{memberId}/basic",
					"/members/{memberId}/lifestyle",
					"/members/{memberId}/preference",
					"/members/{memberId}/editprofile",
					"/members/{memberId}/editlifestyle",
					"/members/{memberId}/editpreference",
					"/members/{memberId}/chatrooms"
				);
	}
}
