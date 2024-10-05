package hello.roommate.home.repository;

import hello.roommate.home.domain.Notice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class NoticeRepositoryTest {
    @Autowired  NoticeRepository repository;
    @Test
    void save() {
        Notice notice = new Notice("title", "content", "2024/09/28");

        Notice save = repository.save(notice);
        Assertions.assertThat(save).isEqualTo(notice);
    }

    @Test
    void findById() {
        //given
        Notice notice = new Notice("title", "content", "2024/09/28");
        repository.save(notice);
        //when
        Notice find = repository.findById(notice.getId()).orElseThrow();
        //then
        Assertions.assertThat(find).isEqualTo(notice);
    }

    @Test
    void findByTitle() {
        Notice notice = new Notice("title", "content", "2024/09/28");
        repository.save(notice);
        //when
        Notice find = repository.findByTitle(notice.getTitle()).orElseThrow();
        //then
        Assertions.assertThat(find).isEqualTo(notice);
    }

    @Test
    void findAllTitles() {
        Notice notice1 = new Notice("title", "content", "2024/09/28");
        Notice notice2 = new Notice("title2", "content", "2024/09/28");
        Notice notice3 = new Notice("title3", "content", "2024/09/28");
        repository.save(notice1);
        repository.save(notice2);
        repository.save(notice3);

        List<String> titles = repository.findAllTitles();

        Assertions.assertThat(titles).contains("title")
                .contains("title2")
                .contains("title3");
    }

    @Test
    void delete() {
        //given
        Notice notice = new Notice("title", "content", "2024/09/28");
        repository.save(notice);
        //when
        repository.deleteById(notice.getId());
        //then
        Assertions.assertThatThrownBy(() -> repository.findById(notice.getId()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteByTitle() {
        Notice notice = new Notice("title", "content", "2024/09/28");
        repository.save(notice);
        //when
        repository.deleteByTitle("title");
        //then
        Assertions.assertThatThrownBy(() -> repository.findById(notice.getId()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }
}