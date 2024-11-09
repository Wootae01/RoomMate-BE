package hello.roommate.recommendation.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.profile.domain.Profile;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;

@Repository
public class RecommendationRepository {
    private final NamedParameterJdbcTemplate template;
    private final MemberRepository memberRepository;
    public RecommendationRepository(DataSource dataSource, MemberRepository memberRepository) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.memberRepository = memberRepository;
    }

    public Recommendation save(Recommendation recommendation) {
        String sql = "insert into recommendation(member_id, matched_id, score) " +
                "values(:memberId, :matchedId, :score)";

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
        String sql = "select * from recommendation " +
                "where member_id=:memberId " +
                "ORDER BY score desc ";

        Map<String, String> param = Map.of("memberId", memberId);
        List<Recommendation> recommendations = template.query(sql, param, recommendationRowMapper());
        Member member = memberRepository.findById(memberId);

        for (Recommendation recommendation : recommendations) {
            String matchedId = recommendation.getMatchedMember().getId();
            Member matchedMember = memberRepository.findById(matchedId);
            recommendation.setMember(member);
            recommendation.setMatchedMember(matchedMember);
        }
        return recommendations;
    }

    public void update(RecommendationUpdateDto updateDto) {

        String sql = "update recommendation " +
                "set score=:score " +
                "where member_id=:memberId and matched_id=:matchedId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("score", updateDto.getScore())
                .addValue("memberId", updateDto.getMemberId())
                .addValue("matchedId", updateDto.getMatchedMemberId());

        template.update(sql, param);
    }

    public void delete(String memberId) {
        String sql = "delete from recommendation " +
                "where member_id =:memberId or matched_id=:memberId";
        Map<String, String> param = Map.of("memberId", memberId, "matchedId", memberId);

        template.update(sql, param);
    }

    private RowMapper<Recommendation> recommendationRowMapper() {
      return (rs, rowNum) ->{
          Recommendation recommendation = new Recommendation();
          Member member = new Member();
          Member matchedMember = new Member();

          member.setId(rs.getString("member_id"));
          matchedMember.setId(rs.getString("matched_id"));

          recommendation.setMember(member);
          recommendation.setMatchedMember(matchedMember);
          recommendation.setId(rs.getLong("result_id"));
          recommendation.setScore(rs.getDouble("score"));

          return recommendation;
      };
    }
}
