package hello.roommate.member.controller;

import java.util.ArrayList;
import java.util.List;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.member.domain.MemberChatRoom;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.dto.OptionDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberSevice;
    private final MessageService messageService;


    /*
     * 나의 모든 채팅방 반환
     * 1. 나의 채팅방 찾음
     * 2. 그 채팅방의 상대 닉네임 찾고
     * 3. 해당 채팅방의 최근 대화 날짜, 대화 내역 찾아서
     * 4. dto로 변환
     * */
    @GetMapping("/{memberId}/chatrooms")
    public List<ChatRoomDTO> findAllChatRooms(@PathVariable Long memberId) {
        List<ChatRoomDTO> result = new ArrayList<>();
        List<ChatRoom> chatRooms = memberSevice.findAllChatRooms(memberId);

        for (ChatRoom chatRoom : chatRooms) {
            List<MemberChatRoom> memberChatRooms = chatRoom.getMemberChatRooms();

            for (MemberChatRoom memberChatRoom : memberChatRooms) {
                if (memberChatRoom.getMember().getId() != memberId) { // 채팅방 중 내가 아닌 상대방 닉네임 찾고
                    Member opponent = memberChatRoom.getMember();
                    String nickname = opponent.getNickname();
                    Message latestMessage = messageService.findLatestMessage(chatRoom.getId()); //최근 메시지 찾고

                    ChatRoomDTO dto = new ChatRoomDTO(); //dto로 변환
                    dto.setId(chatRoom.getId());
                    dto.setNickname(nickname);
                    dto.setUpdatedTime(latestMessage.getSendTime());
                    dto.setMessage(latestMessage.getContent());
                    result.add(dto);
                }
            }
        }

        return result;
    }

    @GetMapping("/{memberId}/recommendation")
    public List<Member> recommendMembers(@PathVariable Long memberId) {
        return memberSevice.recommendMembers(memberId);
    }

    @PostMapping("/{memberId}/recommendation")
    public List<Member> searchMembers(@PathVariable Long memberId, @RequestBody List<OptionDto> optionDto) {
        return memberSevice.searchMembers(memberId, optionDto);
    }
}
