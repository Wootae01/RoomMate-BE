package hello.roommate.chat.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

	private final ChatRoomService chatService;

	//채팅방 생성 후 id 반환
	@PostMapping("/chatroom")
	public ResponseEntity<Map<String, Object>> createChatRoom(@RequestBody CreateChatRoomDTO dto) {
		log.info("채팅방 생성 요청");

		ChatRoom chatRoom = chatService.createChatRoom(dto);
		Map<String, Object> response = Map.of("chatRoomId", chatRoom.getId());

		log.info("채팅방 id 반환 chatRoomId={}", chatRoom.getId());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
