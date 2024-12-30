package hello.roommate.member.domain;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LIFESTYLE_ID")
    private LifeStyle lifeStyle;

    @OneToMany(mappedBy = "id")
    List<Recommendation> recommendations = new ArrayList<>();

    private String nickname;
    private String introduce;
    private String img;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private Dormitory dorm;
}
