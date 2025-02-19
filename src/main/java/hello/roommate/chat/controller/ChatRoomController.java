package hello.roommate.chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatService;

    //채팅방 생성 후 id 반환
    @PostMapping("/chatroom")
    public ResponseEntity<Map<String, Object>> createChatRoom(@RequestBody CreateChatRoomDTO dto) {
        ChatRoom chatRoom = chatService.createChatRoom(dto);
        Map<String, Object> response = Map.of("chat_room_id", chatRoom.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
