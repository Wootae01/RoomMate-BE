package hello.roommate.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Member {
	@Id
	@Column(name = "MEMBER_ID")
	private String id;

	private String nickname;
	private String introduce;
	private String img;
	private int age;

	@Enumerated(EnumType.STRING)
	private Dormitory dorm;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@OneToMany(mappedBy = "member")
	private List<LifeStyle> lifeStyle;

	@OneToMany(mappedBy = "member")
	private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Preference> preference = new ArrayList<>();

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private LocalDateTime timestamp;

	public Member(String id, String nickname, String introduce, String img, int age, Dormitory dorm, Gender gender) {
		this.id = id;
		this.nickname = nickname;
		this.introduce = introduce;
		this.img = img;
		this.age = age;
		this.dorm = dorm;
		this.gender = gender;
	}
}
