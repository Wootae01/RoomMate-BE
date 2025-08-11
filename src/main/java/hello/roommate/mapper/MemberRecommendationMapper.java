package hello.roommate.mapper;

import hello.roommate.member.domain.Member;
import hello.roommate.member.dto.RecommendMemberDTO;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.domain.enums.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemberRecommendationMapper {
    // 상대방 회원번호를 통해 얻은 List<LifeStyle>을 Map<String, List<Long>>으로 변환
    public Map<String, List<Long>> convertLifeStyleListToMap(List<LifeStyle> lifeStyleList) {
        List<Option> options = lifeStyleList.stream()
                .map(LifeStyle::getOption)
                .toList();            // friend의 LifeStyle의 Option만 가져와 List<Option>에 저장

        Map<String, List<Long>> collect = options.stream()
                .collect(Collectors.groupingBy(        // Map 객체 리턴
                        option -> option.getCategory().name(),
                        Collectors.mapping(Option::getId, Collectors.toList())
                ));
        // Option클래스의 getCategory : String을 key로 설정
        // Collectors.toList()를 통해 List를 생성하고 List<T>에서 T를 Long으로 설정하고 List<Long>안에 Option_id를 담음

        return collect;
    }

    // 상대방 회원번호를 통해 얻은 List<Preference>을 Map<String, List<Long>>으로 변환
    public Map<String, List<Long>> convertPreferenceListToMap(List<Preference> preferenceList) {
        List<Option> options = preferenceList.stream()
                .map(Preference::getOption)
                .toList();            // friend의 LifeStyle의 Option만 가져와 List<Option>에 저장

        Map<String, List<Long>> collect = options.stream()
                .collect(Collectors.groupingBy(        // Map 객체 리턴
                        option -> option.getCategory().name(),
                        Collectors.mapping(Option::getId, Collectors.toList())
                ));

        return collect;
    }

    //상관 없음 체크한 항목 제외한 옵션 추출
    public Map<Category, List<Long>> convertPreferenceListToMapWithoutNone(List<Preference> preferences) {
        List<Option> options = preferences
                .stream()
                .filter(preference -> preference.getOption().getId() > 100)
                .map(Preference::getOption)
                .toList();

        Map<Category, List<Long>> cond = options.stream()
                .collect(
                        Collectors.groupingBy(
                                option -> option.getCategory(),
                                Collectors.mapping(Option::getId, Collectors.toList())
                        ));
        return cond;
    }

    //dto로 전환
    public RecommendMemberDTO convertToDTO(Member member) {
        RecommendMemberDTO dto = new RecommendMemberDTO();
        dto.setMemberId(member.getId());
        dto.setNickname(member.getNickname());
        dto.setIntroduce(member.getIntroduce());
        return dto;
    }
}
