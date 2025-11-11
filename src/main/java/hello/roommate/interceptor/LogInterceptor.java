package hello.roommate.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static hello.roommate.auth.jwt.JWTFilter.USERNAME;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();


        String logId = (String) request.getAttribute(LOG_ID);
        if (logId != null) {
            log.info("REQUEST [{}] [{}][{}]", logId, requestURI, handler);
        } else {
            String uuid = UUID.randomUUID().toString();
            request.setAttribute(LOG_ID, uuid);
            log.info("REQUEST [{}] [{}][{}]", uuid, requestURI, handler);
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String logId = (String)request.getAttribute(LOG_ID);
        String requestURI = request.getRequestURI();

        if (ex != null) {
            log.error("after completion error", ex);
        }
        log.info("RESPONSE[{}] [{}]", logId, requestURI);
    }
}
