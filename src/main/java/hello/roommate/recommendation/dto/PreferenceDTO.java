package hello.roommate.recommendation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceDTO {
	private Long id;
	private List<Long> options;

}
