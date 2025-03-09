package hello.roommate.recommendation.dto;

import java.util.List;
import java.util.Map;

import hello.roommate.validator.ValidLifeStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LifeStyleDTO {
	@ValidLifeStyle
	private Map<String, List<Long>> options; // Category : Option_value
}
