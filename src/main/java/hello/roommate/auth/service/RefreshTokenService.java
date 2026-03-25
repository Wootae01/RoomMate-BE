package hello.roommate.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import hello.roommate.auth.dto.JWTTokenDTO;
import hello.roommate.auth.exception.ExpiredTokenException;
import hello.roommate.auth.exception.InvalidTokenException;
import hello.roommate.auth.exception.JWTErrorCode;
import hello.roommate.auth.exception.RefreshTokenException;
import hello.roommate.auth.jwt.JWTConstants;
import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RedisClient redisClient;
    private final JWTUtil jwtUtil;
    private static final String PREFIX = "refresh:";


    //refresh 토큰으로 새로운 access 토큰 재생성 메서드
    public JWTTokenDTO createNewToken(String refresh) {
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt(username, role, "access", JWTConstants.ACCESS_TOKEN_EXPIRATION);
        String newRefresh = jwtUtil.createJwt(username, role, "refresh", JWTConstants.REFRESH_TOKEN_EXPIRATION);

        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();
        jwtTokenDTO.setAccessToken(newAccess);
        jwtTokenDTO.setRefreshToken(newRefresh);

        return jwtTokenDTO;
    }

    //refresh 토큰 검증 메서드
    public void validateRefresh(String refresh) throws RefreshTokenException {
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            log.info("해당 토큰은 refresh 토큰이 아닙니다.");
            throw new InvalidTokenException(JWTErrorCode.INVALID_TOKEN.getMessage());
        }
        if (jwtUtil.isExpired(refresh)) {
            log.info("해당 refresh 토큰은 만료되었습니다.");
            throw new ExpiredTokenException(JWTErrorCode.EXPIRED_TOKEN.getMessage());
        }


        if (!existsByRefresh(refresh)) {
            log.info("해당 refresh 토큰은 존재하지 않습니다.");
            throw new InvalidTokenException(JWTErrorCode.INVALID_TOKEN.getMessage());
        }
    }

    public void saveEntity(String username, String refresh, Long expiration) {
        redisClient.set(PREFIX + username, refresh, Duration.ofMillis(expiration));
    }

    public boolean existsByRefresh(String refresh) {
        String username = jwtUtil.getUsername(refresh);
        Optional<String> optional = redisClient.get(PREFIX + username, new TypeReference<String>() {});
        return optional.map(stored -> stored.equals(refresh)).orElse(false);
    }

    public void deleteByRefresh(String refresh) {
        String username = jwtUtil.getUsername(refresh);
        redisClient.delete(PREFIX + username);
    }

    public void deleteByUsername(String username) {
        redisClient.delete(PREFIX + username);
    }
}
