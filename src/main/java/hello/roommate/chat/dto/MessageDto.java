package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
	private String content;

	public MessageDto(String content) {
		this.content = content;
	}

	public MessageDto() {
	}
}
