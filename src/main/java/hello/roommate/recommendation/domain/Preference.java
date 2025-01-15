package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Preference {
	@Id
	@GeneratedValue
	@Column(name = "PREFERENCE_ID")
	@Setter(value = AccessLevel.NONE)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Category category;

	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
}
