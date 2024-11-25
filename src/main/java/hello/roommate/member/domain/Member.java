package hello.roommate.member.domain;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    @OneToOne
    @JoinColumn(name = "LIFESTYLE_ID")
    private LifeStyle lifeStyle;

    @OneToMany(mappedBy = "id")
    List<Recommendation> recommendations = new ArrayList<>();

    private String nickname;
    private String introduce;
    private String img;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private Dormitory dorm;
}
