package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id;                    //채팅방 id
    private LocalDateTime updatedTime;  //마지막 대화 날짜
    private String message;             //마지막 대화 메시지
    private String nickname;            //상대 닉네임
}
