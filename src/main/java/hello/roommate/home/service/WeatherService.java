package hello.roommate.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.roommate.home.domain.Weather;
import hello.roommate.home.domain.WeatherExtremes;
import hello.roommate.home.external.weather.WeatherApiClient;
import hello.roommate.home.external.weather.WeatherResponse;
import hello.roommate.home.repository.WeatherDto;
import hello.roommate.home.repository.WeatherExtremesRepository;
import hello.roommate.home.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static hello.roommate.home.external.weather.WeatherResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherExtremesRepository extremesRepository;
    private final WeatherApiClient apiClient;

    /*
    * 현재 날씨 db에서 꺼내서 반환
    * */
    public WeatherDto getCurrentWeather() {
        LocalTime time = LocalTime.now().withMinute(0).withSecond(0);
        LocalDate date = LocalDate.now();

        String baseTime = time.format(DateTimeFormatter.ofPattern("HHmm"));
        String baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Weather weather = weatherRepository.findByFcstDateAndFcstTime(baseDate, baseTime)
                .orElseThrow(() -> new NoSuchElementException("Unable to get weather information"));
        WeatherExtremes extremes = extremesRepository.findByDate(baseDate)
                .orElseThrow(() -> new NoSuchElementException("Unable to get weatherExtremes information"));

        WeatherDto dto = new WeatherDto(weather, extremes);
        return dto;
    }

    /*
    * 단기 에보는 3시간마다 업데이트됨
    * 3시간 후까지의 날씨 예보를 받아서 업데이트
    * */
    @Scheduled(cron = "0 11 2/3 * * *")
    public void fetchWeatherNext3Hour() {
        String json = apiClient.fetchWeatherNext3Hours();
        Map<String, WeatherDto> weatherMap = new HashMap<>();
        parseWeather(json, weatherMap);

        updateWeather(weatherMap);
    }

    /*
    * 처음 시작할 때 db에 업데이트
    * */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String json = apiClient.fetchTodayWeather();
        Map<String, WeatherDto> weatherMap = new HashMap<>();
        parseWeather(json, weatherMap);

        updateWeather(weatherMap);
    }

    /*
    * json 데이터를 받아서 weather로 parsing
    * */
    private static void parseWeather(String json, Map<String, WeatherDto> weatherMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WeatherResponse response = objectMapper.readValue(json, WeatherResponse.class);
            List<Item> items = response.getResponse().getBody().getItems().getItem();
            for (Item item : items) {
                String category = item.getCategory();
                WeatherDto weatherDto = getWeather(weatherMap, item);
                setWeatherCategory(category, weatherDto, item);
            }
        } catch (JsonProcessingException e) {
            log.info("json parsing not possible");
            throw new RuntimeException(e);
        }
    }

    /*
    * db의 날씨 정보 업데이트
    * 현재 예측된 날씨 값이 있으면 업데이트
    * 없으면 새로 만듬
    * */
    private void updateWeather(Map<String, WeatherDto> weatherMap) {
        for (String key : weatherMap.keySet()) {
            WeatherDto dto = weatherMap.get(key);
            Weather weather = new Weather(dto.getFcstTime(), dto.getFcstDate(),
                    dto.getTmp(), dto.getSky(), dto.getPty());
            WeatherExtremes extremes = new WeatherExtremes(dto.getFcstDate(), dto.getTmn(), dto.getTmx());

            Optional<Weather> find = weatherRepository
                    .findByFcstDateAndFcstTime(dto.getFcstDate(), dto.getFcstTime());
            if (find.isPresent()) {
                update(weather);
            } else{
                weatherRepository.save(weather);
            }

            Optional<WeatherExtremes> find2 = extremesRepository
                    .findByDate(dto.getFcstDate());
            if (find2.isPresent()) {
                extremesRepository.update(extremes);
            } else{
                extremesRepository.save(extremes);
            }
        }
    }

    public void update(Weather dto) {
        Weather weather = weatherRepository.findByFcstDateAndFcstTime(dto.getFcstDate(), dto.getFcstTime()).orElseThrow();
        weather.setTmp(dto.getTmp());
        weather.setSky(dto.getSky());
        weather.setPty(dto.getPty());
    }

    private static void setWeatherCategory(String category, WeatherDto weather, Item item) {
        switch(category) {
            case "TMP":
                weather.setTmp(Integer.parseInt(item.getFcstValue()));
                break;
            case "SKY":
                weather.setSky(Integer.parseInt(item.getFcstValue()));
                break;
            case "PTY":
                weather.setPty(Integer.parseInt(item.getFcstValue()));
                break;
            case "TMN":
                weather.setTmn(Float.parseFloat(item.getFcstValue()));
                break;
            case "TMX":
                weather.setTmx(Float.parseFloat(item.getFcstValue()));
                break;
        }
    }

    //weatherMap에 값이 있으면 그거 반환, 없으면 새로운 객체 만들어서 넣고 반환
    private static WeatherDto getWeather(Map<String, WeatherDto> map, Item item) {
        String date = item.getFcstDate();
        String time = item.getFcstTime();
        String key = date + "_" + time;
        if (!map.containsKey(key)) {
            map.put(key, new WeatherDto(date, time));
        }
        return map.get(key);
    }
}
