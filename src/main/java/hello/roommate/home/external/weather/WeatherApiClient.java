package hello.roommate.home.external.weather;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class WeatherApiClient {

    @Value("${weather.serviceKey}")
    private String serviceKey;

    //하루 날씨를 가져옴
    public String fetchTodayWeather() {
        WeatherRequestParam param
                = new WeatherRequestParam(serviceKey, 1, 290, "json", getBaseDate(), getBaseTime(), 68, 106);

        URI uri = getUri(param);
        log.info(uri.toString());
        String response = getResponse(uri);
        return response;
    }

    //3시간치 날씨 가져옴
    public String fetchWeatherNext3Hours() {
        WeatherRequestParam param
                = new WeatherRequestParam(serviceKey, 1, 37, "json", getBaseDate(), getBaseTime(), 68, 106);

        URI uri = getUri(param);
        String response = getResponse(uri);
        return response;
    }

    //날씨 api 요청 후 json 응답 반환
    private static String getResponse(URI uri) {
        RestClient restClient = RestClient.create();
        String response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(String.class);
        return response;
    }

    //api 요청 uri 생성해서 반환
    private URI getUri(WeatherRequestParam param) {
        String uri = UriComponentsBuilder.fromUriString(param.getURI())
                .queryParam("serviceKey", param.getServiceKey())
                .queryParam("numOfRows", param.getNumOfRows())
                .queryParam("pageNo", param.getPageNo())
                .queryParam("base_date", param.getBaseDate())
                .queryParam("base_time", param.getBaseTime())
                .queryParam("nx", param.getNx())
                .queryParam("ny", param.getNy())
                .queryParam("dataType", param.getDataType())
                .encode()
                .toUriString();
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            log.info("incorrect URI");
            throw new RuntimeException(e);
        }
    }

    //기준 날짜 반환
    private String getBaseDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime time = now.toLocalTime();
        if (time.isBefore(LocalTime.of(2, 11))) {
            now.minusDays(1);
        }
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return baseDate;
    }
    //기준 시간 반환
    private String getBaseTime() {
        LocalTime now = LocalTime.now();

        LocalTime time2 = LocalTime.of(2, 11);
        LocalTime time5 = LocalTime.of(5, 11);
        LocalTime time8 = LocalTime.of(8, 11);
        LocalTime time11 = LocalTime.of(11, 11);
        LocalTime time14 = LocalTime.of(14, 11);
        LocalTime time17 = LocalTime.of(17, 11);
        LocalTime time20 = LocalTime.of(20, 11);
        LocalTime time23 = LocalTime.of(23, 11);

        if (now.isBefore(time2)) {
            return "2300";
        } else if (now.isBefore(time5)) {
            return "0200";
        }
        else if (now.isBefore(time8)) {
            return "0500";
        }
        else if (now.isBefore(time11)) {
            return "0800";
        }
        else if (now.isBefore(time14)) {
            return "1100";
        }
        else if (now.isBefore(time17)) {
            return "1400";
        }
        else if (now.isBefore(time20)) {
            return "1700";
        }
        else if (now.isBefore(time23)) {
            return "2000";
        }
        return "2300";
    }
}
