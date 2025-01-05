package hello.roommate.chat.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hello.roommate.member.domain.MemberChatRoom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CHAT_ROOM", uniqueConstraints = @UniqueConstraint(columnNames = {"member1_id", "member2_id"}))
@Getter
@Setter
public class ChatRoom {
	@Id
	@GeneratedValue
	@Column(name = "chat_room_id")
	private Long id;

	@OneToMany(mappedBy = "chatRoom")
	private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private LocalDateTime updatedTime;
}
