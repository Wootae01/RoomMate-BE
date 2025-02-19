package hello.roommate.chat.controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final MessageService messageService;

    //메시지 전송
    @MessageMapping("/{roomId}")
    @SendTo("/topic/chatroom/{roomId}")
    public MessageDTO sendMessage(MessageDTO messageDto) {
        Message message = messageService.convertToEntity(messageDto);
        messageService.save(message);

        return messageDto;
    }

}
