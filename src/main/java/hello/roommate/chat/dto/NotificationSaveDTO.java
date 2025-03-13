package hello.roommate.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자 알림 토큰 저장 시 사용하는 DTO
 */
@Getter
@Setter
@ToString
public class NotificationSaveDTO {
	@NotNull(message = "{NotNull.userId}")
	private Long memberId;

	@NotBlank(message = "{NotBlank}")
	private String token;
}
