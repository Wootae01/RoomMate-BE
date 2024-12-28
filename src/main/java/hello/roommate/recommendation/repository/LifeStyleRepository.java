package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {
}
