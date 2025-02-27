package hello.roommate.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.auth.dto.EditMemberDTO;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.auth.service.SignUpService;
import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import hello.roommate.recommendation.dto.PreferenceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
	private final MemberService memberService;
	private final MessageService messageService;
	private final SignUpService signUpService;
	private final MemberRepository memberRepository;

	/**
	 * 상대방 아이디를 넘겨받아 상대 프로필 정보 조회 후 해당 정보 반환
	 *
	 * @param friendId 상대방 Id
	 * @return 상대방 profile, lifestyle, preference
	 */

	@GetMapping("/{friendId}/information")
	public ResponseEntity<SignUpDTO> getFriendInformation(@PathVariable Long friendId) {

		Member friend = memberService.findById(friendId);    // profile만 반환

		List<LifeStyle> lifeStyleList = friend.getLifeStyle();
		Map<String, List<Long>> lifestylemap = memberService.convertLifeStyleListToMap(lifeStyleList);

		List<Preference> preferenceList = friend.getPreference();
		Map<String, List<Long>> preferencemap = memberService.convertPreferenceListToMap(preferenceList);

		SignUpDTO friendData = new SignUpDTO(
			friend.getId(), friend.getNickname(), friend.getIntroduce(), friend.getAge(),
			friend.getGender(), friend.getDorm(),
			lifestylemap, preferencemap
		);

		return ResponseEntity.ok(friendData); //ResponseEntity.ok(SignUpDTO);
	}

	/**
	 * 내 아이디를 넘겨받아 내 프로필 정보 조회 후 해당 정보 반환
	 *
	 * @param memberId 사용자 Id
	 * @return 나의 basic_profile, lifestyle, preference
	 */

	// 1) My profile 반환
	@GetMapping("/{memberId}/basic")
	public ResponseEntity<EditMemberDTO> getProfile(@PathVariable Long memberId) {

		Member member = memberService.findById(memberId);

		EditMemberDTO memberDTO = new EditMemberDTO(memberId, member.getNickname(),
			member.getIntroduce(), member.getAge(), member.getDorm(), member.getGender());

		return ResponseEntity.ok(memberDTO);
	}

	// 2) My Lifestyle 반환  : Map<String, List<Long>>으로
	@GetMapping("/{memberId}/lifestyle")
	public ResponseEntity<LifeStyleDTO> getLifeStyle(@PathVariable Long memberId) {

		Member member = memberService.findById(memberId);

		Map<String, List<Long>> memberLifeStyleList =
			memberService.convertLifeStyleListToMap(member.getLifeStyle());

		LifeStyleDTO myLifeStyleDTO = new LifeStyleDTO(
			memberId, memberLifeStyleList);

		return ResponseEntity.ok(myLifeStyleDTO);
	}

	// 3) My Preference 반환 : Map<String, List<Long>>으로
	@GetMapping("/{memberId}/preference")
	public ResponseEntity<PreferenceDTO> getPreference(@PathVariable Long memberId) {

		Member member = memberService.findById(memberId);

		Map<String, List<Long>> memberPreferenceList =
			memberService.convertPreferenceListToMap(member.getPreference());

		PreferenceDTO myPreferenceDTO = new PreferenceDTO(
			memberId, memberPreferenceList);

		return ResponseEntity.ok(myPreferenceDTO);
	}

	/**
	 * 내 정보 수정 후 성공여부 반환
	 *
	 * @param memberId 사용자 Id
	 * @return 성공여부 {JSON} "success":"true"
	 */

	// 프론트에서 내정보 1단계만 수정할 시(Member basic만 수정시)
	@PostMapping("/{memberId}/editprofile")
	public ResponseEntity<Map<String, Object>> editProfile(@RequestBody EditMemberDTO member,
		@PathVariable Long memberId) {

		signUpService.editMember(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 LifeStyle만 수정할 시
	@PostMapping("/{memberId}/editlifestyle")
	public ResponseEntity<Map<String, Object>> editLifeStyle(@RequestBody LifeStyleDTO member,
		@PathVariable Long memberId) {

		// Member LifeStyle만 수정할 경우
		signUpService.editLifeStyle(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 Preference만 수정할 시
	@PostMapping("/{memberId}/editpreference")
	public ResponseEntity<Map<String, Object>> editPreference(@RequestBody PreferenceDTO member,
		@PathVariable Long memberId) {

		// Member Preference만 수정할 경우
		signUpService.editPreference(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	/**
	 * 나의 모든 채팅방을 찾아 반환
	 *
	 * @param memberId 사용자 Id
	 * @return 모든 채팅방
	 */
	@GetMapping("/{memberId}/chatrooms")
	public List<ChatRoomDTO> findAllChatRooms(@PathVariable Long memberId) {
		List<ChatRoomDTO> result = new ArrayList<>();
		List<ChatRoom> chatRooms = memberService.findAllChatRooms(memberId);

		for (ChatRoom chatRoom : chatRooms) {
			List<MemberChatRoom> memberChatRooms = chatRoom.getMemberChatRooms();

			for (MemberChatRoom memberChatRoom : memberChatRooms) {
				if (memberChatRoom.getMember().getId() != memberId) { // 채팅방 중 내가 아닌 상대방 닉네임 찾고
					Member opponent = memberChatRoom.getMember();
					String nickname = opponent.getNickname();
					Message latestMessage = messageService.findLatestMessage(chatRoom.getId()); //최근 메시지 찾고

					ChatRoomDTO dto = new ChatRoomDTO(); //dto로 변환
					dto.setChatRoomId(chatRoom.getId());
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
	public List<RecommendMemberDTO> recommendMembers(@PathVariable Long memberId) {
		log.info("추천 목록 반환 요청, id={}", memberId);
		List<Member> members = memberService.recommendMembers(memberId);

		//dto로 변환
		List<RecommendMemberDTO> dtoList = members.stream()
			.map(member -> memberService.convertToDTO(member))
			.collect(Collectors.toList());
		log.info("{}", dtoList);
		return dtoList;
	}

	/**
	 * 필터 적용하여 추천목록 반환
	 *
	 * @param memberId 사용자 id
	 * @param filterCond 사용자가 적용한 필터 항목들
	 * @return 필터 적용된 추천목록 멤버 반환
	 */
	@PostMapping("/{memberId}/recommendation")
	public List<RecommendMemberDTO> searchMembers(@PathVariable Long memberId, @RequestBody FilterCond filterCond) {
		List<Member> members = memberService.searchMembers(memberId, filterCond);
		List<RecommendMemberDTO> dtoList = members.stream()
			.map(member -> memberService.convertToDTO(member))
			.collect(Collectors.toList());

		return dtoList;
	}
}
