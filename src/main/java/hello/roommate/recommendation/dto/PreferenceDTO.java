package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PreferenceDTO {
	private Long memberId;
	private Map<String, List<Long>> options; 	// Category : Option_value
}
