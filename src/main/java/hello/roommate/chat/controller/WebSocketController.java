package hello.roommate.chat.controller;

import java.util.HashMap;
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
		Message message = messageService.convertToEntity(messageReceiveDTO);
		Message save = messageService.save(message);
		String nickname = save.getSender().getNickname();

		//메시지 전송 DTO 생성
		MessageDTO sendDTO = new MessageDTO();
		sendDTO.setSendTime(messageReceiveDTO.getSendTime());
		sendDTO.setNickname(nickname);
		sendDTO.setContent(messageReceiveDTO.getContent());
		sendDTO.setMemberId(messageReceiveDTO.getMemberId());
		sendDTO.setChatRoomId(messageReceiveDTO.getChatRoomId());

		//알림 요청

		//해당 채팅 방의 상대 찾기
		Member opponent = memberChatRoomRepository.findByMemberExceptMyId(messageReceiveDTO.getChatRoomId(),
				messageReceiveDTO.getMemberId())
			.orElseThrow(() -> new NoSuchElementException("채팅 상대를 찾을 수 없습니다."));

		Optional<Notification> optionalNotification = notificationService.findByMemberId(opponent.getId());

		//상대방이 알림 권한 허용을 안했으면 알림 보내지 않음
		if (optionalNotification.isEmpty()) {
			return Mono.just(sendDTO);
		}

		NotificationPushDTO pushDTO = new NotificationPushDTO();
		pushDTO.setTo(optionalNotification.get().getToken());
		pushDTO.setBody(messageReceiveDTO.getContent());
		pushDTO.setTitle(nickname);
		pushDTO.setSound("default");
		pushDTO.setData(new HashMap<>());

		// push 알림 전송 후 sendDTO 반환
		return pushNotificationService.sendNotification(pushDTO)
			.thenReturn(sendDTO);
	}

}
