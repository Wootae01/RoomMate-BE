package hello.roommate.recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.recommendation.domain.LifeStyle;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {
}
