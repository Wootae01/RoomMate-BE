package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.Recommendation;
import org.apache.tomcat.Jar;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class RecommendationRepository {
    private final NamedParameterJdbcTemplate template;

    public RecommendationRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Recommendation save(Recommendation recommendation) {
        String sql = "insert into recommendation(result_id, member_id, matched_id, score) " +
                "values(:id, :memberId, :matchedId, :score)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", recommendation.getMember().getId())
                .addValue("matchedId", recommendation.getMatchedMember().getId())
                .addValue("score", recommendation.getScore());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        Long key = keyHolder.getKey().longValue();
        recommendation.setId(key);
        return recommendation;
    }

    public List<Recommendation> findByMemberId(String memberId) {
        String sql = "select * from recommendation where member_id = :memberId";
        Map<String, String> param = Map.of("id", memberId);

        return template.query(sql, param, recommendationRowMapper());
    }

    public void update(RecommendationUpdateDto updateDto) {

        String sql = "update recommendation " +
                "set score=:score " +
                "where member_id=:memberId and matched_id=:matched_id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("score", updateDto.getScore())
                .addValue("memberId", updateDto.getMember().getId())
                .addValue("matchedId", updateDto.getMatchedMember().getId());

        template.update(sql, param);
    }

    public void delete(String memberId) {
        String sql = "delete from recommendation " +
                "where member_id =:memberId or matched_id=:memberId";
        Map<String, String> param = Map.of("memberId", memberId, "matchedId", memberId);

        template.update(sql, param);
    }

    public RowMapper<Recommendation> recommendationRowMapper() {
        return BeanPropertyRowMapper.newInstance(Recommendation.class);
    }
}
