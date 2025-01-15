package hello.roommate.recommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.QPreference;
import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.IndoorCall;
import hello.roommate.recommendation.domain.enums.Noise;
import hello.roommate.recommendation.domain.enums.Relationship;
import hello.roommate.recommendation.domain.enums.Scent;
import hello.roommate.recommendation.domain.enums.WakeUpTime;

public class PreferenceRepositoryImpl extends QuerydslRepositorySupport implements PreferenceRepositoryCustom {

	public PreferenceRepositoryImpl() {
		super(Preference.class);
	}

	@Override
	public List<Preference> search(PreferenceSearchCond cond) {
		QPreference preference = QPreference.preference;
		JPQLQuery<Preference> query = from(preference);

		BooleanBuilder builder = new BooleanBuilder();

		// BedTime 조건 추가
		if (!cond.getBedTimes().isEmpty()) {
			BooleanBuilder bedTimeConditions = new BooleanBuilder();
			for (BedTime bedTime : cond.getBedTimes()) {
				bedTimeConditions.or(preference.category.eq(Category.BED_TIME)
					.and(preference.value.eq(bedTime.name())));
			}
			builder.and(bedTimeConditions);
		}

		// WakeUpTime 조건 추가
		if (!cond.getWakeUpTimes().isEmpty()) {
			BooleanBuilder wakeUpTimeConditions = new BooleanBuilder();
			for (WakeUpTime wakeUpTime : cond.getWakeUpTimes()) {
				wakeUpTimeConditions.or(preference.category.eq(Category.WAKEUP_TIME)
					.and(preference.value.eq(wakeUpTime.name())));
			}
			builder.and(wakeUpTimeConditions);
		}

		// Cleaning 조건 추가
		if (!cond.getCleanings().isEmpty()) {
			BooleanBuilder cleaningConditions = new BooleanBuilder();
			for (Cleaning cleaning : cond.getCleanings()) {
				cleaningConditions.or(preference.category.eq(Category.CLEANING)
					.and(preference.value.eq(cleaning.name())));
			}
			builder.and(cleaningConditions);
		}

		// Cooling 조건 추가
		if (!cond.getCoolings().isEmpty()) {
			BooleanBuilder coolingConditions = new BooleanBuilder();
			for (Cooling cooling : cond.getCoolings()) {
				coolingConditions.or(preference.category.eq(Category.COOLING)
					.and(preference.value.eq(cooling.name())));
			}
			builder.and(coolingConditions);
		}

		// Heating 조건 추가
		if (!cond.getHeatings().isEmpty()) {
			BooleanBuilder heatingConditions = new BooleanBuilder();
			for (Heating heating : cond.getHeatings()) {
				heatingConditions.or(preference.category.eq(Category.HEATING)
					.and(preference.value.eq(heating.name())));
			}
			builder.and(heatingConditions);
		}

		// Noise 조건 추가
		if (!cond.getNoises().isEmpty()) {
			BooleanBuilder noiseConditions = new BooleanBuilder();
			for (Noise noise : cond.getNoises()) {
				noiseConditions.or(preference.category.eq(Category.NOISE)
					.and(preference.value.eq(noise.name())));
			}
			builder.and(noiseConditions);
		}

		// Scent 조건 추가
		if (!cond.getScents().isEmpty()) {
			BooleanBuilder scentConditions = new BooleanBuilder();
			for (Scent scent : cond.getScents()) {
				scentConditions.or(preference.category.eq(Category.SCENT)
					.and(preference.value.eq(scent.name())));
			}
			builder.and(scentConditions);
		}

		// Smoking 조건 추가
		if (cond.getSmoking() != null) {
			builder.and(preference.category.eq(Category.SMOKING)
				.and(preference.value.eq(cond.getSmoking().name())));
		}

		// SleepHabit 조건 추가
		if (cond.getSleepHabit() != null) {
			builder.and(preference.category.eq(Category.SLEEP_HABIT)
				.and(preference.value.eq(cond.getSleepHabit().name())));
		}

		// Age 조건 추가 (Integer 값)
		if (!cond.getAges().isEmpty()) {
			BooleanBuilder ageConditions = new BooleanBuilder();
			for (Integer age : cond.getAges()) {
				ageConditions.or(preference.category.eq(Category.AGE)
					.and(preference.value.eq(String.valueOf(age))));
			}
			builder.and(ageConditions);
		}

		//IndoorCall 조건 추가
		if (!cond.getIndoorCalls().isEmpty()) {
			BooleanBuilder indoorCallConditions = new BooleanBuilder();
			for (IndoorCall call : cond.getIndoorCalls()) {
				indoorCallConditions.or(preference.category.eq(Category.IndoorCall)
					.and(preference.value.eq(call.name())));
			}
			builder.and(indoorCallConditions);
		}

		// Eating 조건 추가
		if (!cond.getEatings().isEmpty()) {
			BooleanBuilder eatingConditions = new BooleanBuilder();
			for (Eating eating : cond.getEatings()) {
				eatingConditions.or(preference.category.eq(Category.EATING)
					.and(preference.value.eq(eating.name())));
			}
			builder.and(eatingConditions);
		}

		// Relationship 조건 추가
		if (!cond.getRelationships().isEmpty()) {
			BooleanBuilder relationshipConditions = new BooleanBuilder();
			for (Relationship relationship : cond.getRelationships()) {
				relationshipConditions.or(preference.category.eq(Category.RELATIONSHIP)
					.and(preference.value.eq(relationship.name())));
			}
			builder.and(relationshipConditions);
		}

		// Drinking 조건 추가
		if (!cond.getDrinkings().isEmpty()) {
			BooleanBuilder drinkingConditions = new BooleanBuilder();
			for (Drinking drinking : cond.getDrinkings()) {
				drinkingConditions.or(preference.category.eq(Category.DRINKING)
					.and(preference.value.eq(drinking.name())));
			}
			builder.and(drinkingConditions);
		}

		// 최종 쿼리 실행
		query.where(builder);

		return query.fetch();
	}

}
