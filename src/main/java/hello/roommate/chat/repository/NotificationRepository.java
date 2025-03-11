package hello.roommate.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.chat.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Optional<Notification> findByMemberId(Long memberId);
}
