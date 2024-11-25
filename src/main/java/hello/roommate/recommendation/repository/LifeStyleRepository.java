package hello.roommate.recommendation.repository;

import hello.roommate.recommendation.domain.LifeStyle;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class LifeStyleRepository {
    private final EntityManager em;

    public LifeStyle save(LifeStyle lifeStyle) {
        em.persist(lifeStyle);
        return lifeStyle;
    }
    public LifeStyle findById(Long lifeStyleId) {
        return em.find(LifeStyle.class, lifeStyleId);
    }

    public void update(Long lifeStyleId, LifeStyleUpdateDto dto) {
        LifeStyle lifeStyle = em.find(LifeStyle.class, lifeStyleId);
        lifeStyle.setBedTime(dto.getBedTime());
        lifeStyle.setWakeupTime(dto.getWakeupTime());
        lifeStyle.setSleepHabit(dto.getSleepHabit());
        lifeStyle.setCleaning(dto.getCleaning());
        lifeStyle.setAircon(dto.getAircon());
        lifeStyle.setHeater(dto.getHeater());
        lifeStyle.setNoise(dto.getNoise());
        lifeStyle.setSmoking(dto.getSmoking());
        lifeStyle.setScent(dto.getScent());
        lifeStyle.setEating(dto.getEating());
        lifeStyle.setRelationship(dto.getRelationship());
        lifeStyle.setHome(dto.getHome());
        lifeStyle.setDrinking(dto.getDrinking());
        lifeStyle.setAge(dto.getAge());
        lifeStyle.setDormHour(dto.getDormHour());
    }

    public void delete(Long lifeStyleId) {
        LifeStyle lifeStyle = em.find(LifeStyle.class, lifeStyleId);
        em.remove(lifeStyle);
    }
}
