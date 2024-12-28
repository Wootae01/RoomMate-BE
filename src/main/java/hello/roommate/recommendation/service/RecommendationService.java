package hello.roommate.recommendation.service;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import hello.roommate.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationService {

    private final RecommendationRepository recommendRepository;
    private final MemberRepository memberRepository;

    //db에 저장되어있는 유사도 계산 결과 반환
    public List<Recommendation> findRecommendationByMemberId(String memberId) {
        return recommendRepository.findByMemberId(memberId);
    }

    //모든 멤버들의 유사도 계산해서 반환
    public List<RecommendationDto> findLiveRecommendations(String memberId) {
        List<RecommendationDto> list = getRecommendationDto(memberId);
        return list;
    }

    //상위 3명의 멤버만 반환
    public List<RecommendationDto> findLiveRecommendationsTop3(String memberId) {
        List<RecommendationDto> list = getRecommendationDto(memberId);
        return list.subList(0, 3);
    }

    //나와 같은 기숙사인 모든 멤버들의 유사도를 실시간으로 계산하여 리스트로 반환
    private List<RecommendationDto> getRecommendationDto(String memberId) {
        List<RecommendationDto> list = new ArrayList<>();
        Member currentMember = memberRepository.findById(memberId);
        LifeStyle a = currentMember.getLifeStyle();
        Dormitory dorm = currentMember.getDorm();

        List<Member> sameDormMembers = memberRepository.findByDorm(dorm);
        for (Member member : sameDormMembers) {
            if (member.getId().equals(currentMember.getId())) {
                continue;
            }
            LifeStyle b = member.getLifeStyle();
            double score = calCosineSimilarity(a, b);

            RecommendationDto dto = new RecommendationDto();
            dto.setNickname(member.getNickname());
            dto.setScore(Math.round(score*1000)/10.0);
            dto.setAge(member.getLifeStyle().getAge());
            list.add(dto);
        }
        list.sort(Comparator.comparingDouble(RecommendationDto::getScore).reversed());
        return list;
    }

    //나와 같은 기숙사인 모든 멤버들의 유사도를 계산하여 db에 저장
    public void saveAllRecommendations(String memberId) {
        Member currentMember = memberRepository.findById(memberId);
        LifeStyle a = currentMember.getLifeStyle();
        Dormitory dorm = currentMember.getDorm();

        List<Member> sameDormMembers = memberRepository.findByDorm(dorm);
        for (Member member : sameDormMembers) {

            LifeStyle b = member.getLifeStyle();
            double score = calCosineSimilarity(a, b);

            Recommendation recommendation = new Recommendation();
            recommendation.setScore(score);
            recommendRepository.save(recommendation);
        }
    }

    private double calCosineSimilarity(LifeStyle a, LifeStyle b) {
        int[] vectorA = {a.getBedTime(), a.getWakeupTime(), a.getSleepHabit(), a.getCleaning(), a.getAircon(),
                a.getHeater(), a.getNoise(), a.getSmoking(), a.getScent(), a.getEating(),
                a.getRelationship(), a.getDrinking(), a.getAge()};

        int[] vectorB = {b.getBedTime(), b.getWakeupTime(), b.getSleepHabit(), b.getCleaning(), b.getAircon(),
                b.getHeater(), b.getNoise(), b.getSmoking(), b.getScent(), b.getEating(),
                b.getRelationship(), b.getDrinking(), b.getAge()};

        double dotProduct = 0.0;

        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += (vectorA[i] * vectorB[i]);
            magnitudeA += (vectorA[i] * vectorA[i]);
            magnitudeB += (vectorB[i] * vectorB[i]);
        }
        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        return dotProduct / (magnitudeA * magnitudeB);
    }
}
