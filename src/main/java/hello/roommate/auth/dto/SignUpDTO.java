package hello.roommate.auth.dto;

import java.util.List;
import java.util.Map;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import hello.roommate.validator.ValidLifeStyle;
import hello.roommate.validator.ValidPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//JSON으로 프론트에서 회원가입폼으로 입력한 값들 넘겨줌
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SignUpDTO {
	@NotNull
	private Long userId;

	@NotBlank(message = "닉네임은 필수 값 입니다.")
	@Size(max = 10, message = "닉네임의 최대 길이는 10글자 입니다.")
	private String nickname;

	@Size(max = 60, message = "한줄 소개는 최대 60글자 입니다.")
	private String introduce;

	@NotNull(message = "출생년도는 필수 값 입니다.")
	private int age;

	@NotNull(message = "성별은 필수 값 입니다.")
	private Gender gender;

	@NotNull(message = "기숙사 정보는 필수 값 입니다.")
	private Dormitory dormitory;

	@ValidLifeStyle
	private Map<String, List<Long>> lifeStyle;   // 선택한 LifeStyle   : String    = Category

	@ValidPreference
	private Map<String, List<Long>> preference;  // 선택한 Preference  : List<Long>= Option_Id
}
