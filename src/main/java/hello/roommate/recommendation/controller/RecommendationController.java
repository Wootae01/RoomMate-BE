package hello.roommate.recommendation.controller;

import hello.roommate.recommendation.domain.Recommendation;
import hello.roommate.recommendation.service.RecommendationDto;
import hello.roommate.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
