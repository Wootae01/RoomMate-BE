package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatRoomDTO {
    private Long member1Id;
    private Long member2Id;
}
