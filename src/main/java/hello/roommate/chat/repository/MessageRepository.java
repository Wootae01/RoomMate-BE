package hello.roommate.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.chat.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query("select m from Message m where m.chatRoom.id = :id")
	List<Message> findAllByChatRoomId(@Param("id") Long chatRoomId);
}
