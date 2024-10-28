package hello.roommate.home.repository;

import hello.roommate.home.domain.WeatherExtremes;
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
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class WeatherExtremesRepository {
    private final NamedParameterJdbcTemplate template;

    public WeatherExtremesRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public WeatherExtremes save(WeatherExtremes weatherExtremes) {
        String sql = "insert into weather_extremes(fcst_date, tmn, tmx)" +
                "values(:fcstDate, :tmn, :tmx)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(weatherExtremes);
        template.update(sql, param);
        return weatherExtremes;
    }

    public Optional<WeatherExtremes> findByDate(String fcstDate) {
        String sql = "select * from weather_extremes " +
                "where fcst_date = :fcstDate";
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("fcstDate", fcstDate);
            WeatherExtremes weatherExtremes = template.queryForObject(sql, param, weatherExtremesRowMapper());
            return Optional.of(weatherExtremes);
        } catch (EmptyResultDataAccessException e) {
            log.info("No extremes found for the given date: {}", fcstDate);
            return Optional.empty();
        }
    }

    public void update(WeatherExtremes extremes) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        boolean flag = false;
        if (extremes.getTmn() == null && extremes.getTmx() == null) {
            return;
        }
        String sql2 = "update weather_extremes" +
                " set ";

        if (extremes.getTmn() != null) {
            flag = true;
            sql2 += "tmn=:tmn ";
            param.addValue("tmn", extremes.getTmn());
        }
        if (extremes.getTmx() != null) {
            if (flag) {
                sql2 += ", tmx=:tmx";
            } else {
                sql2 += "tmx=:tmx";
            }
            param.addValue("tmx", extremes.getTmx());
        }
        sql2 += " where fcst_date=:fcstDate";
        param.addValue("fcstDate", extremes.getFcstDate());
        template.update(sql2, param);
    }

    public void delete(String fcstDate) {
        String sql = "delete from weather where fcst_date =:fcstDate";
        Map map = Map.of("fcstDate", fcstDate);
        template.update(sql, map);
    }

    private RowMapper<WeatherExtremes> weatherExtremesRowMapper() {
        return BeanPropertyRowMapper.newInstance(WeatherExtremes.class);
    }
}
