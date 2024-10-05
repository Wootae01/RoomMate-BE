package hello.roommate.home.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Weather {
    private String fcstTime; //예측 시간
    private String fcstDate; //예측 날짜

    private int tmp; //1시간 기온
    private int sky; //하늘 상태    맑음(1), 구름많음(3), 흐림(4)
    private int pty; //강수 형태    없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private int tmn; //일 최저 기온
    private int tmx; //일 최고 기온

    public Weather(String fcstTime, String fcstDate, int tmp, int sky, int pty, int tmn, int tmx) {
        this.fcstTime = fcstTime;
        this.fcstDate = fcstDate;
        this.tmp = tmp;
        this.sky = sky;
        this.pty = pty;
        this.tmn = tmn;
        this.tmx = tmx;
    }

    public Weather() {

    }
}
