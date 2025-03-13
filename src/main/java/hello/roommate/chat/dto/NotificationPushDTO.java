package hello.roommate.chat.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 푸시 알림 요청 시 사용하는 DTO 클래스
 * 알림 받을 대상의 토큰 정보, 알림이 울릴 소리, 알림 제목, 알림 내용, 추가 데이터를 포함한다.
 */
@Getter
@Setter
public class NotificationPushDTO {
	private String to;        //알림 받을 대상의 토큰 정보
	private String sound;      //알림 소리 정보 (default, null)
	private String title;      //알림 제목
	private String body;      //알림 내용
	private Map<String, Object> data;    //알림과 함께 전달할 추가 데이터
}
