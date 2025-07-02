package hello.roommate.recommendation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {
	private final PreferenceRepository preferenceRepository;
	public Preference save(Preference preference) {
		return preferenceRepository.save(preference);
	}

}
