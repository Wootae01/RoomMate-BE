package hello.roommate.chat.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.service.ChatService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

	private final ChatService chatService;
	private final MemberService memberService;

	//메시지 전송
	@MessageMapping("/room/{roomId}")
	@SendTo("/topic/{roomId}")
	public Message sendMessage(String senderId, Long roomId, String content) {
		ChatRoom room = chatService.findRoomById(roomId);
		Member sender = memberService.findById(senderId);
		LocalDateTime now = LocalDateTime.now();
		Message message = new Message();
		message.setContent(content);
		message.setChatRoom(room);
		message.setSender(sender);
		message.setSendTime(now);

		Message save = chatService.save(message);
		room.setUpdatedTime(now);
		return save;
	}

}
