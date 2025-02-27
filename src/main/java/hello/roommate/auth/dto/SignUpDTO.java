package hello.roommate.auth.dto;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

//JSON으로 프론트에서 회원가입폼으로 입력한 값들 넘겨줌
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SignUpDTO {
    private Long userId;
    private String nickname;
    private String introduce;
    private int age;
    private Gender gender;
    private Dormitory dormitory;
    private Map<String, List<Long>> lifeStyle;   // 선택한 LifeStyle   : String    = Category
    private Map<String, List<Long>> preference;  // 선택한 Preference  : List<Long>= Option_Id
}
