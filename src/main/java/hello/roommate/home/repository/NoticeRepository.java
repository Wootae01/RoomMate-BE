package hello.roommate.home.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.roommate.home.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	@Query("select n.title from Notice n")
	List<String> findAllTitles();

	Optional<Notice> findByTitle(String title);

	void deleteByTitle(String title);
}
