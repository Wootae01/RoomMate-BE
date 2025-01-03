package hello.roommate.home.repository;

import hello.roommate.home.domain.Weather;
import hello.roommate.home.domain.WeatherExtremes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherDto {
	private String fcstTime; //예측 시간
	private String fcstDate; //예측 날짜

	private int tmp; //1시간 기온
	private int sky; //하늘 상태    맑음(1), 구름많음(3), 흐림(4)
	private int pty; //강수 형태    없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
	private Float tmn; //일 최저 기온
	private Float tmx; //일 최고 기온

	public WeatherDto(String fcstDate, String fcstTime) {
		this.fcstDate = fcstDate;
		this.fcstTime = fcstTime;
	}

	public WeatherDto() {

	}

	public WeatherDto(Weather weather, WeatherExtremes extremes) {
		this.fcstDate = weather.getFcstDate();
		this.fcstTime = weather.getFcstTime();
		this.tmp = weather.getTmp();
		this.sky = weather.getSky();
		this.tmn = extremes.getTmn();
		this.tmx = extremes.getTmx();
	}
}
