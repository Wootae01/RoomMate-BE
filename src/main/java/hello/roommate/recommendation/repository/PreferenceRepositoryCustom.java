package hello.roommate.recommendation.repository;

import java.util.List;

import hello.roommate.recommendation.domain.Preference;

public interface PreferenceRepositoryCustom {
	List<Preference> search(PreferenceSearchCond cond);
}
