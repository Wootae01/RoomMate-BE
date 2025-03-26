package hello.roommate.auth.jwt;

import hello.roommate.auth.exception.JWTErrorCode;
import hello.roommate.auth.repository.RefreshEntityRepository;
import hello.roommate.auth.service.RefreshEntityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshEntityService refreshEntityService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //uri 확인
        String uri = request.getRequestURI();
        if (!uri.equals("/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        //post 요청 확인
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("로그 아웃 요청");

        //헤더 확인
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.info("헤더 오류={}", header);
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

        //refresh 토큰이 맞는지 확인
        String category = jwtUtil.getCategory(token);
        if(!category.equals("refresh")){
            log.info("refresh token 아닙니다.");
            writeErrorResponse(response, JWTErrorCode.INVALID_TOKEN.getCode(), JWTErrorCode.INVALID_TOKEN.getMessage());
            return;
        }

        //토큰 확인
        Boolean isExist = refreshEntityService.existsByRefresh(token);
        if (!isExist) {
            log.info("해당 토큰이 존재하지 않습니다.");
            writeErrorResponse(response, JWTErrorCode.INVALID_TOKEN.getCode(), JWTErrorCode.INVALID_TOKEN.getMessage());
            return;
        }
        log.info("refresh token={}", token);
        refreshEntityService.deleteByRefresh(token);
        response.setStatus(HttpServletResponse.SC_OK);
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
