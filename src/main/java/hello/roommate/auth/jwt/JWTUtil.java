package hello.roommate.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

/**
 * JWT 토큰을 다루기 위한 util 클래스.
 */
@Component
public class JWTUtil {
	private final SecretKeySpec secretKey;

	public JWTUtil(@Value("${jwt.secret}") String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("username", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}

	public String getCategory(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("category", String.class);
	}

	public Authentication getAuthentication(String token) {
		String username = getUsername(token);
		String role = getRole(token);
		return new UsernamePasswordAuthenticationToken(username, null,
			Collections.singletonList(new SimpleGrantedAuthority(role)));
	}

	public Boolean isExpired(String token) {
		try {
			Date expiration = Jwts
				.parser().verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();

			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}

	}

	public String createJwt(String username, String role, String category, Long expiredMs) {
		return Jwts.builder()
			.claim("username", username)
			.claim("role", role)
			.claim("category", category)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}

}
