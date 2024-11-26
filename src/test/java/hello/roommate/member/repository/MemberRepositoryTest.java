package hello.roommate.member.repository;

import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private LifeStyleRepository lifeStyleRepository;
    @Test
    void save() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);
        Member member = createMember(lifeStyle);

        //when
        Member save = memberRepository.save(member);

        //then
        assertThat(save).isEqualTo(member);
        assertThat(save.getLifeStyle()).isEqualTo(member.getLifeStyle());
    }

    @Test
    void findById() {
        //given
        LifeStyle lifeStyle = createLifeStyle();
        lifeStyleRepository.save(lifeStyle);
        Member member = createMember(lifeStyle);
        memberRepository.save(member);

        //when
        Member find = memberRepository.findById(member.getId());
        assertThat(find).isEqualTo(member);
    }

    @Test
    void findByDorm() {
        Member member1 = createMember("1245", Dormitory.INUI, "abc", new Date(), null);
        Member member2 = createMember("1345", Dormitory.INUI, "abcd", new Date(), null);
        Member member3 = createMember("1347", Dormitory.YEJI, "abce", new Date(), null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        List<Member> inui = memberRepository.findByDorm(Dormitory.INUI);
        List<Member> yeji = memberRepository.findByDorm(Dormitory.YEJI);

        //then
        assertThat(inui.size()).isEqualTo(2);
        assertThat(yeji.size()).isEqualTo(1);
        assertThat(inui).contains(member1, member2);
        assertThat(yeji).contains(member3);
    }

    @Test
    void delete() {
        //given
        Member member = createMember(null);
        memberRepository.save(member);

        //when
        memberRepository.delete(member.getId());
        Member find = memberRepository.findById(member.getId());

        //then
        assertThat(find).isNull();
    }

    private Member createMember(String id, Dormitory dorm, String nickname, Date time, LifeStyle lifeStyle) {
        Member member = new Member();
        member.setId(id);
        member.setDorm(dorm);
        member.setLifeStyle(lifeStyle);
        member.setNickname(nickname);
        member.setTimestamp(time);
        return member;
    }
    private Member createMember(LifeStyle lifeStyle) {
        Member member = new Member();
        member.setId("1234");
        member.setDorm(Dormitory.INUI);
        member.setLifeStyle(lifeStyle);
        member.setNickname("nickname");
        member.setTimestamp(new Date());
        return member;
    }
    private LifeStyle createLifeStyle() {
        LifeStyle lifeStyle = new LifeStyle();
        lifeStyle.setBedTime(5);
        lifeStyle.setWakeupTime(5);
        lifeStyle.setSleepHabit(4);
        lifeStyle.setCleaning(4);
        lifeStyle.setAircon(4);
        lifeStyle.setHeater(3);
        lifeStyle.setNoise(2);
        lifeStyle.setSmoking(4);
        lifeStyle.setScent(5);
        lifeStyle.setEating(4);
        lifeStyle.setRelationship(2);
        lifeStyle.setHome(5);
        lifeStyle.setDrinking(5);
        lifeStyle.setAge(4);
        lifeStyle.setDormHour(3);
        return lifeStyle;
    }
}