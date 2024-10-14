package hello.roommate.home.external.weather;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class WeatherApiClientTest {
    @Autowired
    WeatherApiClient weatherApiClient;
    @Test
    void fetchTodayWeather() {
        String response = weatherApiClient.fetchTodayWeather();
        log.info(response);
    }

    @Test
    void fetchWeatherNext3Hours() {
        log.info("Classpath resource found: " + this.getClass().getClassLoader().getResource("api-key.properties"));
    }
}