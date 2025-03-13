package hello.roommate.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {

	//이미 존재하는 채팅방인지 확인
	@Query(value = """
		select m.chatRoom from MemberChatRoom m 
		where m.member.id =:member1Id 
		and m.chatRoom.id in (select m2.chatRoom.id from MemberChatRoom m2 where m2.member.id =:member2Id)
		""")
	Optional<ChatRoom> findExistingChatRoomByMembersId(Long member1Id, Long member2Id);

	//해당 채팅방의 상대 조회
	@Query("select m.member from MemberChatRoom m where m.chatRoom.id = :chatRoomId and m.member.id <> :myId")
	Optional<Member> findByMemberExceptMyId(Long chatRoomId, Long myId);

}
