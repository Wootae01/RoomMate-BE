package hello.roommate.chat.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.dto.NotificationSaveDTO;
import hello.roommate.chat.repository.NotificationRepository;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberRepository memberRepository;

	public void saveOrUpdate(NotificationSaveDTO dto) {

		Optional<Notification> optional = notificationRepository.findByMemberId(dto.getMemberId());
		if (optional.isEmpty()) {
			Member member = memberRepository.findById(dto.getMemberId())
				.orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));

			Notification notification = new Notification();
			notification.setMember(member);
			notification.setToken(dto.getToken());
			notificationRepository.save(notification);
		} else {
			Notification notification = optional.get();
			notification.setToken(dto.getToken());
		}
	}

	public Optional<Notification> findByMemberId(Long memberId) {
		return notificationRepository.findByMemberId(memberId);

	}
}
