package hello.roommate.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RefreshEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_id")
	private Long id;

	private String username;
	private String refresh;
	private String role;
	private LocalDateTime expiration;
}
