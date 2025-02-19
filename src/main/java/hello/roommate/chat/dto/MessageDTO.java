package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDTO {
    private String nickname;
    private Long chatRoomId;
    private String content;
    private LocalDateTime sendTime;
}
