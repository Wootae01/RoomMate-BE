package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LifeStyleDTO {
	private Long memberId;
	private Map<String, List<Long>> options; // Category : Option_value
}
