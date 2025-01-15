package hello.roommate.home.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import hello.roommate.home.domain.Notice;
import hello.roommate.home.external.crawler.NoticeCrawler;
import hello.roommate.home.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
	private final NoticeRepository repository;
	private final NoticeCrawler crawler;

	private static boolean isTitlePresent(List<Notice> notices, String title) {
		for (Notice notice : notices) {
			if (notice.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}

	public List<String> findAllTitles() {
		return repository.findAllTitles();
	}

	public Notice findByTitle(String title) {
		return repository.findByTitle(title).orElseThrow(() -> new NoSuchElementException("find not notice by title"));
	}

	//@Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 0)
	public void updateNotice() {
		List<Notice> notices = crawler.getNotices();
		List<String> existingTitles = repository.findAllTitles();

		//크롤링 했을 때 없는 공지사항이면 추가
		for (Notice notice : notices) {
			if (!existingTitles.contains(notice.getTitle())) {
				repository.save(notice);
			}
		}

		//없어진 공지사항은 삭제
		for (String title : existingTitles) {
			if (!isTitlePresent(notices, title)) {
				repository.deleteByTitle(title);
			}
		}
	}
}
