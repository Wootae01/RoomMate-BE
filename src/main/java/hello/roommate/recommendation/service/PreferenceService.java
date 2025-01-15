package hello.roommate.recommendation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceService {
	private final PreferenceRepository preferenceRepository;

	public Preference save(Preference preference) {
		return preferenceRepository.save(preference);
	}

	public List<Member> searchMembers() {
		return null;
	}
}
