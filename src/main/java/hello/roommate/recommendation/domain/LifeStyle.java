package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "LIFESTYLE")
@NoArgsConstructor
@ToString
public class LifeStyle {
	@Id
	@GeneratedValue
	@Column(name = "LIFESTYLE_ID")
	@Setter(value = AccessLevel.NONE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OPTION_ID")
	private Option option;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	public LifeStyle(Member member, Option option) {
		this.option = option;
		this.member = member;
	}

	public LifeStyle(Member member) {
		this.member = member;
	}
}
