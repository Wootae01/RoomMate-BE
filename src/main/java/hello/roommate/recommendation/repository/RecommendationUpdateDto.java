package hello.roommate.recommendation.repository;

import hello.roommate.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationUpdateDto {
    private Member member;
    private Member matchedMember;

    private int score;
}
