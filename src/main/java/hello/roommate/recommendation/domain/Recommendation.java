package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"MEMBER_ID_1", "MEMBER_ID_2"}))
public class Recommendation {
    @Id @GeneratedValue
    @Column(name = "RECOMMENDATION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID_1")
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID_2")
    private Member member2;

    private double score;
}
