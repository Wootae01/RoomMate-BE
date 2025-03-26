package hello.roommate.auth.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
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
			writeErrorResponse(response, JWTErrorCode.INVALID_TOKEN.getCode(), JWTErrorCode.INVALID_TOKEN.getMessage());
			return;
		}

		String[] split = header.split(" ");
		String token = split[1];

		//토큰 만료 확인
		if (jwtUtil.isExpired(token)) {
			log.info("{}, 토큰이 만료되었습니다.", request.getRequestURI());
			writeErrorResponse(response, JWTErrorCode.EXPIRED_TOKEN.getCode(), JWTErrorCode.EXPIRED_TOKEN.getMessage());
			return;
		}

		String category = jwtUtil.getCategory(token);

		//로그인 용 토큰인지 확인
		if (category.equals("login")) {
			String requestURI = request.getRequestURI();
			if (!requestURI.equals("/auth/login")) {
				log.info("서버 토큰이 아닙니다.");
				writeErrorResponse(response, JWTErrorCode.INVALID_TOKEN.getCode(),
					JWTErrorCode.INVALID_TOKEN.getMessage());
				return;
			}

			//엑세스 토큰이 아닌지 확인
		} else if (!category.equals("access")) {
			log.info("access 토큰이 아닙니다.");
			writeErrorResponse(response, JWTErrorCode.INVALID_TOKEN.getCode(), JWTErrorCode.INVALID_TOKEN.getMessage());
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
		return path.equals("/auth/token")
				|| path.equals("/auth/reissue")
				|| path.startsWith("/ws")
				|| path.matches("^/members/[^/]+/resign$");
	}

	private void writeErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write("{\"code\":\"" + code + "\", \"message\":\"" + message + "\"}");
		writer.flush();
	}
}
