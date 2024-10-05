package hello.roommate.home.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class WeatherRequest {
    private final String URI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String dataType; //json or xml
    private String base_date; //20210628
    private String base_time; //0600
    private int nx;
    private int ny;

    public WeatherRequest(String serviceKey, int pageNo, int numOfRows, String dataType, String base_date, String base_time, int nx, int ny) {
        this.serviceKey = serviceKey;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.dataType = dataType;
        this.base_date = base_date;
        this.base_time = base_time;
        this.nx = nx;
        this.ny = ny;
    }

}
