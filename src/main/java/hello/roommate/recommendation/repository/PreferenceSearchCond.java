package hello.roommate.recommendation.repository;

import java.util.ArrayList;
import java.util.List;

import hello.roommate.recommendation.domain.enums.BedTime;
import hello.roommate.recommendation.domain.enums.Cleaning;
import hello.roommate.recommendation.domain.enums.Cooling;
import hello.roommate.recommendation.domain.enums.Drinking;
import hello.roommate.recommendation.domain.enums.Eating;
import hello.roommate.recommendation.domain.enums.Heating;
import hello.roommate.recommendation.domain.enums.IndoorCall;
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
public class PreferenceSearchCond {
	private List<BedTime> bedTimes = new ArrayList<>();
	private List<WakeUpTime> wakeUpTimes = new ArrayList<>();
	private List<Cleaning> cleanings = new ArrayList<>();
	private List<Cooling> coolings = new ArrayList<>();
	private List<Heating> heatings = new ArrayList<>();
	private List<Noise> noises = new ArrayList<>();
	private Smoking smoking;
	private List<Scent> scents = new ArrayList<>();
	private List<Eating> eatings = new ArrayList<>();
	private List<Relationship> relationships = new ArrayList<>();
	private List<Drinking> drinkings = new ArrayList<>();
	private List<Integer> ages = new ArrayList<>();
	private List<IndoorCall> indoorCalls = new ArrayList<>();
	private SleepHabit sleepHabit;
}
