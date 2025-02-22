package hello.roommate.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
	private Long memberId;
	private String nickname;
	private String introduce;
}
