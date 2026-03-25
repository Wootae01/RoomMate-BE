package hello.roommate.chat.service;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.repository.MemberChatRoomRepository;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.redis.DistributedLockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 채팅방 생성, 조회 하는 서비스 클래스
 *
 * @author Wootae
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final DistributedLockTemplate lockTemplate;

    /**
     * 채팅방 id로 특정 채팅방 찾아서 반환
     *
     * @param chatRoomId 채팅방 id
     * @return ChatRoom
     */
    public ChatRoom findRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NoSuchElementException("ChatRoom not found"));
    }

    /**
     * 채팅방 생성 하는 메서드.
     * 기존 채팅방이 있으면 그 채팅방 반환, 없으면 새로운 채팅방 생성 후 반환
     *
     * @param dto 채팅방 생성에 필요한 dto
     * @return ChatRoom
     */
    public ChatRoom createChatRoom(CreateChatRoomDTO dto) {

        if (dto.getMember1Id().equals(dto.getMember2Id())) {
            throw new IllegalArgumentException("자기 자신과 채팅방을 만들 수 없습니다.");
        }

        ///이미 있으면 기존 채팅방 반환
        Optional<ChatRoom> existChatRoom = memberChatRoomRepository
                .findExistingChatRoomByMembersId(dto.getMember1Id(), dto.getMember2Id());
        if (existChatRoom.isPresent()) {
            return existChatRoom.get();
        }

        //채팅방 없으면 새로 만듬
        long minId = Math.min(dto.getMember1Id(), dto.getMember2Id());
        long maxId = Math.max(dto.getMember1Id(), dto.getMember2Id());
        String key = "chat:room:member:" + minId + ":" + maxId;

        try {
            return lockTemplate.executeWithLock(key, 300, () ->
                    // double check
                    memberChatRoomRepository.findExistingChatRoomByMembersId(dto.getMember1Id(), dto.getMember2Id())
                            .orElseGet(() -> {

                                // 없는 경우 채팅방 생성
                                Member member1 = memberRepository.findById(dto.getMember1Id())
                                        .orElseThrow(() -> new NoSuchElementException("member id does not exist"));
                                Member member2 = memberRepository.findById(dto.getMember2Id())
                                        .orElseThrow(() -> new NoSuchElementException("member id does not exist"));

                                MemberChatRoom memberChatRoom1 = new MemberChatRoom();
                                MemberChatRoom memberChatRoom2 = new MemberChatRoom();
                                memberChatRoom1.setMember(member1);
                                memberChatRoom2.setMember(member2);

                                ChatRoom chatRoom = new ChatRoom();
                                chatRoom.addMemberChatRooms(memberChatRoom1);
                                chatRoom.addMemberChatRooms(memberChatRoom2);
                                return chatRoomRepository.save(chatRoom);
                            })
            );
        } catch (IllegalStateException e) {
            log.info("채팅방 생성 실패. memberId1={}, memberId2={}, errorMessage={}", dto.getMember1Id(), dto.getMember2Id(), e.getMessage());
            throw e;
        }
    }
}
