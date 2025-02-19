package hello.roommate.recommendation.service;

import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;

    public Option findByCategoryAndValue(Category category, String value) {
        return optionRepository.findByCategoryAndValue(category, value);
    }

    public List<Option> findAll() {
        return optionRepository.findAll();
    }
}
