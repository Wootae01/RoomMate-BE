package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Recommendation {
    private Long id;
    private Member member;
    private Member matchedMember;
    private double score;
}
