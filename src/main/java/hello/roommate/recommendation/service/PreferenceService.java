package hello.roommate.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
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

		//새로운 preference 등록
		List<Preference> updateList = new ArrayList<>();
		List<Long> options = dto.getOptions();
		for (Long option : options) {
			Option find = optionRepository.findById(option)
				.orElseThrow(() -> new NoSuchElementException("Invalid Option id"));
			updateList.add(new Preference(member, find));
		}

		preferenceRepository.saveAll(updateList);
	}

}
