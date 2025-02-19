package hello.roommate.chat.controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message")
    public void save(@RequestBody MessageDTO dto) {
        Message message = messageService.convertToEntity(dto);
        messageService.save(message);
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
