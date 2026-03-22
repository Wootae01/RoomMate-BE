package hello.roommate.interceptor;

import hello.roommate.auth.jwt.JWTUtil;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 요청한 사용자가 path variable의 memberId와 동일한지 검증하는 인터셉터.
 * JWT 토큰에서 username을 추출하고 DB에서 memberId를 조회하여 비교한다.
 * 일치하지 않으면 403 반환.
 */
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // path variable에서 memberId 추출
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String memberIdStr = pathVariables.get("memberId");
        if (memberIdStr == null) {
            return true;
        }
        Long memberId = Long.parseLong(memberIdStr);

        // JWT에서 username 추출
        String header = request.getHeader("Authorization");
        String username = jwtUtil.getUsername(header.split(" ")[1]);

        // username으로 memberId 조회 후 path variable의 memberId와 비교
        Member tokenMember = memberService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자입니다."));

        if (!tokenMember.getId().equals(memberId)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}
