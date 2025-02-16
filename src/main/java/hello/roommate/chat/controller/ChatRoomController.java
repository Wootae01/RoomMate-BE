package hello.roommate.chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatService;
	private final MessageService messageService;

	//채팅방 생성 후 id 반환
	@PostMapping("/chatroom")
	public ResponseEntity<Map<String, Object>> createChatRoom(Long member1Id, Long member2Id) {
		ChatRoom chatRoom = chatService.createChatRoom(member1Id, member2Id);
		Map<String, Object> response = Map.of("chat_room_id", chatRoom.getId());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//해당 채팅방의 모든 메시지 조회
	@GetMapping("/chatroom/{chatRoomId}/messages")
	public List<MessageDTO> findAllMessage(@PathVariable Long chatRoomId) {
		List<Message> messages = messageService.findAllByChatRoomId(chatRoomId);
		List<MessageDTO> result = new ArrayList<>();

		for (Message message : messages) {
			MessageDTO dto = messageService.convertToDTO(message);
			result.add(dto);
		}
		return result;
	}
}
