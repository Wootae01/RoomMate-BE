package hello.roommate.auth.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import hello.roommate.auth.exception.JWTErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			log.info("{} 토큰이 존재하지 않거나 Bearer 타입이 아닙니다.", request.getRequestURI());
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":" + JWTErrorCode.INVALID_TOKEN.getCode() + ", \"message\":"
				+ JWTErrorCode.INVALID_TOKEN.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		String[] split = header.split(" ");
		String token = split[1];

		//토큰 만료 확인
		if (jwtUtil.isExpired(token)) {
			log.info("토큰이 만료되었습니다.");
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":" + JWTErrorCode.EXPIRED_TOKEN.getCode() + ", \"message\":"
				+ JWTErrorCode.EXPIRED_TOKEN.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String category = jwtUtil.getCategory(token);

		//로그인 용 토큰인지 확인
		if (category.equals("login")) {
			String requestURI = request.getRequestURI();
			if (!requestURI.equals("/auth/login")) {
				PrintWriter writer = response.getWriter();
				writer.write("{\"code\":" + JWTErrorCode.INVALID_TOKEN.getCode() + ", \"message\":"
					+ JWTErrorCode.INVALID_TOKEN.getMessage());

				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			//엑세스 토큰이 아닌지 확인
		} else if (!category.equals("access")) {
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":" + JWTErrorCode.INVALID_TOKEN.getCode() + ", \"message\":"
				+ JWTErrorCode.INVALID_TOKEN.getMessage());

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
			null, authorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		return path.equals("/auth/token") || path.equals("/auth/reissue");
	}
}
