package hello.roommate.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.dto.FilterCond;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository repository;
	private final MemberRepository memberRepository;
	private final LifeStyleRepository lifeStyleRepository;
	private final PreferenceRepository preferenceRepository;

	public Member save(Member member) {
		return repository.save(member);
	}

	public Member findById(Long id) {
		return repository.findById(id).orElseThrow();
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	public List<ChatRoom> findAllChatRooms(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));
		List<MemberChatRoom> memberChatRooms = member.getMemberChatRooms();
		List<ChatRoom> chatRooms = new ArrayList<>();
		for (MemberChatRoom memberChatRoom : memberChatRooms) {
			chatRooms.add(memberChatRoom.getChatRoom());
		}

		return chatRooms;
	}

	public List<Member> findByDorm(Dormitory dorm) {
		return repository.findByDorm(dorm);
	}

	public Optional<Member> findByNickname(String nickname) {
		return repository.findByNickname(nickname);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void deleteMemberCascade(Long id) {
		lifeStyleRepository.deleteByMemberId(id);
		preferenceRepository.deleteByMemberId(id);
		repository.deleteById(id);
	}

	// 상대방 회원번호를 통해 얻은 List<LifeStyle>을 Map<String, List<Long>>으로 변환
	public Map<String, List<Long>> convertLifeStyleListToMap(List<LifeStyle> lifeStyleList) {
		List<Option> options = lifeStyleList.stream()
			.map(LifeStyle::getOption)
			.toList();            // friend의 LifeStyle의 Option만 가져와 List<Option>에 저장

		Map<String, List<Long>> collect = options.stream()
			.collect(Collectors.groupingBy(        // Map 객체 리턴
				option -> option.getCategory().name(),
				Collectors.mapping(Option::getId, Collectors.toList())
			));
		// Option클래스의 getCategory : String을 key로 설정
		// Collectors.toList()를 통해 List를 생성하고 List<T>에서 T를 Long으로 설정하고 List<Long>안에 Option_id를 담음

		return collect;
	}

	// 상대방 회원번호를 통해 얻은 List<Preference>을 Map<String, List<Long>>으로 변환
	public Map<String, List<Long>> convertPreferenceListToMap(List<Preference> preferenceList) {
		List<Option> options = preferenceList.stream()
			.map(Preference::getOption)
			.toList();            // friend의 LifeStyle의 Option만 가져와 List<Option>에 저장

		Map<String, List<Long>> collect = options.stream()
			.collect(Collectors.groupingBy(        // Map 객체 리턴
				option -> option.getCategory().name(),
				Collectors.mapping(Option::getId, Collectors.toList())
			));

		return collect;
	}

	/**
	 * 나의 preference 와 상대 LifeStyle 을 비교하여 추천 멤버 조회
	 * @param myId
	 * @return 추천 멤버
	 */
	public List<Member> recommendMembers(Long myId) {
		Member member = memberRepository.findById(myId)
			.orElseThrow(() -> new NoSuchElementException("id not found"));

		List<Preference> preferences = member.getPreference();

		//상관 없음 체크한 항목 제외한 옵션 추출
		List<Option> options = preferences
			.stream()
			.filter(preference -> preference.getOption().getId() > 100)
			.map(Preference::getOption)
			.toList();

		Map<Category, List<Long>> cond = options.stream()
			.collect(
				Collectors.groupingBy(
					Option::getCategory,
					Collectors.mapping(Option::getId, Collectors.toList())
				));

		//나이 추출
		List<Long> ages = cond.remove(Category.AGE);
		List<Integer> intAges = getIntAges(ages);

		List<Member> search = memberRepository.search(myId, cond, intAges);

		return search;
	}

	/**
	 * 검색 조건을 입력받아 추천 멤버 검색
	 * @param myId 사용자 id
	 * @param filterCond 검색 조건
	 * @return 추천 멤버
	 */
	public List<Member> searchMembers(Long myId, FilterCond filterCond) {
		Map<Category, List<Long>> cond = filterCond.getCond();
		List<Long> ages = cond.remove(Category.AGE);
		List<Integer> intAges = getIntAges(ages);

		List<Member> search = memberRepository.search(myId, cond, intAges);
		return search;
	}

	//age Long 값 Integer 로 변경
	private static List<Integer> getIntAges(List<Long> ages) {

		List<Integer> intAges = ages == null ? new ArrayList<>() : ages.stream()
			.map(Long::intValue)
			.toList();
		return intAges;
	}

	//dto로 전환
	public RecommendMemberDTO convertToDTO(Member member) {
		RecommendMemberDTO dto = new RecommendMemberDTO();
		dto.setMemberId(member.getId());
		dto.setNickname(member.getNickname());
		dto.setIntroduce(member.getIntroduce());
		return dto;
	}
}
