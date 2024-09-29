package hello.roommate.home.crawler;

import hello.roommate.home.domain.Notice;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class NoticeCrawler {
    private final String URL = "https://dorm.chungbuk.ac.kr";
    private final String NOTICE_URN = "sub.php?menukey=20039";


    public Document getNoticePage(String urn) {
        try {
            return Jsoup.connect(URL + "/home/" + urn).get();
        } catch (IOException e) {
            log.info("IOException occurred during crawling");
            throw new RuntimeException(e);
        }
    }
    public Document getNoticePage() {
        try {
            return Jsoup.connect(URL + "/home/" + NOTICE_URN).get();
        } catch (IOException e) {
            log.info("IOException occurred during crawling");
            throw new RuntimeException(e);
        }
    }

    public List<Notice> getNotices() {
        List<Notice> list = new ArrayList<>();
        List<String> urns = getURN();
        for (String urn : urns) {
            Document noticePage = getNoticePage(urn);
            Elements board = noticePage.select(".board_insert");

            String title = board.select("th[colspan=6]").text();
            String date = board.select("th:contains(등록일) + td").first().text();

            //이미지있는 경우는 상대경로를 절대 경로로 바꿔서 저장
            Element content = noticePage.select(".substance").first();
            Elements img = content.select("img");

            if (!img.isEmpty()) {
                for (Element element : img) {
                    String src = element.attr("src");
                    element.attr("src", URL + src);
                }
            }

                list.add(new Notice(title, content.toString(), date));
        }

        return list;
    }

    private List<String> getURN() {
        Document noticePage = getNoticePage(NOTICE_URN);
        Elements brd_notice = noticePage.select(".brd_notice");
        Elements title = brd_notice.select(".title").select("a");

        List<String> list = new ArrayList<>();
        for (Element element : title) {
            list.add(element.attr("href"));
        }
        return list;
    }

}
