package hello.roommate.chat.service;

import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.config.RedisTestConfig;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberChatRoomRepository;
import hello.roommate.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Testcontainers
@Import(RedisTestConfig.class)
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private MemberChatRoomRepository memberChatRoomRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Long member1Id;
    private Long member2Id;

    @BeforeEach
    void setUp() {
        Member member1 = new Member();
        member1.setUsername("testUser1");
        member1Id = memberRepository.save(member1).getId();

        Member member2 = new Member();
        member2.setUsername("testUser2");
        member2Id = memberRepository.save(member2).getId();
    }

    @Test
    @DisplayName("동시에 채팅방 생성 요청시 하나만 생성된다.")
    void createChatRoom() throws InterruptedException {
        CreateChatRoomDTO dto = new CreateChatRoomDTO();
        dto.setMember1Id(member1Id);
        dto.setMember2Id(member2Id);

        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    chatRoomService.createChatRoom(dto);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long roomCount = memberChatRoomRepository.findExistingChatRoomByMembersId(member1Id, member2Id)
                .stream().count();

        Assertions.assertThat(roomCount).isEqualTo(1);
    }
}
