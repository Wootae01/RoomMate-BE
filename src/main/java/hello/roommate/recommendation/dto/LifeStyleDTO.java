package hello.roommate.recommendation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeStyleDTO {
	private Long memberId;
	private List<Long> options; //option id
}
