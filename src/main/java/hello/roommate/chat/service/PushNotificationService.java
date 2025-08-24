package hello.roommate.chat.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.chat.dto.NotificationPushDTO;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PushNotificationService {
	private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

	private final WebClient webClient;
	private final MemberService memberService;
	private final NotificationService notificationService;

	public PushNotificationService(WebClient.Builder webClientBuilder, MemberService memberService,
		NotificationService notificationService) {
		this.webClient = webClientBuilder.baseUrl(EXPO_PUSH_URL)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
			.build();
		this.memberService = memberService;
		this.notificationService = notificationService;
	}

	public Mono<String> sendNotification(NotificationPushDTO dto) {

		return webClient.post()
			.body(BodyInserters.fromValue(dto))
			.retrieve()
			.bodyToMono(String.class)
			.doOnSuccess(response -> log.debug("푸시 알림 요청 성공 {}", response))
			.doOnError(error -> log.debug("푸시 알림 요청 실패"));
	}

	public Mono<String> sendNotificationIfAllowed(MessageReceiveDTO messageDto, Long targetMemberId) {
		Optional<Notification> optionalNotification = notificationService.findByMemberId(targetMemberId);

		if (optionalNotification.isEmpty()) {
			log.debug("푸시 토큰 존재 X: memberId={}", targetMemberId);
			return Mono.empty(); // 전송 안 함
		}

		Notification notification = optionalNotification.get();
		if (!notification.getPermission()) {
			log.debug("푸시 권한 없음: memberId={}", targetMemberId);
			return Mono.empty(); // 전송 안 함
		}

		NotificationPushDTO pushDTO = getNotificationPushDTO(messageDto, notification);

		return sendNotification(pushDTO)
			.doOnError(e -> log.warn("푸시 전송 실패 memberId={}, error={}", targetMemberId, e.getMessage()));
	}

	public NotificationPushDTO getNotificationPushDTO(MessageReceiveDTO messageReceiveDTO, Notification notification) {
		Member sender = memberService.findById(messageReceiveDTO.getMemberId());

		NotificationPushDTO pushDTO = new NotificationPushDTO();
		pushDTO.setTo(notification.getToken());
		pushDTO.setBody(messageReceiveDTO.getContent());
		pushDTO.setTitle(sender.getNickname());
		pushDTO.setSound("default");
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("chatRoomId", String.valueOf(messageReceiveDTO.getChatRoomId()));
		pushDTO.setData(dataMap);
		return pushDTO;
	}

}
