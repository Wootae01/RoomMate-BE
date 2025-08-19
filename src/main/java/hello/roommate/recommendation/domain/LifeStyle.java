package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@ToString
@Table(name = "lifestyle")
public class LifeStyle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lifestyle_id")
	@Setter(value = AccessLevel.NONE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id")
	private Option option;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public LifeStyle(Member member, Option option) {
		this.option = option;
		this.member = member;
	}

	public LifeStyle(Member member) {
		this.member = member;
	}
}
