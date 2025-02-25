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
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.dto.PreferenceDTO;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {
	private final PreferenceRepository preferenceRepository;
	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;

	public Preference save(Preference preference) {
		return preferenceRepository.save(preference);
	}

	public List<Preference> findByMemberId(Long memberId) {
		return preferenceRepository.findByMemberId(memberId);
	}

	public void saveAll(List<Preference> preferences) {
		preferenceRepository.saveAll(preferences);
	}

	public void update(Long memberId, PreferenceDTO dto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("Invalid Id"));

		//기존 preference 삭제
		preferenceRepository.deleteByMemberId(memberId);

		// Map<String, List<Long>>에서 모든 옵션 Id를 추출 휴, Option 객체 조회
		List<Option> allOptions = new ArrayList<>();
		for (List<Long> optionIds : dto.getOptions().values()) {
			allOptions.addAll(optionRepository.findAllById(optionIds));
		}

		// 조회된 Option 객체들을 이용하여 새로운 preference 객체들을 생성
		List<Preference> updateList = allOptions.stream()
			.map(option -> new Preference(member, option))
			.collect(Collectors.toList());

		preferenceRepository.saveAll(updateList);
	}

}
