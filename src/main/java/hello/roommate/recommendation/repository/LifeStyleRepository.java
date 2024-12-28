package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LifeStyleRepository extends JpaRepository<LifeStyle, Long> {
    LifeStyle save(LifeStyle lifeStyle);

    Optional<LifeStyle> findById(Long aLong);

    void deleteById(Long id);

}
