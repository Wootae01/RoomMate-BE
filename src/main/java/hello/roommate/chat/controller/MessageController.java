package hello.roommate.chat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;
	private final MessageMapper mapper;

	/**
	 * 메시지를 저장하는 REST 엔드포인트
	 *
	 * @param dto 저장할 메시지 정보를 담고 있는 DTO 객체
	 */
	@PostMapping("/message")
	public void save(@RequestBody MessageDTO dto) {
		Message message = mapper.convertToEntity(dto);
		messageService.save(message);
	}

	/**
	 * 특정 채팅방의 모든 메시지를 조회하는 REST 엔드포인트
	 *
	 * @param chatRoomId
	 * @return
	 */
	@GetMapping("/chatroom/{chatRoomId}/messages")
	public List<MessageDTO> findAllMessage(@PathVariable Long chatRoomId) {
		List<Message> messages = messageService.findAllByChatRoomId(chatRoomId);
		List<MessageDTO> result = new ArrayList<>();

		for (Message message : messages) {
			MessageDTO dto = mapper.convertToDTO(message);
			result.add(dto);
		}
		return result;
	}

}
