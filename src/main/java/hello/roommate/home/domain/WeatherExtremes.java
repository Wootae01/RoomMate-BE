package hello.roommate.home.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WeatherExtremes {
    @Id
    @Column(name = "fcst_date")
    private String fcstDate;
    private Float tmn;
    private Float tmx;
}
