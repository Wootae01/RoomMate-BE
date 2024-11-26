package hello.roommate.recommendation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class LifeStyle {
    @Id @GeneratedValue
    @Column(name = "LIFESTYLE_ID")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private int bedTime;        //취침 시간
    private int wakeupTime;     //기상 시간
    private int sleepHabit;     //잠버릇
    private int cleaning;       //청소 빈도
    private int aircon;         //에어컨 빈도
    private int heater;         //히터 빈도
    private int noise;          //소리 민감도
    private int smoking;        //담배 냄새 민감도
    private int scent;          //향 민감도
    private int eating;         //실내 취식 여부
    private int relationship;   // 관계 발전도
    private int home;           // 집 가는 주기
    private int drinking;       //음주 빈도
    private int age;            //나이
    private int dormHour;       //기숙사 거주 시간

}
