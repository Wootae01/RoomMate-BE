package hello.roommate.recommendation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.roommate.recommendation.dto.RecommendationDto;
import hello.roommate.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {

	private final RecommendationService service;

	@GetMapping("/list/{id}")
	public List<RecommendationDto> findLiveRecommendation(@PathVariable String id) {
		return service.findLiveRecommendations(id);
	}
}
