package hello.roommate.chat.domain;

import hello.roommate.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	private String token;

	private Boolean permission;
}
