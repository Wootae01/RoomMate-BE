package hello.roommate.chat.controller;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.Notification;
import hello.roommate.chat.dto.NotificationPermitDTO;
import hello.roommate.chat.dto.NotificationSaveDTO;
import hello.roommate.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@PostMapping("/token")
	public ResponseEntity<Void> saveOrUpdateToken(@RequestBody @Validated NotificationSaveDTO dto) {
		log.info("알림 토큰 저장 요청");
		notificationService.saveOrUpdate(dto);
		log.info("알림 토큰 저장 성공");
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{memberId}/settings")
	public NotificationPermitDTO getPermission(@PathVariable Long memberId) {
		Notification notification = notificationService.findByMemberId(memberId)
			.orElseThrow(() -> new NoSuchElementException("알림 권한이 허용되어 있지 않습니다."));

		NotificationPermitDTO dto = new NotificationPermitDTO();
		dto.setPermission(notification.getPermission());

		return dto;
	}

	@PostMapping("/{memberId}/settings")
	public ResponseEntity<Void> setNotificationsPermit(@PathVariable Long memberId,
		@RequestBody NotificationPermitDTO dto) {
		log.info("알림 권한 수정 요청");

		notificationService.updatePermission(memberId, dto);

		return ResponseEntity.ok().build();
	}
}
