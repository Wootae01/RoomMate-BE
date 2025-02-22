package hello.roommate.member.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.dto.MemberDTO;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.dto.OptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
	private final MemberService memberSevice;
	private final MessageService messageService;

	/*
	 * 나의 모든 채팅방 반환
	 * 1. 나의 채팅방 찾음
	 * 2. 그 채팅방의 상대 닉네임 찾고
	 * 3. 해당 채팅방의 최근 대화 날짜, 대화 내역 찾아서
	 * 4. dto로 변환
	 * */
	@GetMapping("/{memberId}/chatrooms")
	public List<ChatRoomDTO> findAllChatRooms(@PathVariable Long memberId) {
		List<ChatRoomDTO> result = new ArrayList<>();
		List<ChatRoom> chatRooms = memberSevice.findAllChatRooms(memberId);

		for (ChatRoom chatRoom : chatRooms) {
			List<MemberChatRoom> memberChatRooms = chatRoom.getMemberChatRooms();

			for (MemberChatRoom memberChatRoom : memberChatRooms) {
				if (memberChatRoom.getMember().getId() != memberId) { // 채팅방 중 내가 아닌 상대방 닉네임 찾고
					Member opponent = memberChatRoom.getMember();
					String nickname = opponent.getNickname();
					Message latestMessage = messageService.findLatestMessage(chatRoom.getId()); //최근 메시지 찾고

					ChatRoomDTO dto = new ChatRoomDTO(); //dto로 변환
					dto.setId(chatRoom.getId());
					dto.setNickname(nickname);
					dto.setUpdatedTime(latestMessage.getSendTime());
					dto.setMessage(latestMessage.getContent());
					result.add(dto);
				}
			}
		}

		return result;
	}

	/**
	 * 기본 추천 목록 반환
	 *
	 * @param memberId 사용자 id
	 * @return 추천 목록 멤버 반환
	 */
	@GetMapping("/{memberId}/recommendation")
	public List<MemberDTO> recommendMembers(@PathVariable Long memberId) {
		log.info("추천 목록 반환 요청, id={}", memberId);
		List<Member> members = memberSevice.recommendMembers(memberId);

		//dto로 변환
		List<MemberDTO> dtoList = members.stream()
			.map(member -> memberSevice.convertToDTO(member))
			.collect(Collectors.toList());
		log.info("{}", dtoList.toString());
		return dtoList;
	}

	/**
	 * 필터 적용하여 추천목록 반환
	 *
	 * @param memberId 사용자 id
	 * @param optionDto 사용자가 적용한 필터 항목들
	 * @return 필터 적용된 추천목록 멤버 반환
	 */
	@PostMapping("/{memberId}/recommendation")
	public List<MemberDTO> searchMembers(@PathVariable Long memberId, @RequestBody List<OptionDTO> optionDto) {
		List<Member> members = memberSevice.searchMembers(memberId, optionDto);
		List<MemberDTO> dtoList = members.stream()
			.map(member -> memberSevice.convertToDTO(member))
			.collect(Collectors.toList());

		return dtoList;
	}
}
