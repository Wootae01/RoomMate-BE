package hello.roommate.recommendation.domain;

import hello.roommate.recommendation.domain.enums.Category;
import jakarta.persistence.*;
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
	@Column(name = "OPTION_ID")
	private Long id;

	@Enumerated(EnumType.STRING)  //enum 타입을 엔티티 클래스의 속성으로 사용위해, String : 값을 문자열로 DB에 저장
	private Category category;

	private String optionValue;

	public Option(Category category, String optionValue) {
		this.category = category;
		this.optionValue = optionValue;
	}
}
