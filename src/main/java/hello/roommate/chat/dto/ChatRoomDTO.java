package hello.roommate.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅 목록의 채팅방 정보를 저장하는 DTO 클래스
 * 채팅방 id, 마지막 대화 날짜, 마지막 대화 내역, 상대 닉네임 정보를 포함한다.
 *
 * @author Wootae
 */
@Getter
@Setter
@ToString
public class ChatRoomDTO {
	private Long chatRoomId;            //채팅방 id
	private LocalDateTime updatedTime;  //마지막 대화 날짜
	private String message;             //마지막 대화 메시지
	private String nickname;            //상대 닉네임
}
