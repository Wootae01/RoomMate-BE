package hello.roommate.profile.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private Long id;
    private String introduce;
    private String img;
}
