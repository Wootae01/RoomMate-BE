package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LifeStyleDTO {
	private Map<String, List<Long>> options; // Category : Option_value
}
