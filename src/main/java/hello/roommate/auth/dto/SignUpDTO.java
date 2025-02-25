package hello.roommate.auth.dto;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

//JSON으로 프론트에서 회원가입폼으로 입력한 값들 넘겨줌
@Getter
@Setter
@ToString
public class SignUpDTO {
    private Long userId;
    private String nickname;
    private Gender gender;
    private String introduce;
    private Dormitory dormitory;
    private int age;
    private Map<String, List<Long>> lifeStyle;   // 선택한 LifeStyle
    private Map<String, List<Long>> preference;  // 선택한 Preference
}
