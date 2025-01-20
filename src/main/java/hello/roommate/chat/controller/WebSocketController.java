package hello.roommate.chat.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDto;
import hello.roommate.chat.service.ChatService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

	private final ChatService chatService;
	private final MemberService memberService;

	//메시지 전송
	@MessageMapping("/{roomId}")
	@SendTo("/topic/chatroom/{roomId}")
	public MessageDto sendMessage(MessageDto messageDto) {
		Message message = toMessage(messageDto);
		chatService.save(message);

		return messageDto;
	}

	private Message toMessage(MessageDto messageDto) {
		ChatRoom room = chatService.findRoomById(messageDto.getChatRoomId());
		Member member = memberService.findById(messageDto.getSenderId());

		Message message = new Message();
		message.setSendTime(LocalDateTime.now());
		message.setContent(messageDto.getContent());
		message.setChatRoom(room);
		message.setSender(member);
		return message;
	}
}
