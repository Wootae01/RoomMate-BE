package hello.roommate.chat.domain;

import hello.roommate.member.domain.MemberChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
