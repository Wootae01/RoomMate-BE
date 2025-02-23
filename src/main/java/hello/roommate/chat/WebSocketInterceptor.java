package hello.roommate.chat;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 웹소켓 연결 요청 로그를 작성하기 위한 Interceptor
 *
 * @author Wootae
 */
public class WebSocketInterceptor implements HandshakeInterceptor {
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) throws Exception {
		log.info("WebSocket 연결 요청 uri={}", request.getURI());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {
		log.info("WebSocket 연결 완료 uri={}", request.getURI());
	}

}
