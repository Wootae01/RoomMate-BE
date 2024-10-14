package hello.roommate.home.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private String fcstTime; //예측 시간
    private String fcstDate; //예측 날짜

    private int tmp; //1시간 기온
    private int sky; //하늘 상태    맑음(1), 구름많음(3), 흐림(4)
    private int pty; //강수 형태    없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)

    public Weather(String fcstDate, String fcstTime) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
    }


}
