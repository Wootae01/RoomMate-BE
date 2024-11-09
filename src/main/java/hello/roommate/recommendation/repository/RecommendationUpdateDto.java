package hello.roommate.recommendation.repository;

import hello.roommate.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.StringConcatException;

@Getter
@Setter
public class RecommendationUpdateDto {
    private String memberId;
    private String matchedMemberId;

    private double score;
}
