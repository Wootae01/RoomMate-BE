package hello.roommate.member.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberSevice;

	@GetMapping("/{memberId}/chatrooms")
	public List<ChatRoom> findAllChatRooms(@PathVariable String memberId) {
		return memberSevice.findAllChatRooms(memberId);
	}
}
