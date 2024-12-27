package hello.roommate.recommendation.service;

import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.LifeStyleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LifestyleService {
    private final LifeStyleRepository repository;

    public LifeStyle save(LifeStyle lifeStyle) {
        repository.save(lifeStyle);
        return lifeStyle;
    }

    public LifeStyle findById(Long id) {
        return repository.findById(id);
    }

    public void update(Long id, LifeStyleUpdateDto dto) {
        repository.update(id, dto);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
