package hello.roommate.home.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.home.domain.Weather;

@Transactional
@SpringBootTest
class WeatherRepositoryTest {
	@Autowired
	WeatherRepository repository;

	@Test
	void save() {
		Weather weather = new Weather("20241005", "1700", 20, 0, 0);
		Weather save = repository.save(weather);
		Assertions.assertThat(save).isEqualTo(weather);
	}

	@Test
	void findByFcstDateAndFcstTime() {
		Weather weather = new Weather("1700", "20241005", 20, 0, 0);
		Weather save = repository.save(weather);
		Optional<Weather> optional = repository.findByFcstDateAndFcstTime("20241005", "1700");

		Weather find = optional.orElseThrow();
		Assertions.assertThat(find).isEqualTo(save);
	}

}
