package hello.roommate.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.auth.dto.EditMemberDTO;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.auth.exception.MissingTokenException;
import hello.roommate.auth.service.RefreshEntityService;
import hello.roommate.auth.service.SignUpService;
import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.chat.domain.Message;
import hello.roommate.chat.dto.ChatRoomDTO;
import hello.roommate.chat.service.MessageService;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import hello.roommate.recommendation.dto.PreferenceDTO;
import jakarta.servlet.http.HttpServletRequest;
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
	private final RefreshEntityService refreshService;

	/**
	 * 회원 탈퇴 처리
	 *
	 * @param memberId memberId
	 * @return 성공여부 {JSON} : "success":"true"
	 */
	@DeleteMapping("/{memberId}/resign")
	public ResponseEntity<Map<String, Object>> reSign(HttpServletRequest request,
		@Validated @PathVariable Long memberId) {

		//refresh 토큰 검증
		String header = request.getHeader("Authorization");
		log.info("header={}", header);
		if (header == null || !header.startsWith("Bearer ")) {
			throw new MissingTokenException();
		}

		String[] split = header.split(" ");
		String refresh = split[1];
		refreshService.validateRefresh(refresh);

		memberService.delete(memberId);
		refreshService.deleteByRefresh(refresh);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

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

		//상관 없음 항목 제외한 preferenceMap
		Map<String, List<Long>> preferencemap = friend.getPreference().stream()
			.filter(preference -> preference.getOption().getId() > 100)
			.map(Preference::getOption)
			.collect(Collectors.groupingBy(
				option -> option.getCategory().name(),
				Collectors.mapping(Option::getId, Collectors.toList())
			));

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

		LifeStyleDTO myLifeStyleDTO = new LifeStyleDTO(memberLifeStyleList);

		return ResponseEntity.ok(myLifeStyleDTO);
	}

	// 3) My Preference 반환 : Map<String, List<Long>>으로
	@GetMapping("/{memberId}/preference")
	public ResponseEntity<PreferenceDTO> getPreference(@PathVariable Long memberId) {

		Member member = memberService.findById(memberId);

		Map<String, List<Long>> memberPreferenceList =
			memberService.convertPreferenceListToMap(member.getPreference());

		PreferenceDTO myPreferenceDTO = new PreferenceDTO(memberPreferenceList);

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
	public ResponseEntity<Map<String, Object>> editProfile(@Validated @RequestBody EditMemberDTO member,
		@PathVariable Long memberId) {

		signUpService.editMember(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 LifeStyle만 수정할 시
	@PostMapping("/{memberId}/editlifestyle")
	public ResponseEntity<Map<String, Object>> editLifeStyle(@Validated @RequestBody LifeStyleDTO member,
		@PathVariable Long memberId) {

		log.info("LifeStlye 수정 요청 data={}", member);

		// Member LifeStyle만 수정할 경우
		signUpService.editLifeStyle(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	// 프론트에서 Preference만 수정할 시
	@PostMapping("/{memberId}/editpreference")
	public ResponseEntity<Map<String, Object>> editPreference(@Validated @RequestBody PreferenceDTO member,
		@PathVariable Long memberId) {

		log.info("Preference 수정 요청 data={}", member);
		// Member Preference만 수정할 경우
		signUpService.editPreference(member, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);

		return ResponseEntity.ok(response);
	}

	/**
	 * 사용 가능한 닉네임인지 확인하여 사용 가능하면 true, 불가능 false 반환한다.
	 *
	 * @param nickname
	 * @return 사용 불가 : false, 사용 가능 true 반환
	 */
	@GetMapping("/validate-nickname")
	public Boolean validateDuplicatedNickname(@RequestParam String nickname) {
		Optional<Member> optional = memberService.findByNickname(nickname);

		if (optional.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 사용자 닉네임 반환
	 * @param memberId 사용자 id
	 * @return 닉네임
	 */
	@GetMapping("/{memberId}/nickname")
	public String getNickName(@PathVariable Long memberId) {
		Member member = memberService.findById(memberId);
		String nickname = member.getNickname();
		return nickname;
	}

	/**
	 * 나의 모든 채팅방을 찾아 반환
	 *
	 * @param memberId 사용자 Id
	 * @return 모든 채팅방
	 */
	@GetMapping("/{memberId}/chatrooms")
	public List<ChatRoomDTO> findAllChatRooms(@PathVariable Long memberId) {
		log.info("모든 채팅방 반환 요청 id={}", memberId);
		List<ChatRoomDTO> result = new ArrayList<>();
		List<ChatRoom> chatRooms = memberService.findAllChatRooms(memberId);
		for (ChatRoom chatRoom : chatRooms) {
			List<MemberChatRoom> memberChatRooms = chatRoom.getMemberChatRooms();

			for (MemberChatRoom memberChatRoom : memberChatRooms) {
				if (!memberChatRoom.getMember().getId().equals(memberId)) { // 채팅방 중 내가 아닌 상대방 닉네임 찾고
					Member opponent = memberChatRoom.getMember();
					String nickname = opponent.getNickname();
					Optional<Message> optional = messageService.findLatestMessage(chatRoom.getId()); //최근 메시지 찾고
					if (optional.isEmpty()) {
						continue;
					}
					Message latestMessage = optional.get();
					ChatRoomDTO dto = new ChatRoomDTO(); //dto로 변환
					dto.setChatRoomId(chatRoom.getId());
					dto.setNickname(nickname);
					dto.setUpdatedTime(latestMessage.getSendTime());
					dto.setMessage(latestMessage.getContent());
					result.add(dto);
				}
			}
		}
		log.info("반환 데이터 = {}", result);
		result.sort(((o1, o2) -> o2.getUpdatedTime().compareTo(o1.getUpdatedTime())));
		return result;
	}
}
