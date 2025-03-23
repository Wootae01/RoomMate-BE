package hello.roommate.chat;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import hello.roommate.auth.exception.InvalidTokenException;
import hello.roommate.auth.exception.MissingTokenException;
import hello.roommate.auth.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;

/**
 * 웹소켓 관련 설정을 정의한 클래스
 *
 * @author Wootae
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JWTUtil jwtUtil;

	/**
	 * 메시지 브로커 설정하는 메서드.
	 * 클라이언트가 구독할 수 있는 브로커, 메시지를 보낼 때 사용할 prefix 설정
	 *
	 * @param registry 메시지 브로커 레지스트리
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//구독할 때 사용할 메시지 브로커의 prefix /topic 으로 설정
		registry.enableSimpleBroker("/topic");

		//메시지 보낼 때 사용할 prefix /app/chatroom 으로 설정
		registry.setApplicationDestinationPrefixes("/app/chatroom");
	}

	/**
	 * Stomp 엔드포인트를 등록하는 메서드.
	 * 클라이언트가 연결할 엔드포인트 주소를 정의하고, CORS 설정 및 인터셉터 등록
	 *
	 * @param registry Stomp 엔드포인트 레지스트리
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/chat") //
			.setAllowedOriginPatterns("*")    //임시로 모든 오리진 허용
			.addInterceptors(new WebSocketInterceptor()); //인터셉터 추가

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					List<String> authHeaders = accessor.getNativeHeader("Authorization");

					if (authHeaders == null || authHeaders.isEmpty()) {
						throw new MissingTokenException();
					}
					String authorization = authHeaders.get(0);
					if (!authorization.startsWith("Bearer ")) {
						throw new InvalidTokenException();
					}

					String[] split = authorization.split(" ");
					String token = split[1];
					Authentication authentication = jwtUtil.getAuthentication(token);
					accessor.setUser(authentication);
				}
				return message;
			}
		});
	}
}
