package hello.roommate.member.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Member;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.dto.OptionDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberSevice;

	@GetMapping("/{memberId}/chatrooms")
	public List<ChatRoom> findAllChatRooms(@PathVariable Long memberId) {
		return memberSevice.findAllChatRooms(memberId);
	}

	@GetMapping("/{memberId}/recommendation")
	public List<Member> recommendMembers(@PathVariable Long memberId) {
		return memberSevice.recommendMembers(memberId);
	}

	@PostMapping("/{memberId}/recommendation")
	public List<Member> searchMembers(@PathVariable Long memberId, @RequestBody List<OptionDto> optionDto) {
		return memberSevice.searchMembers(memberId, optionDto);
	}
}
