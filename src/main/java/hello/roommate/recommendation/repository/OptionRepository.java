package hello.roommate.recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;

public interface OptionRepository extends JpaRepository<Option, Long> {

	@Query("select o from Option o where o.category = :category and o.optionValue = :value")
	Option findByCategoryAndValue(Category category, String value);

}
