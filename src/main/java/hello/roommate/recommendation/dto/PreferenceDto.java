package hello.roommate.recommendation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PreferenceDto {
    private List<String> bedTimes = new ArrayList<>();
    private List<String> wakeUpTimes = new ArrayList<>();
    private List<String> cleanings = new ArrayList<>();
    private List<String> coolings = new ArrayList<>();
    private List<String> heatings = new ArrayList<>();
    private List<String> noises = new ArrayList<>();
    private List<String> scents = new ArrayList<>();
    private List<String> eatings = new ArrayList<>();
    private List<String> relationships = new ArrayList<>();
    private List<String> drinkings = new ArrayList<>();
    private List<String> ages = new ArrayList<>();
    private List<String> indoorCalls = new ArrayList<>();

    private String sleepHabit;
    private String smoking;

    public PreferenceDto(String bedTimes, String wakeUpTimes, String coolings,
                         String heatings, String cleaning, String noise, String scent, String drinking,
                         String relationship,
                         String eating, String indoorCall, String sleepHabit, String smoking) {
        this.bedTimes = parseStringToList(bedTimes);
        this.wakeUpTimes = parseStringToList(wakeUpTimes);
        this.coolings = parseStringToList(coolings);
        this.heatings = parseStringToList(heatings);
        this.cleanings = parseStringToList(cleaning);
        this.noises = parseStringToList(noise);
        this.scents = parseStringToList(scent);
        this.drinkings = parseStringToList(drinking);
        this.relationships = parseStringToList(relationship);
        this.eatings = parseStringToList(eating);
        this.indoorCalls = parseStringToList(indoorCall);
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
