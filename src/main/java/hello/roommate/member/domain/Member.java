package hello.roommate.member.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.domain.Notification;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
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
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	@Column(length = 10)
	private String nickname;
	private String introduce;
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


	public Member( String nickname, String introduce,int age, Dormitory dorm, Gender gender) {
		this.nickname = nickname;
		this.introduce = introduce;
		this.age = age;
		this.dorm = dorm;
		this.gender = gender;
	}
}
