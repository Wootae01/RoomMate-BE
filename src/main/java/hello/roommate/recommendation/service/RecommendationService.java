package hello.roommate.recommendation.service;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Recommendation;
import hello.roommate.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendRepository;
    private final MemberRepository memberRepository;

    //db에 저장되어있는 유사도 계산 결과 반환
    public List<Recommendation> findRecommendationByMemberId(String memberId) {
        return recommendRepository.findByMemberId(memberId);
    }

    //나와 같은 기숙사인 모든 멤버들의 유사도를 실시간으로 계산하여 리스트로 반환
    public List<Recommendation> findLiveRecommendations(String memberId) {
        List<Recommendation> list = new ArrayList<>();
        Member currentMember = memberRepository.findById(memberId);
        LifeStyle a = currentMember.getLifeStyle();
        String dorm = currentMember.getDorm().name();

        List<Member> sameDormMembers = memberRepository.findByDorm(dorm);
        for (Member member : sameDormMembers) {

            LifeStyle b = member.getLifeStyle();
            double score = calCosineSimilarity(a, b);

            Recommendation recommendation = new Recommendation();
            recommendation.setMember(currentMember);
            recommendation.setMatchedMember(member);
            recommendation.setScore(score);
            list.add(recommendation);
        }
        return list;
    }

    //나와 같은 기숙사인 모든 멤버들의 유사도를 계산하여 db에 저장
    public void saveAllRecommendations(String memberId) {
        Member currentMember = memberRepository.findById(memberId);
        LifeStyle a = currentMember.getLifeStyle();
        String dorm = currentMember.getDorm().name();

        List<Member> sameDormMembers = memberRepository.findByDorm(dorm);
        for (Member member : sameDormMembers) {

            LifeStyle b = member.getLifeStyle();
            double score = calCosineSimilarity(a, b);

            Recommendation recommendation = new Recommendation();
            recommendation.setMember(currentMember);
            recommendation.setMatchedMember(member);
            recommendation.setScore(score);
            recommendRepository.save(recommendation);
        }
    }

    private double calCosineSimilarity(LifeStyle a, LifeStyle b) {
        int[] vectorA = {a.getBedTime(), a.getWakeupTime(), a.getSleepHabit(), a.getCleaning(), a.getAircon(),
                a.getHeater(), a.getNoise(), a.getSmoking(), a.getScent(), a.getEating(),
                a.getRelationship(), a.getHome(), a.getDrinking(), a.getAge(), a.getDormHour()};

        int[] vectorB = {b.getBedTime(), b.getWakeupTime(), b.getSleepHabit(), b.getCleaning(), b.getAircon(),
                b.getHeater(), b.getNoise(), b.getSmoking(), b.getScent(), b.getEating(),
                b.getRelationship(), b.getHome(), b.getDrinking(), b.getAge(), b.getDormHour()};

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
