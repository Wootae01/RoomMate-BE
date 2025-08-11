package hello.roommate.init;

import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.repository.NotificationRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationInit {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    void createNotification() {
        if(notificationRepository.count() != 0){
            return;
        }
        List<Member> members = memberRepository.findAll();
        List<Notification> notifications = new ArrayList<>();

        for (Member member : members) {
            Notification notification = new Notification();
            notification.setToken("token");
            notification.setMember(member);
            notification.setPermission(true);
            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);
    }
}
