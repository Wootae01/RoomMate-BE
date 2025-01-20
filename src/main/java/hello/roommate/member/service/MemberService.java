package hello.roommate.member.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository repository;
	private final MemberRepository memberRepository;

	public Member save(Member member) {
		repository.save(member);
		return member;
	}

	public Member findById(String id) {
		return repository.findById(id).orElseThrow();
	}

	public List<ChatRoom> findAllChatRooms(String memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		List<MemberChatRoom> memberChatRooms = member.getMemberChatRooms();
		List<ChatRoom> chatRooms = new ArrayList<>();
		for (MemberChatRoom memberChatRoom : memberChatRooms) {
			chatRooms.add(memberChatRoom.getChatRoom());
		}

		Collections.sort(chatRooms, (c1, c2) -> c2.getUpdatedTime().compareTo(c1.getUpdatedTime()));
		return chatRooms;
	}

	public List<Member> findByDorm(Dormitory dorm) {
		return repository.findByDorm(dorm);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}

	/*public List<Member> searchMember(String id) {
		Member member = memberRepository.findById(id).orElseThrow();
		LifeStyle lifeStyle = member.getLifeStyle();
		List<Preference> preference = member.getPreference();
		PreferenceSearchCond cond = preferenceToCond(preference);

		return memberRepository.search(member, cond);
	}

	private PreferenceSearchCond preferenceToCond(List<Preference> preferences) {
		PreferenceSearchCond cond = new PreferenceSearchCond();

		for (Preference preference : preferences) {
			switch (preference.getCategory()) {
				case BED_TIME -> cond.addBedTime(BedTime.valueOf(preference.getOptionValue()));
				case WAKEUP_TIME -> cond.addWakeUpTime(WakeUpTime.valueOf(preference.getOptionValue()));
				case CLEANING -> cond.addCleaning(Cleaning.valueOf(preference.getOptionValue()));
				case COOLING -> cond.addCooling(Cooling.valueOf(preference.getOptionValue()));
				case HEATING -> cond.addHeating(Heating.valueOf(preference.getOptionValue()));
				case NOISE -> cond.addNoise(Noise.valueOf(preference.getOptionValue()));
				case SMOKING -> cond.setSmoking(Smoking.valueOf(preference.getOptionValue()));
				case SCENT -> cond.addScent(Scent.valueOf(preference.getOptionValue()));
				case EATING -> cond.addEating(Eating.valueOf(preference.getOptionValue()));
				case RELATIONSHIP -> cond.addRelationship(Relationship.valueOf(preference.getOptionValue()));
				case DRINKING -> cond.addDrinking(Drinking.valueOf(preference.getOptionValue()));
				case AGE -> cond.addAge(Integer.valueOf(preference.getOptionValue()));
				case IndoorCall -> cond.addIndoorCall(IndoorCall.valueOf(preference.getOptionValue()));
				case SLEEP_HABIT -> cond.setSleepHabit(SleepHabit.valueOf(preference.getOptionValue()));
				default -> throw new IllegalArgumentException("Unknown category: " + preference.getCategory());
			}
		}
		return cond;
	}*/
}
