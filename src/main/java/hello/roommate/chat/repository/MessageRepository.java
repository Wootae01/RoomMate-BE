package hello.roommate.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.roommate.chat.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	//해당 채팅방의 모든 메시지 찾아서 반환
	@Query("select m from Message m where m.chatRoom.id = :id")
	List<Message> findAllByChatRoomId(@Param("id") Long chatRoomId);

	//가장 최근 메시지 찾아서 반환
	Optional<Message> findFirstByChatRoomIdOrderBySendTimeDesc(Long chatRoomId);
}
