package hello.roommate.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.dto.NotificationDTO;
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
	public ResponseEntity<Void> saveOrUpdateToken(@RequestBody @Validated NotificationDTO dto) {
		log.info("알림 토큰 저장 요청");
		notificationService.saveOrUpdate(dto);
		log.info("알림 토큰 저장 성공");
		return ResponseEntity.ok().build();
	}
}
