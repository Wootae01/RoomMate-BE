package hello.roommate.recommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.recommendation.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long>, PreferenceRepositoryCustom {

	List<Preference> findByMemberId(String memberId);
}
