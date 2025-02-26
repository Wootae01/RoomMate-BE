package hello.roommate.auth.dto;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class EditMemberDTO {

	/**
	 * 사용자 정보 수정시 사용될 사용자 정보를 담고 있는 DTO
	 */
	private long userId;
	private String nickname;
	private String introduce;
	private int age;
	private Dormitory dormitory;
	private Gender gender;

	public EditMemberDTO(long userId, String nickname, String introduce, int age, Dormitory dormitory, Gender gender) {
		this.userId = userId;
		this.nickname = nickname;
		this.introduce = introduce;
		this.age = age;
		this.dormitory = dormitory;
		this.gender = gender;
	}
}
