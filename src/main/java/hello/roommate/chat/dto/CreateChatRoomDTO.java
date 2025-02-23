package hello.roommate.chat.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 채팅방 생성할 때 필요한 DTO 정보를 저장하는 DTO 클래스.
 * 1대1 채팅에 참여하는 memberId 포함한다.
 *
 * @author Wootae
 */
@Getter
@Setter
public class CreateChatRoomDTO {
	private Long member1Id;
	private Long member2Id;
}
