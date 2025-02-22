package hello.roommate.auth.service;

import hello.roommate.auth.dto.EditMemberDTO;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.member.domain.Member;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.Preference;
import hello.roommate.recommendation.dto.LifeStyleDTO;
import hello.roommate.recommendation.dto.PreferenceDTO;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import hello.roommate.recommendation.repository.OptionRepository;
import hello.roommate.recommendation.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignUpService {

	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;
	private final LifeStyleRepository lifeStyleRepository;
	private final PreferenceRepository preferenceRepository;

	public void registerMember(SignUpDTO request) {
		// 가입되어있는 회원인지 조회
		Member member = memberRepository.findById(request.getUserId())
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 회원 정보 저장
		member.setNickname(request.getNickname());
		member.setGender(request.getGender());
		member.setAge(request.getAge());
		member.setDorm(request.getDormitory());
		member.setIntroduce(request.getIntroduce());

		// 4. 업데이트된 Member 저장
		member = memberRepository.save(member);

		// 3. LifeStyle 및 Preference 저장
		saveLifeStyle(member, request.getLifeStyle());
		savePreference(member, request.getPreference());

	}

	// Member Profile만 수정할경우: EditMemberDTO를 받음
	public void EditMember(EditMemberDTO request) {
		// 가입되어있는 회원인지 조회
		Member member = memberRepository.findById(request.getUserId())
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 회원 정보 저장
		member.setNickname(request.getNickname());
		member.setIntroduce(request.getIntroduce());
		member.setDorm(request.getDormitory());
		member.setAge(request.getAge());
		// 업데이트된 Member 저장
		member = memberRepository.save(member);
	}

	// Member LifeStyle만 수정할경우: LifeStyleDTO를 받음
	public void EditLifeStyle(LifeStyleDTO request) {
		// 1. 가입되어있는 회원인지 조회 후 Member_Id = 회원번호 반환
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 2. LifeStyle 저장
		saveLifeStyle(member, request.getOptions());

	}

	// Member Preference만 수정할경우: PreferenceDTO를 받음
	public void EditPreference(PreferenceDTO request) {
		// 1. 가입되어있는 회원인지 조회 후 Member_Id = 회원번호 반환
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 2. LifeStyle 저장
		savePreference(member, request.getOptions());

	}

	// LifeStyle 저장
	private void saveLifeStyle(Member member, List<Long> optionIds) {
		List<Option> options = optionRepository.findAllById(optionIds);

		List<LifeStyle> lifeStyles = options.stream()
			.map(option -> new LifeStyle(member, option))
			.collect(Collectors.toList());

		lifeStyleRepository.saveAll(lifeStyles);
	}

	// Preference 저장
	private void savePreference(Member member, List<Long> optionIds) {
		List<Option> options = optionRepository.findAllById(optionIds);

		List<Preference> preferences = options.stream()
			.map(option -> new Preference(member, option))
			.collect(Collectors.toList());

		preferenceRepository.saveAll(preferences);
	}
}
// options.stream() -> 리스트 데이터 하나씩 처리
// options:Option을 LifeStyle&Preference로 변환 후 해당 객체에 저장
// .collect(Collectors.toList()) -> map()으로 변환된 객체들을 리스트로 반환
//option = List options에 들어있는 개별 Option 객체
