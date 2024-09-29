package hello.roommate.home.domain;

import lombok.*;

import java.time.LocalDate;
@Data
public class Notice {
    private Long id;
    private String title;
    private String content;
    private String noticeDate;
    public Notice(){

    }

    public Notice(String title, String content, String noticeDate) {
        this.title = title;
        this.content = content;
        this.noticeDate = noticeDate;
    }
}
