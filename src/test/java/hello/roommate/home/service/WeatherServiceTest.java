package hello.roommate.home.service;

import static hello.roommate.home.external.weather.WeatherResponse.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.home.domain.Weather;
import hello.roommate.home.external.weather.WeatherApiClient;
import hello.roommate.home.repository.WeatherDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
class WeatherServiceTest {
	@Autowired
	private WeatherService service;

	@Autowired
	private WeatherApiClient apiClient;

	@Test
	void getCurrentWeather() {
	}

	@Test
	void fetchWeatherNext3Hour() {
		service.fetchWeatherNext3Hour();
		WeatherDto weather = service.getCurrentWeather();
		log.info("weather={}", weather);
	}

	/* @Test
	 void parseWeather() {
		 String json = apiClient.fetchTodayWeather();
		 ObjectMapper objectMapper = new ObjectMapper();
		 Map<String, Weather> weatherMap = new HashMap<>();
		 try {
			 WeatherResponse response = objectMapper.readValue(json, WeatherResponse.class);
			 List<Item> items = response.getResponse().getBody().getItems().getItem();
			 for (Item item : items) {
				 String category = item.getCategory();
				 Weather weather = getWeather(weatherMap, item);
				 setWeatherCategory(category, weather, item);
			 }
			 for (String key : weatherMap.keySet()) {
				 Weather weather = weatherMap.get(key);
				 log.info("weather={}", weather);
			 }
		 } catch (JsonProcessingException e) {
			 log.info("json parsing not possible");
			 throw new RuntimeException(e);
		 }
	 }*/
	private static Weather getWeather(Map<String, Weather> map, Item item) {
		String date = item.getFcstDate();
		String time = item.getFcstTime();
		String key = date + "_" + time;
		if (!map.containsKey(key)) {
			map.put(key, new Weather(date, time));
		}
		return map.get(key);
	}

}