package hello.roommate.chat.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatService chatService;

	//채팅방 생성 후 id 반환
	@PostMapping("/chatroom")
	public ResponseEntity<Map<String, Object>> createChatRoom(Long member1Id, Long member2Id) {
		ChatRoom chatRoom = chatService.createChatRoom(member1Id, member2Id);
		Map<String, Object> response = Map.of("chat_room_id", chatRoom.getId());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
