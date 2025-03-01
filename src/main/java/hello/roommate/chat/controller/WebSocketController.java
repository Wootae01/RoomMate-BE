package hello.roommate.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

	private final MessageService messageService;

	/**
	 * 웹소켓을 통해 메시지를 받아 처리하고, 저장 후 동일한 메시지를 구독자에게 전송하는 메서드
	 *
	 * @param messageDto 전송할 메시지 정보 담고 있는 DTO 객체
	 * @return 저장 후 클라이언트에게 전송할 메시지 DTO 객체
	 */
	@MessageMapping("/{roomId}")
	@SendTo("/topic/chatroom/{roomId}")
	public MessageDTO sendMessage(MessageDTO messageDto) {
		log.info("arrive message");
		log.info("message ={}", messageDto);
		Message message = messageService.convertToEntity(messageDto);
		Message save = messageService.save(message);
		String nickname = save.getSender().getNickname();
		messageDto.setNickname(nickname);
		return messageDto;
	}

}
