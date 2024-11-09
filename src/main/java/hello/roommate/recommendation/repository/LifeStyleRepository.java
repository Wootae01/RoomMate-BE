package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class LifeStyleRepository {
    private final NamedParameterJdbcTemplate template;

    public LifeStyleRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public LifeStyle save(LifeStyle lifeStyle) {
        String sql = "insert into LifeStyle" +
                "(bed_time, wakeup_time, sleep_habit, cleaning, aircon, heater, noise, " +
                "smoking, scent, eating, relationship, home, drinking, age, dorm_hour) " +
                "values (:bedTime, :wakeupTime, :sleepHabit, :cleaning, :aircon, :heater, :noise, " +
                ":smoking, :scent, :eating, :relationship, :home, :drinking, :age, :dormHour)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("bedTime", lifeStyle.getBedTime())
                .addValue("wakeupTime", lifeStyle.getWakeupTime())
                .addValue("sleepHabit", lifeStyle.getSleepHabit())
                .addValue("cleaning", lifeStyle.getCleaning())
                .addValue("aircon", lifeStyle.getAircon())
                .addValue("heater", lifeStyle.getHeater())
                .addValue("noise", lifeStyle.getNoise())
                .addValue("smoking", lifeStyle.getSmoking())
                .addValue("scent", lifeStyle.getScent())
                .addValue("eating", lifeStyle.getEating())
                .addValue("relationship", lifeStyle.getRelationship())
                .addValue("home", lifeStyle.getHome())
                .addValue("drinking", lifeStyle.getDrinking())
                .addValue("age", lifeStyle.getAge())
                .addValue("dormHour", lifeStyle.getDormHour());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        lifeStyle.setId(key);
        return lifeStyle;
    }
    public LifeStyle findById(Long id) {
        String sql = "select * from lifestyle where lifestyle_id=:id";

        Map<String, Long> param = Map.of("id", id);
        LifeStyle lifeStyle = template.queryForObject(sql, param, lifeStyleRowMapper());
        return lifeStyle;
    }

    public void update(Long lifeStyleId, LifeStyleUpdateDto dto) {
        String sql = "update lifestyle set " +
                "bed_time = :bedTime, wakeup_time = :wakeupTime, sleep_habit = :sleepHabit, " +
                "cleaning = :cleaning, aircon = :aircon, heater = :heater, noise = :noise, " +
                "smoking = :smoking, scent = :scent, eating = :eating, relationship = :relationship, " +
                "home = :home, drinking = :drinking, age = :age, dorm_hour = :dormHour " +
                "where lifestyle_id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("bedTime", dto.getBedTime())
                .addValue("wakeupTime", dto.getWakeupTime())
                .addValue("sleepHabit", dto.getSleepHabit())
                .addValue("cleaning", dto.getCleaning())
                .addValue("aircon", dto.getAircon())
                .addValue("heater", dto.getHeater())
                .addValue("noise", dto.getNoise())
                .addValue("smoking", dto.getSmoking())
                .addValue("scent", dto.getScent())
                .addValue("eating", dto.getEating())
                .addValue("relationship", dto.getRelationship())
                .addValue("home", dto.getHome())
                .addValue("drinking", dto.getDrinking())
                .addValue("age", dto.getAge())
                .addValue("dormHour", dto.getDormHour())
                .addValue("id", lifeStyleId);

        template.update(sql, param);
    }

    public void delete(Long id) {
        String sql = "delete from lifestyle where lifestyle_id=:id";

        Map<String, Long> param = Map.of("id", id);
        template.update(sql, param);
    }
    private RowMapper<LifeStyle> lifeStyleRowMapper() {
        return (rs, rowNum) -> {
            LifeStyle lifeStyle = new LifeStyle();
            lifeStyle.setId(rs.getLong("lifestyle_id"));
            lifeStyle.setBedTime(rs.getInt("bed_time"));
            lifeStyle.setWakeupTime(rs.getInt("wakeup_time"));
            lifeStyle.setSleepHabit(rs.getInt("sleep_habit"));
            lifeStyle.setCleaning(rs.getInt("cleaning"));
            lifeStyle.setAircon(rs.getInt("aircon"));
            lifeStyle.setHeater(rs.getInt("heater"));
            lifeStyle.setNoise(rs.getInt("noise"));
            lifeStyle.setSmoking(rs.getInt("smoking"));
            lifeStyle.setScent(rs.getInt("scent"));
            lifeStyle.setEating(rs.getInt("eating"));
            lifeStyle.setRelationship(rs.getInt("relationship"));
            lifeStyle.setHome(rs.getInt("home"));
            lifeStyle.setDrinking(rs.getInt("drinking"));
            lifeStyle.setAge(rs.getInt("age"));
            lifeStyle.setDormHour(rs.getInt("dorm_hour"));
            return lifeStyle;
        };
    }


}
