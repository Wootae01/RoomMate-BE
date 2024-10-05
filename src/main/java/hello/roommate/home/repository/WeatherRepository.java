package hello.roommate.home.repository;

import hello.roommate.home.domain.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class WeatherRepository {
    private final NamedParameterJdbcTemplate template;

    public WeatherRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Weather save(Weather weather) {
        String sql = "insert into weather(fcst_time, fcst_date, tmp, sky, pty, tmn, tmx)" +
                "values(:fcstTime, :fcstDate, :tmp, :sky, :pty, :tmn, :tmx)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(weather);
        template.update(sql, param);
        return weather;
    }

    public Optional<Weather> findByFcstTime(String fcstTime) {
        String sql = "select * from weather where fcst_time=:fcstTime";

        try {
            Map param = Map.of("fcstTime", fcstTime);
            Weather weather = template.queryForObject(sql, param, weatherRowMapper());
            return Optional.of(weather);
        } catch (EmptyResultDataAccessException e) {
            log.info("inappropriate time");
            return Optional.empty();
        }
    }

    public void update(Weather weather) {
        String sql = "update weather" +
                " set fcst_date=:fcstDate, tmp=:tmp, sky=:sky, pty=:pty, tmn=:tmn, tmx=:tmx" +
                " where fcst_time=:fcstTime";
        SqlParameterSource param = new BeanPropertySqlParameterSource(weather);
        template.update(sql, param);
    }

    private RowMapper<Weather> weatherRowMapper() {
        return BeanPropertyRowMapper.newInstance(Weather.class);
    }
}
