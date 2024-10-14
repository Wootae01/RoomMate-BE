package hello.roommate.home.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherExtremes {
    private String fcstDate;
    private Float tmn;
    private Float tmx;
}
