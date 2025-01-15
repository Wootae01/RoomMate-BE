package hello.roommate.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {
	@Id
	@Column(name = "MEMBER_ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "LIFESTYLE_ID")
	private LifeStyle lifeStyle;

	@OneToMany(mappedBy = "member")
	private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Preference> preference = new ArrayList<>();

	private String nickname;
	private String introduce;
	private String img;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private LocalDateTime timestamp;

	@Enumerated(EnumType.STRING)
	private Dormitory dorm;
}
