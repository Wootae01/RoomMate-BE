package hello.roommate.chat.service;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.repository.MessageRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    //해당 채팅방의 가장 최근 메시지 찾아서 반환
    public Message findLatestMessage(Long chatRoomId) {
        return messageRepository.findFirstByChatRoomIdOrderBySendTimeDesc(chatRoomId);
    }

    //해당 채팅 방의 모든 메시지 내용 반환
    public List<Message> findAllByChatRoomId(Long chatRoomId) {
        return messageRepository.findAllByChatRoomId(chatRoomId);
    }

    //MessageDTO -> Entity로 변환
    public Message convertToEntity(MessageDTO dto) {
        Message message = new Message();
        ChatRoom chatRoom = chatRoomService.findRoomById(dto.getChatRoomId());
        Member sender = memberService.findByNickname(dto.getNickname());

        message.setContent(dto.getContent());
        message.setSendTime(dto.getSendTime());
        message.setSender(sender);
        message.setChatRoom(chatRoom);
        return message;
    }

    //Entity -> DTO로 변환
    public MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();

        dto.setChatRoomId(message.getChatRoom().getId());
        Member member = memberService.findById(message.getId());
        dto.setNickname(member.getNickname());
        dto.setContent(message.getContent());
        dto.setSendTime(message.getSendTime());

        return dto;
    }

}
