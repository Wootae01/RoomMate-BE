package hello.roommate.chat.controller;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.MessageDTO;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.chat.service.ChatService;
import hello.roommate.chat.service.MemberChatRoomService;
import hello.roommate.chat.service.MessageService;
import hello.roommate.chat.service.PushNotificationService;
import hello.roommate.mapper.MessageMapper;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final MessageService messageService;
    private final MemberChatRoomService memberChatRoomService;
    private final PushNotificationService pushNotificationService;
    private final MessageMapper messageMapper;
    private final MemberService memberService;
    private final ChatService chatService;

    /**
     * 웹소켓을 통해 메시지를 받아 처리하고, 저장 후 동일한 메시지를 구독자에게 전송하는 메서드
     *
     * @param messageReceiveDTO 전송할 메시지 정보 담고 있는 DTO 객체
     * @return 저장 후 클라이언트에게 전송할 메시지 DTO 객체
     */
    @MessageMapping("/{roomId}")
    @SendTo("/topic/chatroom/{roomId}")
    public Mono<MessageDTO> sendMessage(MessageReceiveDTO messageReceiveDTO, Principal principal) {
        log.info("arrive message");
        log.info("message ={}", messageReceiveDTO);
        Message message = messageMapper.convertToEntity(messageReceiveDTO);
        message = messageService.save(message);

        Member sender = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자입니다."));

        //해당 채팅 방의 상대 찾기
        Member opponent = memberChatRoomService.findByMemberExceptMyId(messageReceiveDTO.getChatRoomId(),
                sender.getId());

        // 채팅방 목록 내용 실시간 업데이트
        chatService.sendChatListUpdate(sender, opponent, messageReceiveDTO);

        //메시지 전송 DTO 생성
        MessageDTO sendDTO = messageMapper.convertToDTO(message);

        //알림 전송 후 메시지 내용 전달
        return pushNotificationService.sendNotificationIfAllowed(messageReceiveDTO, opponent.getId())
                .thenReturn(sendDTO);
    }

}
