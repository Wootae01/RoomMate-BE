package hello.roommate.recommendation.domain;

import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.Noise;
import hello.roommate.recommendation.domain.enums.Relationship;
import hello.roommate.recommendation.domain.enums.Scent;
import hello.roommate.recommendation.domain.enums.SleepHabit;
import hello.roommate.recommendation.domain.enums.Smoking;
import hello.roommate.recommendation.domain.enums.WakeUpTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "LIFESTYLE")
public class LifeStyle {
	@Id
	@GeneratedValue
	@Column(name = "LIFESTYLE_ID")
	@Setter(value = AccessLevel.NONE)
	private Long id;

	@Enumerated(EnumType.STRING)
	private BedTime bedTime;        // 취침 시간

	@Enumerated(EnumType.STRING)
	private WakeUpTime wakeupTime;  // 기상 시간

	@Enumerated(EnumType.STRING)
	private SleepHabit sleepHabit;  // 잠버릇

	@Enumerated(EnumType.STRING)
	private Cleaning cleaning;      // 청소 빈도

	@Enumerated(EnumType.STRING)
	private Cooling cooling;         // 냉방

	@Enumerated(EnumType.STRING)
	private Heating heater;         // 난방

	@Enumerated(EnumType.STRING)
	private Noise noise;            // 소리 민감도

	@Enumerated(EnumType.STRING)
	private Smoking smoking;        // 흡연 여부

	@Enumerated(EnumType.STRING)
	private Scent scent;            // 향 민감도

	@Enumerated(EnumType.STRING)
	private Eating eating;    // 실내 취식 여부

	@Enumerated(EnumType.STRING)
	private Relationship relationship; // 관계 발전도

	@Enumerated(EnumType.STRING)
	private Drinking drinking;      // 음주 빈도

	private int age;                // 나이
}
