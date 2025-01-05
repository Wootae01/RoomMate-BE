package hello.roommate.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
