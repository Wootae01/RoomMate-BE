package hello.roommate.home.external.weather;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherRequestParam {
    private final String URI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String dataType; //json or xml
    private String baseDate; //20210628
    private String baseTime; //0600
    private int nx;
    private int ny;

    public WeatherRequestParam(String serviceKey, int pageNo, int numOfRows, String dataType, String baseDate, String baseTime, int nx, int ny) {
        this.serviceKey = serviceKey;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.dataType = dataType;
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }

    public WeatherRequestParam() {

    }
}
