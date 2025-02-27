package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LifeStyleDTO {
	private Map<String, List<Long>> options; // Category : Option_value
}
