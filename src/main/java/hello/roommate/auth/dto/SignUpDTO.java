package hello.roommate.auth.dto;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//JSON으로 프론트에서 회원가입폼으로 입력한 값들 넘겨줌
@Getter
@Setter
public class SignUpDTO {
    private Long userId;
    private String nickname;
    private Gender gender;
    private Dormitory dormitory;
    private int age;
    private List<Long> lifeStyle;   // 선택한 LifeStyle의 option_id(Long) 리스트
    private List<Long> preference;  // 선택한 Preference의 option_id(Long) 리스트
}
