package hello.roommate.chat.repository;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;

@DataJpaTest
class MessageRepositoryTest {

	@Autowired
	MessageRepository messageRepository;
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ChatRoomRepository chatRoomRepository;

	@Test
	void findFirstByChatRoomIdOrderBySendTimeDesc() throws InterruptedException {
		Member memberA = new Member();
		Member memberB = new Member();
		memberA.setId(1L);
		memberB.setId(2L);
		Member saveA = memberRepository.save(memberA);
		Member saveB = memberRepository.save(memberB);
		ChatRoom chatRoom = new ChatRoom();
		chatRoom = chatRoomRepository.save(chatRoom);

		Message message1 = new Message();
		message1.setSender(saveA);
		message1.setSendTime(LocalDateTime.now());
		message1.setChatRoom(chatRoom);
		message1.setContent("1");

		Thread.sleep(1000);
		Message message2 = new Message();
		message1.setSender(saveB);
		message1.setSendTime(LocalDateTime.now());
		message1.setChatRoom(chatRoom);
		message1.setContent("2");
		messageRepository.save(message1);
		messageRepository.save(message2);

		Message latest = messageRepository.findFirstByChatRoomIdOrderBySendTimeDesc(chatRoom.getId())
			.get();
		Assertions.assertThat(latest.getContent()).isEqualTo("2");

	}
}