package hello.roommate.chat.service;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.dto.CreateChatRoomDTO;
import hello.roommate.chat.repository.ChatRoomRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.repository.MemberChatRoomRepository;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    //특정 채팅방 찾기
    public ChatRoom findRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow();
    }

    //새로운 채팅방 만듬, 이미 존재하면 기존 채팅방 반환
    public ChatRoom createChatRoom(CreateChatRoomDTO dto) {

        ///이미 있으면 기존 채팅방 반환
        Optional<ChatRoom> existChatRoom = memberChatRoomRepository
                .findExistingChatRoomByMembersId(dto.getMember1Id(), dto.getMember2Id());
        if (existChatRoom.isPresent()) {
            return existChatRoom.get();
        }

        //채팅방 없으면 새로 만듬
        Member member1 = memberRepository.findById(dto.getMember1Id())
                .orElseThrow(() -> new NoSuchElementException("id dose not exist"));
        Member member2 = memberRepository.findById(dto.getMember2Id())
                .orElseThrow(() -> new NoSuchElementException("id dose not exist"));

        MemberChatRoom memberChatRoom1 = new MemberChatRoom();
        MemberChatRoom memberChatRoom2 = new MemberChatRoom();
        memberChatRoom1.setMember(member1);
        memberChatRoom2.setMember(member2);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.addMemberChatRooms(memberChatRoom1);
        chatRoom.addMemberChatRooms(memberChatRoom2);
        return chatRoomRepository.save(chatRoom);
    }

}
