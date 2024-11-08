package hello.roommate.profile.repository;

import hello.roommate.profile.domain.Profile;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Repository
public class ProfileRepository {
    private final NamedParameterJdbcTemplate template;

    public ProfileRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Profile save(Profile profile) {
        String sql = "insert into profile(lifestyle_id, nickname, introduce, img) " +
                "values(:lifestyle_id, :nickname, :introduce, :img)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lifestyle_id", profile.getLifeStyle().getId())
                .addValue("nickname", profile.getNickname())
                .addValue("introduce", profile.getIntroduce())
                .addValue("img", profile.getImg());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        Long key = keyHolder.getKey().longValue();
        profile.setId(key);
        return profile;
    }


    public Profile findById(Long id) {
        String sql = "select p.*, l.* from profile p " +
                "left join lifestyle l on p.lifestyle_id = l.lifestyle_id " +
                "where p.profile_id=:id";
        Map<String, Long> param = Map.of("id", id);

        Profile profile = template.queryForObject(sql, param, profileRowMapper());
        return profile;
    }
    public Profile findByNickname(String nickname) {
        String sql = "select p.*, l.* from profile p " +
                "left join lifestyle l on p.lifestyle_id = l.lifestyle_id " +
                "where p.nickname=:nickname";
        Map<String, String> param = Map.of("nickname", nickname);

        Profile profile = template.queryForObject(sql, param, profileRowMapper());
        return profile;
    }

    public void update(String nickname, ProfileUpdateDto updateDto) {
        String sql = "update profile set " +
                "introduce=:introduce, img=:img " +
                "where nickname=:nickname";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("introduce", updateDto.getIntroduce())
                .addValue("img", updateDto.getImg())
                .addValue("nickname", nickname);
        template.update(sql, param);
    }

    public void deleteById(Long id) {
        String sql = "delete from profile where profile_id=:id";
        Map<String, Long> param = Map.of("id", id);
        template.update(sql, param);
    }
    public void deleteByNickname(String nickname) {
        String sql = "delete from profile where nickname=:nickname";
        Map<String, String> param = Map.of("nickname", nickname);
        template.update(sql, param);
    }

    private RowMapper<Profile> profileRowMapper() {
        return ((rs, rowNum) -> {
            Profile profile = new Profile();
            profile.setId(rs.getLong("profile_id"));
            profile.setImg(rs.getString("img"));
            profile.setNickname(rs.getString("nickname"));
            profile.setIntroduce(rs.getString("introduce"));
            profile.setLifeStyle(mapLifeStyle(rs));
            return profile;
        });
    }
    private LifeStyle mapLifeStyle(ResultSet rs) throws SQLException {
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
    }
}
