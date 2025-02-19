package hello.roommate.member.domain;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private Long id;

    private String nickname;
    private String introduce;
    private String img;
    private int age;

    @Enumerated(EnumType.STRING)
    private Dormitory dorm;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "member")
    private List<LifeStyle> lifeStyle;

    @OneToMany(mappedBy = "member")
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Preference> preference = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime timestamp;

    public Member(Long id, String nickname, String introduce, String img, int age, Dormitory dorm, Gender gender) {
        this.id = id;
        this.nickname = nickname;
        this.introduce = introduce;
        this.img = img;
        this.age = age;
        this.dorm = dorm;
        this.gender = gender;
    }
}
