package hello.roommate.chat.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import hello.roommate.chat.dto.NotificationPushDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PushNotificationService {
	private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

	private final WebClient webClient;

	public PushNotificationService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(EXPO_PUSH_URL)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
			.build();
	}

	public Mono<String> sendNotification(NotificationPushDTO dto) {
		return webClient.post()
			.body(BodyInserters.fromValue(dto))
			.retrieve()
			.bodyToMono(String.class)
			.doOnSuccess(response -> log.info("푸시 알림 요청 성공 {}", response))
			.doOnError(error -> log.info("푸시 알림 요청 실패"));
	}

}
