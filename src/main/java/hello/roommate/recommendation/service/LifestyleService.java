package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LifestyleService {
	private final LifeStyleRepository lifeStyleRepository;
	private final OptionRepository optionRepository;
	private final MemberRepository memberRepository;

	public LifeStyle save(LifeStyle lifeStyle) {
		lifeStyleRepository.save(lifeStyle);
		return lifeStyle;
	}

	public LifeStyle findById(Long id) {
		return lifeStyleRepository.findById(id).orElseThrow();
	}

	public List<LifeStyle> findByMemberId(Long memberId) {
		return lifeStyleRepository.findByMemberId(memberId);
	}

	public void update(Long memberId, LifeStyleDTO dto) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("Invalid Id"));

		//기존 lifestyle 삭제
		lifeStyleRepository.deleteByMemberId(memberId);

		// Map<String, List<Long>>에서 모든 옵션 Id를 추출 휴, Option 객체 조회
		List<Option> allOptions = new ArrayList<>();
		for (List<Long> optionIds : dto.getOptions().values()) {
			allOptions.addAll(optionRepository.findAllById(optionIds));
		}

		// 조회된 Option 객체들을 이용하여 새로운 LifeStyle 객체들을 생성
		List<LifeStyle> updateList = allOptions.stream()
			.map(option -> new LifeStyle(member, option))
			.collect(Collectors.toList());

		lifeStyleRepository.saveAll(updateList);

	}

	/**
	 * 각 카테고리 별로 조건에 맞는 멤버를 찾아 반환한다.
	 * 카테고리마다 맞는 조건이 1개 이상 있어야 된다.
	 *
	 * @param optionIds 조건
	 * @param totalCategory 조건의 전체 카테고리 개수
	 * @return 조건에 맞는 memberIds
	 */
	public List<Long> findMemberIdsCoverAllCategory(List<Long> optionIds, long totalCategory) {

		// 특정 조건이 없으면 모든 멤버 id 반환
		if (optionIds.isEmpty()) {
			return memberRepository.findAllIds();
		}
		return lifeStyleRepository.findMemberIdsCoverAllCategory(optionIds, totalCategory);
	}

	public void delete(Long id) {
		lifeStyleRepository.deleteById(id);
	}
}
