package hello.roommate.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.domain.Notification;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@GeneratedValue
	private Long id;

	private String username;

	private String nickname;
	private String introduce;
	private String img;
	private int age;

	@Enumerated(EnumType.STRING)
	private Dormitory dorm;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<LifeStyle> lifeStyle;

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Preference> preference = new ArrayList<>();

	@OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Notification notification;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Message> messages;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private LocalDateTime timestamp;

	public Member(Long id, String nickname, String introduce, String img, int age, Dormitory dorm, Gender gender) {
		this.id = id;
		this.nickname = nickname;
		this.introduce = introduce;
		this.img = img;
		this.age = age;
		this.dorm = dorm;
		this.gender = gender;
	}
}
