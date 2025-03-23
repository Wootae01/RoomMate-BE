package hello.roommate.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.auth.domain.RefreshEntity;
import jakarta.transaction.Transactional;

public interface RefreshEntityRepository extends JpaRepository<RefreshEntity, Long> {

	Boolean existsByRefresh(String refresh);

	@Transactional
	void deleteByRefresh(String refresh);
}
