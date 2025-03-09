package hello.roommate.auth.dto;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMemberDTO {

	/**
	 * 사용자 정보 수정시 사용될 사용자 정보를 담고 있는 DTO
	 */
	@NotNull(message = "{NotNull.userId}")
	private Long userId;

	@NotBlank(message = "{NotBlank.nickname}")
	@Size(max = 10, message = "{Size.nickname}")
	private String nickname;

	@Size(message = "{Size.introduce}")
	private String introduce;

	@NotNull(message = "{NotNull.age}")
	private int age;

	@NotNull(message = "{NotNull.dormitory}")
	private Dormitory dormitory;

	@NotNull(message = "{NotNull.gender}")
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
