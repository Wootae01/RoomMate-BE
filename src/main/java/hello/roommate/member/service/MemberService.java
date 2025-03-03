package hello.roommate.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository repository;
	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;

	public Member save(Member member) {
		return repository.save(member);
	}

	public Member findById(Long id) {
		return repository.findById(id).orElseThrow();
	}

	public List<ChatRoom> findAllChatRooms(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
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

	public Member findByNickname(String nickname) {
		return repository.findByNickname(nickname).orElseThrow(() -> new NoSuchElementException("Member not found"));
	}

	public void delete(Long id) {
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

		List<Option> options = preferences
			.stream().map(preference -> preference.getOption())
			.toList();

		Map<Category, List<Long>> collect = options.stream()
			.collect(
				Collectors.groupingBy(
					Option::getCategory,
					Collectors.mapping(Option::getId, Collectors.toList())
				));

		FilterCond filterCond = new FilterCond();
		filterCond.setCond(collect);

		List<Member> search = memberRepository.search(myId, filterCond);

		return search;
	}

	/**
	 * 검색 조건을 입력받아 추천 멤버 검색
	 * @param myId 사용자 id
	 * @param filterCond 검색 조건
	 * @return 추천 멤버
	 */
	public List<Member> searchMembers(Long myId, FilterCond filterCond) {
		List<Member> search = memberRepository.search(myId, filterCond);
		return search;
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
