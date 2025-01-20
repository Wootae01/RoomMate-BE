package hello.roommate.home.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.home.domain.Notice;
import hello.roommate.home.repository.WeatherDto;
import hello.roommate.home.service.NoticeService;
import hello.roommate.home.service.WeatherService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HomeController {

	private final NoticeService noticeService;
	private final WeatherService weatherService;

	@GetMapping("/notice")
	public ResponseEntity<List<String>> allNotice() {
		List<String> titles = noticeService.findAllTitles();
		return new ResponseEntity<>(titles, HttpStatus.OK);
	}

	@GetMapping("/notice/{title}")
	public Notice notice(@PathVariable String title) {
		return noticeService.findByTitle(title);
	}

	@GetMapping("/weather")
	public WeatherDto getCurrentWeather() {
		return weatherService.getCurrentWeather();
	}

}
