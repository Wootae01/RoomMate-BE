package hello.roommate.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 메시지를 받을 때 사용되는 DTO 클래스
 */
@Getter
@Setter
@ToString
public class MessageReceiveDTO {

	@NotNull(message = "{NotNull.userId}")
	private Long memberId;
	@NotNull(message = "{NotNull.chatRoomId}")
	private Long chatRoomId;

	@NotBlank(message = "{NotBlank.content}")
	private String content;

	@NotNull(message = "{NotNull.sendTime}")
	private String sendTime;
}
