package hello.roommate.home.external.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class NoticeCrawlerTest {

	NoticeCrawler crawler = new NoticeCrawler();
	private final String URL = "https://dorm.chungbuk.ac.kr";

	@Test
	void getNoticePage() {
		Document noticePage = crawler.getNoticePage();
		log.info(noticePage.toString());
	}

	@Test
	void getURN() {
		Document noticePage = crawler.getNoticePage();
		Elements brd_notice = noticePage.select(".brd_notice");
		Elements title = brd_notice.select(".title").select("a");
		log.info(title.toString());

		for (Element element : title) {
			String href = element.attr("href");
			log.info("link = {}", href);
		}
	}

	@Test
	void getNotices() {
		Document noticePage = crawler.getNoticePage("sub.php?menukey=20039&mod=view&no=454178486&listCnt=20");
		Elements board = noticePage.select(".board_insert");
		String date = board.select("th:contains(등록일) + td").first().text();
		String title = board.select("th[colspan=6]").text();
		log.info("date = {}", date);
		log.info("title = {}", title);

		Element content = noticePage.select(".substance").first();
		Elements img = content.select("img");
		if (!img.isEmpty()) {
			for (Element element : img) {
				String src = element.attr("src");
				element.attr("src", URL + src);
			}
		}

		log.info(content.toString());

	}
}