package hello.roommate.home.repository;

import hello.roommate.home.domain.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
@Slf4j
public class WeatherRepository {
    private final NamedParameterJdbcTemplate template;

    public WeatherRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Weather save(Weather weather) {
        String sql = "insert into weather(fcst_time, fcst_date, tmp, sky, pty)" +
                "values(:fcstTime, :fcstDate, :tmp, :sky, :pty)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(weather);
        template.update(sql, param);
        return weather;
    }

    public Optional<Weather> findWeatherByDateAndTime(String fcstDate, String fcstTime) {
        String sql = "select * from weather " +
                "where fcst_date = :fcstDate and fcst_time = :fcstTime";
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("fcstDate", fcstDate)
                    .addValue("fcstTime", fcstTime);
            Weather weather = template.queryForObject(sql, param, weatherRowMapper());
            return Optional.of(weather);
        } catch (EmptyResultDataAccessException e) {
            log.info("No weather found for the given date and time: {}, {}", fcstDate, fcstTime);
            return Optional.empty();
        }
    }

    public void update(Weather weather) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        String sql = "update weather" +
                " set tmp = :tmp, sky = :sky, pty = :pty" +
                " where fcst_date = :fcstDate and fcst_time = :fcstTime";
        param.addValue("tmp", weather.getTmp())
                .addValue("sky", weather.getSky())
                .addValue("pty", weather.getPty())
                .addValue("fcstDate", weather.getFcstDate())
                .addValue("fcstTime", weather.getFcstTime());
        template.update(sql, param);
    }

    private RowMapper<Weather> weatherRowMapper() {
        return BeanPropertyRowMapper.newInstance(Weather.class);
    }
}
