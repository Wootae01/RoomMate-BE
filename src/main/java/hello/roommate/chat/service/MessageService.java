package hello.roommate.chat.service;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    //해당 채팅방의 가장 최근 메시지 찾아서 반환
    public Message findLatestMessage(Long chatRoomId) {
        return  messageRepository.findFirstByChatRoomIdOrderBySendTimeDesc(chatRoomId);
    }

}
