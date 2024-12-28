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
        return repository.findById(id).orElseThrow();
    }

    public void update(Long id, LifeStyleUpdateDto dto) {
        LifeStyle lifeStyle = repository.findById(id).orElseThrow();
        lifeStyle.setAge(dto.getAge());
        lifeStyle.setAircon(dto.getAircon());
        lifeStyle.setBedTime(dto.getBedTime());
        lifeStyle.setCleaning(dto.getCleaning());
        lifeStyle.setDrinking(dto.getDrinking());
        lifeStyle.setEating(dto.getEating());
        lifeStyle.setHeater(dto.getHeater());
        lifeStyle.setNoise(dto.getNoise());
        lifeStyle.setRelationship(dto.getRelationship());
        lifeStyle.setScent(dto.getScent());
        lifeStyle.setSleepHabit(dto.getSleepHabit());
        lifeStyle.setSmoking(dto.getSmoking());
        lifeStyle.setWakeupTime(dto.getWakeupTime());

    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
