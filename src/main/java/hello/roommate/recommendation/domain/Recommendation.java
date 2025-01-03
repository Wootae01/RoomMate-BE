package hello.roommate.recommendation.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"MEMBER_ID_1", "MEMBER_ID_2"}))
public class Recommendation {
	@Id
	@GeneratedValue
	@Column(name = "RECOMMENDATION_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MEMBER_ID_1")
	private Member member1;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "MEMBER_ID_2")
	private Member member2;

	private double score;
}
