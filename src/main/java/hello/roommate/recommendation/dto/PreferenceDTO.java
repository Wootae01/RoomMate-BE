package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreferenceDTO {
	private Map<String, List<Long>> options; 	// Category : Option_value
}
