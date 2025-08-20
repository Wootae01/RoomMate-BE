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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//JSON으로 프론트에서 회원가입폼으로 입력한 값들 넘겨줌
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
	@NotNull(message = "{NotNull.userId}")
	private Long userId;

	@NotBlank(message = "{NotBlank.nickname}")
	@Size(max = 10, message = "{Size.nickname}")
	private String nickname;

	@Size(max = 60, message = "{Size.introduce}")
	private String introduce;

	@NotNull(message = "{NotNull.age}")
	private int age;

	@NotNull(message = "{NotNull.gender}")
	private Gender gender;

	@NotNull(message = "{NotNull.dormitory}")
	private Dormitory dormitory;

	@ValidLifeStyle
	private Map<String, List<Long>> lifeStyle;   // 선택한 LifeStyle   : String    = Category

	@ValidPreference
	private Map<String, List<Long>> preference;  // 선택한 Preference  : List<Long>= Option_Id
}
