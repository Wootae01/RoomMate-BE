package hello.roommate.recommendation.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationUpdateDto {
	private String member1Id;
	private String member2Id;
	private double score;
}
