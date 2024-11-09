package hello.roommate.member.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.profile.domain.Profile;
import hello.roommate.recommendation.domain.LifeStyle;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository {
    private final NamedParameterJdbcTemplate template;

    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }
    public Member save(Member member) {
        String sql = "insert into Member (member_id, profile_id, lifestyle_id, password, email, dorm)" +
                " values (:id, :profileId, :lifestyleId, :password, :email, :dorm)";


        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", member.getId())
                .addValue("profileId", member.getProfile().getId())
                .addValue("lifestyleId", member.getLifeStyle().getId())
                .addValue("password", member.getPassword())
                .addValue("email", member.getEmail())
                .addValue("dorm", member.getDorm().name());
        template.update(sql, param);

        return member;
    }

    public Member findById(String id) {
        String sql = "select m.*, p.*, l.* " +
                "from Member m " +
                "Left Join Profile p ON m.profile_id = p.profile_id " +
                "Left Join LifeStyle l ON m.lifestyle_id = l.lifestyle_id " +
                "where m.member_id =:id";

        Map<String, String> param = Map.of("id", id);
        Member member = template.queryForObject(sql, param, memberRowMapper());
        return member;
    }

    public List<Member> findByDorm(String dorm) {
        String sql = "select m.*, p.*, l.* " +
                "from Member m " +
                "Left Join Profile p ON m.profile_id = p.profile_id " +
                "Left Join LifeStyle l ON m.lifestyle_id = l.lifestyle_id " +
                "where m.dorm =:dorm";

        Map<String, String> param = Map.of("dorm", dorm);

        return template.query(sql, param, memberRowMapper());
    }

    public void delete(String id) {
        String sql = "delete from member where member_id=:id";
        Map<String, String> param = Map.of("id", id);
        template.update(sql, param);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = mapMember(rs);
            member.setProfile(mapProfile(rs));
            member.setLifeStyle(mapLifeStyle(rs));
            return member;
        };
    }

    private Member mapMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getString("member_id"));
        member.setDorm(Dormitory.valueOf(rs.getString("dorm")));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        return member;
    }

    private Profile mapProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getLong("profile_id"));
        profile.setImg(rs.getString("img"));
        profile.setIntroduce(rs.getString("introduce"));
        profile.setNickname(rs.getString("nickname"));
        profile.setLifeStyle(mapLifeStyle(rs));
        return profile;
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

