package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageDTO {
	private Long memberId;
	private String nickname;
	private Long chatRoomId;
	private String content;
	private String sendTime;
}
