package hello.roommate.home.repository;

import hello.roommate.home.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    Optional<Weather> findByFcstDateAndFcstTime(String fcstDate, String fcstTime);
}
