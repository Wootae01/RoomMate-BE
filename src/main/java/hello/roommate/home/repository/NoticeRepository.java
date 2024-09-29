package hello.roommate.home.repository;

import hello.roommate.home.domain.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class NoticeRepository {
    private final NamedParameterJdbcTemplate template;
    public NoticeRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Notice save(Notice notice) {
        String sql = "insert into notice(title, content, notice_date)" +
                " values(:title, :content, :noticeDate)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(notice);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        Long key = keyHolder.getKey().longValue();
        notice.setId(key);
        return notice;
    }

    public List<String> findAllTitles() {
        String sql = "select title from notice";
        return template.queryForList(sql, new HashMap<>(), String.class);
    }

    public Optional<Notice> findByTitle(String title) {
        String sql = "select id, title, content, notice_date from notice where title=:title";
        try {
            Map<String, Object> param = Map.of("title", title);
            Notice notice = template.queryForObject(sql, param, noticeRowMapper());
            return Optional.of(notice);
        } catch (EmptyResultDataAccessException e) {
            log.info("No notices found");
            return Optional.empty();
        }
    }

    public Optional<Notice> findById(Long id) {
        String sql = "select id, title, content, notice_date from notice where id=:id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Notice notice = template.queryForObject(sql, param, noticeRowMapper());
            return Optional.of(notice);
        } catch (EmptyResultDataAccessException e) {
            log.info("No notices found");
            return Optional.empty();
        }
    }
    public void deleteById(Long id) {
        String sql = "delete from notice where id=:id";

        Map<String, Object> param = Map.of("id", id);
        template.update(sql, param);
    }

    public void deleteByTitle(String title) {
        String sql = "delete from notice where title=:title";
        Map<String, Object> param = Map.of("title", title);
        template.update(sql, param);
    }

    private RowMapper<Notice> noticeRowMapper() {
        return BeanPropertyRowMapper.newInstance(Notice.class);
    }
}
