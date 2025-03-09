package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import hello.roommate.validator.ValidPreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PreferenceDTO {
	@ValidPreference
	private Map<String, List<Long>> options;    // Category : Option_value
}
