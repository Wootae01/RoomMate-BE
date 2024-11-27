package hello.roommate.recommendation.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationDto {
    private String nickname;
    private int age;
    private double score;
}
