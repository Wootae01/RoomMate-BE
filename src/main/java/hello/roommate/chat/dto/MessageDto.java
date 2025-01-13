package hello.roommate.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
	private String senderId;
	private Long chatRoomId;
	private String content;
	private LocalDateTime sendTime;
}
