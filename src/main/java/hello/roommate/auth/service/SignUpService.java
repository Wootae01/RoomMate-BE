package hello.roommate.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.advice.DuplicatedNicknameException;
import hello.roommate.auth.dto.EditMemberDTO;
import hello.roommate.auth.dto.LoginResponseDTO;
import hello.roommate.auth.dto.SignUpDTO;
import hello.roommate.auth.jwt.JWTConstants;
import hello.roommate.auth.jwt.JWTUtil;
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

@Service
@RequiredArgsConstructor
public class SignUpService {

	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;
	private final LifeStyleRepository lifeStyleRepository;
	private final PreferenceRepository preferenceRepository;
	private final JWTUtil jwtUtil;

	public LoginResponseDTO createLoginResponse(Member member) {
		LoginResponseDTO dto = new LoginResponseDTO();
		dto.setMemberId(member.getId());
		dto.setIsFirstLogin(member.getNickname() == null);

		String accessToken = jwtUtil.createJwt(member.getUsername(), "ROLE_USER", "access",
			JWTConstants.ACCESS_TOKEN_EXPIRATION);
		String refreshToken = jwtUtil.createJwt(member.getUsername(), "ROLE_USER", "refresh",
			JWTConstants.REFRESH_TOKEN_EXPIRATION);

		dto.setAccessToken(accessToken);
		dto.setRefreshToken(refreshToken);

		return dto;
	}

	@Transactional
	public Member createNewMember(String username) {
		Member member = new Member();
		member.setUsername(username);
		return memberRepository.save(member);
	}

	@Transactional
	public void registerMember(SignUpDTO request) {
		// 가입되어있는 회원인지 조회
		Member member = memberRepository.findById(request.getUserId())
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 회원 정보 저장
		// 존재하는 닉네임이면 에러 발생
		member.setNickname(request.getNickname());
		member.setGender(request.getGender());
		member.setAge(request.getAge());
		member.setDorm(request.getDormitory());
		member.setIntroduce(request.getIntroduce());

		// 4. 업데이트된 Member 저장
		try {
			member = memberRepository.save(member);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicatedNicknameException("이미 사용중인 닉네임입니다.");
		}

		// 3. LifeStyle 및 Preference 저장
		saveLifeStyle(member, request.getLifeStyle());
		savePreference(member, request.getPreference());

	}

	// Member Profile만 수정할경우: EditMemberDTO를 받음
	@Transactional
	public void editMember(EditMemberDTO request, Long memberId) {
		// 가입되어있는 회원인지 조회
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 회원 정보 저장
		member.setNickname(request.getNickname());
		member.setIntroduce(request.getIntroduce());
		member.setDorm(request.getDormitory());
		member.setAge(request.getAge());
		member.setGender(request.getGender());

		// 업데이트된 Member 저장
		memberRepository.save(member);
	}

	// Member LifeStyle만 수정할경우: LifeStyleDTO를 받음
	@Transactional
	public void editLifeStyle(LifeStyleDTO request, Long memberId) {
		// 1. 가입되어있는 회원인지 조회 후 Member_Id = 회원번호 반환
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 기존꺼 삭제
		lifeStyleRepository.deleteByMemberId(memberId);
		// 2. LifeStyle 저장
		saveLifeStyle(member, request.getOptions());

	}

	// Member Preference만 수정할경우: PreferenceDTO를 받음
	@Transactional
	public void editPreference(PreferenceDTO request, Long memberId) {
		// 1. 가입되어있는 회원인지 조회 후 Member_Id = 회원번호 반환
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

		// 기존꺼 삭제
		preferenceRepository.deleteByMemberId(memberId);

		// 2. LifeStyle 저장
		savePreference(member, request.getOptions());

	}

	// LifeStyle 저장

	private void saveLifeStyle(Member member, Map<String, List<Long>> lifeStyle) {
		List<Option> allOptions = new ArrayList<>();

		// Map<String, List<Long>> 구조를 List<Long>으로 변환
		// map.values()를 사용해서 List<Long>을 collection으로 반환.
		for (List<Long> optionIds : lifeStyle.values()) {
			allOptions.addAll(optionRepository.findAllById(optionIds));
		}

		List<LifeStyle> lifeStyles = allOptions.stream()
			.map(option -> new LifeStyle(member, option)) //option: alloptions.stream의 Option 객체
			//각 Option 객체에 대해, new LifeStyle(member, option)를 호출하여 LifeStyle 객체를 생성
			.collect(Collectors.toList());
		//생성된 모든 LifeStyle 객체를 lifeStyles 리스트에 추가

		lifeStyleRepository.saveAll(lifeStyles);
	}

	// Preference 저장
	private void savePreference(Member member, Map<String, List<Long>> preference) {
		List<Option> allOptions = new ArrayList<>();

		for (List<Long> optionIds : preference.values()) {
			allOptions.addAll(optionRepository.findAllById(optionIds));
		}

		List<Preference> preferences = allOptions.stream()
			.map(option -> new Preference(member, option))
			.collect(Collectors.toList());

		preferenceRepository.saveAll(preferences);
	}

}

