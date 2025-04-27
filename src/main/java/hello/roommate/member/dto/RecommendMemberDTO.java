package hello.roommate.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * 추천 목록에서 사용될 사용자 정보를 담고 있는 DTO
 */
public class RecommendMemberDTO {
	private Long memberId;
	private String nickname;
	private String introduce;

	public RecommendMemberDTO(Long memberId, String nickname, String introduce) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.introduce = introduce;
	}

	public RecommendMemberDTO() {
	}
}
