package hello.roommate.chat.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.chat.dto.NotificationPushDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.chat.service.NotificationService;
import hello.roommate.chat.service.PushNotificationService;
import hello.roommate.mapper.MessageMapper;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

	private final MessageService messageService;
	private final NotificationService notificationService;
	private final MemberChatRoomRepository memberChatRoomRepository;
	private final PushNotificationService pushNotificationService;
	private final MessageMapper mapper;

	/**
	 * 웹소켓을 통해 메시지를 받아 처리하고, 저장 후 동일한 메시지를 구독자에게 전송하는 메서드
	 *
	 * @param messageReceiveDTO 전송할 메시지 정보 담고 있는 DTO 객체
	 * @return 저장 후 클라이언트에게 전송할 메시지 DTO 객체
	 */
	@MessageMapping("/{roomId}")
	@SendTo("/topic/chatroom/{roomId}")
	public Mono<MessageDTO> sendMessage(MessageReceiveDTO messageReceiveDTO) {
		log.info("arrive message");
		log.info("message ={}", messageReceiveDTO);
		Message message = mapper.convertToEntity(messageReceiveDTO);
		Message save = messageService.save(message);
		String nickname = save.getSender().getNickname();

		//메시지 전송 DTO 생성
		MessageDTO sendDTO = mapper.convertReceiveToMessageDTO(messageReceiveDTO, nickname);

		//알림 요청

		//해당 채팅 방의 상대 찾기
		Member opponent = memberChatRoomRepository.findByMemberExceptMyId(messageReceiveDTO.getChatRoomId(),
				messageReceiveDTO.getMemberId())
			.orElseThrow(() -> new NoSuchElementException("채팅 상대를 찾을 수 없습니다."));

		Optional<Notification> optionalNotification = notificationService.findByMemberId(opponent.getId());

		//상대방이 알림 권한 허용을 안했으면 알림 전송 x (토큰 생성 되있지 않으면)
		if (optionalNotification.isEmpty()) {
			return Mono.just(sendDTO);
		}
		//알림 허용 하지 않았으면 알림 전송 x
		Notification notification = optionalNotification.get();
		if (!notification.getPermission()) {
			return Mono.just(sendDTO);
		}

		NotificationPushDTO pushDTO = getNotificationPushDTO(messageReceiveDTO, notification, nickname);

		// push 알림 전송 후 sendDTO 반환
		return pushNotificationService.sendNotification(pushDTO)
			.thenReturn(sendDTO);
	}

	private static NotificationPushDTO getNotificationPushDTO(MessageReceiveDTO messageReceiveDTO,
		Notification notification, String nickname) {
		NotificationPushDTO pushDTO = new NotificationPushDTO();
		pushDTO.setTo(notification.getToken());
		pushDTO.setBody(messageReceiveDTO.getContent());
		pushDTO.setTitle(nickname);
		pushDTO.setSound("default");
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("chatRoomId", String.valueOf(messageReceiveDTO.getChatRoomId()));
		pushDTO.setData(dataMap);
		return pushDTO;
	}

}
