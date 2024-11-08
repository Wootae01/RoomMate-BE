package hello.roommate.profile.domain;

import hello.roommate.recommendation.domain.LifeStyle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Profile {
    private Long id;

    private LifeStyle lifeStyle;
    private String nickname; //닉네임
    private String introduce; //자기 소개
    private String img; //프로필 이미지 url?
}
