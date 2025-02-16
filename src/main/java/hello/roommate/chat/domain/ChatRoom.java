package hello.roommate.chat.domain;

import java.util.ArrayList;
import java.util.List;

import hello.roommate.member.domain.MemberChatRoom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CHAT_ROOM")
@Getter
@Setter
public class ChatRoom {
	@Id
	@GeneratedValue
	@Column(name = "chat_room_id")
	private Long id;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
	private List<MemberChatRoom> memberChatRooms = new ArrayList<>();


	public void addMemberChatRooms(MemberChatRoom memberChatRoom) {
		memberChatRooms.add(memberChatRoom);
		memberChatRoom.setChatRoom(this);
	}
}
