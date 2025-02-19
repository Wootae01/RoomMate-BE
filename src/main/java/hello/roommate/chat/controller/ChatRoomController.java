package hello.roommate.chat.controller;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
