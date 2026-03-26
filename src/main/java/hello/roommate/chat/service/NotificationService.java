package hello.roommate.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.dto.NotificationPermitDTO;
import hello.roommate.chat.dto.NotificationSaveDTO;
import hello.roommate.chat.repository.NotificationRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.redis.CacheTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final CacheTemplate cacheTemplate;

    public void saveOrUpdate(NotificationSaveDTO dto) {

        Optional<Notification> optional = notificationRepository.findByMemberId(dto.getMemberId());
        if (optional.isEmpty()) {
            Member member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));

            Notification notification = new Notification();
            notification.setMember(member);
            notification.setToken(dto.getToken());
            notification.setPermission(true);
            notificationRepository.save(notification);
        } else {
            Notification notification = optional.get();
            notification.setToken(dto.getToken());
        }
        String cacheKey = "notification:" + dto.getMemberId();
        cacheTemplate.evict(cacheKey);
    }

    public Optional<Notification> findByMemberId(Long memberId) {
        String key = "notification:" + memberId;
        Notification result = cacheTemplate.execute(key, Duration.ofHours(1), new TypeReference<Notification>() {
                },
                () -> notificationRepository.findByMemberId(memberId).orElse(null));

        return Optional.ofNullable(result);
    }

    public void updatePermission(Long memberId, NotificationPermitDTO dto) {
        Notification notification = notificationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("알림 허용을 하지 않은 사용자 입니다."));

        notification.setPermission(dto.getPermission());
        String cacheKey = "notification:" + memberId;
        cacheTemplate.evict(cacheKey);
    }
}
