package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

	public void saveAll(List<LifeStyle> lifeStyles) {
		lifeStyleRepository.saveAll(lifeStyles);
	}

	public void update(Long memberId, LifeStyleDTO dto) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("Invalid Id"));

		//기존 lifestyle 삭제
		lifeStyleRepository.deleteByMemberId(memberId);

		//새 LifeStyle 등록
		List<LifeStyle> updateList = new ArrayList<>();
		List<Long> options = dto.getOptions();
		for (Long option : options) {
			Option find = optionRepository.findById(option)
				.orElseThrow(() -> new NoSuchElementException("Invalid Option id"));

			updateList.add(new LifeStyle(member, find));
		}

		lifeStyleRepository.saveAll(updateList);

	}

	public void delete(Long id) {
		lifeStyleRepository.deleteById(id);
	}
}
