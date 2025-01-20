package hello.roommate.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.roommate.home.domain.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

	Optional<Weather> findByFcstDateAndFcstTime(String fcstDate, String fcstTime);
}
