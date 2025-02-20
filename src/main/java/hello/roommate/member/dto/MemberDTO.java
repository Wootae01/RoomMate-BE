package hello.roommate.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
	private Long memberId;
	private String nickname;
	private String introduce;
}
