package hello.roommate.recommendation.domain;

import hello.roommate.recommendation.domain.enums.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Option {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Category category;
	private String optionValue;

	public Option(Category category, String optionValue) {
		this.category = category;
		this.optionValue = optionValue;
	}
}
