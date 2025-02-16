package hello.roommate.chat.controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message")
    public void save(@RequestBody MessageDTO dto) {
        Message message = messageService.convertToEntity(dto);
        messageService.save(message);
    }

}
