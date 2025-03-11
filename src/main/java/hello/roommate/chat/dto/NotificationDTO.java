package hello.roommate.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationDTO {
	@NotNull(message = "{NotNull.userId}")
	private Long memberId;

	@NotBlank(message = "{NotBlank}")
	private String token;
}
