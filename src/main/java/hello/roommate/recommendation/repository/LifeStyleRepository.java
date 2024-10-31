package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LifeStyleRepository {
    private final NamedParameterJdbcTemplate template;

    public LifeStyleRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public LifeStyle save(LifeStyle lifeStyle) {
        return lifeStyle;
    }

    public void update(Long lifeStyleId,) {

    }

}
