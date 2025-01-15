package hello.roommate.recommendation.dto;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeStyleUpdateDto {
	private BedTime bedTime;        //취침 시간
	private WakeUpTime wakeupTime;     //기상 시간
	private SleepHabit sleepHabit;     //잠버릇
	private Cleaning cleaning;       //청소 빈도
	private Cooling cooling;         //에어컨 빈도
	private Heating heater;         //히터 빈도
	private Noise noise;          //소리 민감도
	private Smoking smoking;        //담배 냄새 민감도
	private Scent scent;          //향 민감도
	private Eating eating;         //실내 취식 여부
	private Relationship relationship;   // 관계 발전도
	private Drinking drinking;       //음주 빈도

	private int age;            //나이
}
