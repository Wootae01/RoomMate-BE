package hello.roommate.chat.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

	private String token;

	private Boolean permission;
}
