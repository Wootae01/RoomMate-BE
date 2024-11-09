package hello.roommate.member.domain;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.profile.domain.Profile;
import hello.roommate.recommendation.domain.LifeStyle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Member {
    private String id;
    private Profile profile;
    private LifeStyle lifeStyle;
    private String password;
    private String email;

    Dormitory dorm;
}
