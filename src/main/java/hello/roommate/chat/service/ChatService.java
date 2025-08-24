package hello.roommate.chat.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.dto.MessageReceiveDTO;
import hello.roommate.mapper.ChatRoomMapper;
import hello.roommate.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final SimpMessagingTemplate template;
	private final ChatRoomMapper chatRoomMapper;
	private final SimpUserRegistry simpUserRegistry; // 추가

	public void sendChatListUpdate(Member sender, Member opponent, MessageReceiveDTO messageReceiveDTO) {
		ChatRoomDTO chatRoomDTO = chatRoomMapper.convertReceiveToChatRoomDTO(messageReceiveDTO);

		String senderPrincipal = sender.getUsername();
		String opponentPrincipal = opponent.getUsername();

		log.info("sendChatListUpdate called: senderPrincipal={}, opponentPrincipal={}, dto={}",
			senderPrincipal, opponentPrincipal, chatRoomDTO);

		SimpUser u1 = simpUserRegistry.getUser(senderPrincipal);
		SimpUser u2 = simpUserRegistry.getUser(opponentPrincipal);

		log.info("SimpUser presence - senderConnected={}, opponentConnected={}", u1 != null, u2 != null);
		log.info("u1={}, u2={}", u1, u2);
		template.convertAndSendToUser(sender.getUsername(), "/queue/chat-list", chatRoomDTO);
		template.convertAndSendToUser(opponent.getUsername(), "/queue/chat-list", chatRoomDTO);
	}
}
