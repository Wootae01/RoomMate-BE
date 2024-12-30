package hello.roommate.home.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
@Data
@Entity
public class Notice {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;

    @Column(name = "notice_date")
    private String noticeDate;

    public Notice(){

    }

    public Notice(String title, String content, String noticeDate) {
        this.title = title;
        this.content = content;
        this.noticeDate = noticeDate;
    }
}
