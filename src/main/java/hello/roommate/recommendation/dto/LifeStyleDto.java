package hello.roommate.recommendation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class LifeStyleDto {
    private Long memberId;
    private List<String> bedTimes;
    private List<String> wakeUpTimes;
    private List<String> heatings;
    private List<String> coolings;

    private String noise;
    private String indoorCall;
    private String scent;
    private String eating;
    private String drinking;
    private String cleaning;
    private String relationship;
    private String sleepHabit;
    private String smoking;

    public LifeStyleDto(String bedTimes, String wakeUpTimes, String coolings,
                        String heatings, String cleaning, String noise, String scent, String drinking,
                        String relationship,
                        String eating, String indoorCall, String sleepHabit, String smoking) {
        this.bedTimes = parseStringToList(bedTimes);
        this.wakeUpTimes = parseStringToList(wakeUpTimes);
        this.coolings = parseStringToList(coolings);
        this.heatings = parseStringToList(heatings);
        this.cleaning = cleaning;
        this.noise = noise;
        this.scent = scent;
        this.drinking = drinking;
        this.relationship = relationship;
        this.eating = eating;
        this.indoorCall = indoorCall;
        this.sleepHabit = sleepHabit;
        this.smoking = smoking;

    }

    private List<String> parseStringToList(String str) {
        if (str == null) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(str.split(", "));
        }

    }

}
