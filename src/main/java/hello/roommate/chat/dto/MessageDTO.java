package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 메시지 정보를 저장, 전송, 조회에 사용되는 DTO 클래스.
 * 사용자 id, 닉네임, 채팅방 id, 채팅 내역, 전송시간을 포함한다.
 *
 * @author Wootae
 */
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
