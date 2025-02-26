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
