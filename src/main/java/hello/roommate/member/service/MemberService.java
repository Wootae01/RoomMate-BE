package hello.roommate.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.roommate.chat.domain.ChatRoom;
import hello.roommate.member.domain.Dormitory;
import hello.roommate.member.domain.Member;
import hello.roommate.member.domain.MemberChatRoom;
import hello.roommate.member.dto.MemberDTO;
import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.domain.Option;
import hello.roommate.recommendation.domain.enums.Category;
import hello.roommate.recommendation.dto.OptionDTO;
import hello.roommate.recommendation.repository.OptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
	private final MemberRepository repository;
	private final MemberRepository memberRepository;
	private final OptionRepository optionRepository;

	public Member save(Member member) {
		return repository.save(member);
	}

	public Member findById(Long id) {
		return repository.findById(id).orElseThrow();
	}

	public List<ChatRoom> findAllChatRooms(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		List<MemberChatRoom> memberChatRooms = member.getMemberChatRooms();
		List<ChatRoom> chatRooms = new ArrayList<>();
		for (MemberChatRoom memberChatRoom : memberChatRooms) {
			chatRooms.add(memberChatRoom.getChatRoom());
		}

		return chatRooms;
	}

	public List<Member> findByDorm(Dormitory dorm) {
		return repository.findByDorm(dorm);
	}

	public Member findByNickname(String nickname) {
		return repository.findByNickname(nickname).orElseThrow(() -> new NoSuchElementException("Member not found"));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public List<Member> recommendMembers(Long myId) {
		return repository.recommendMembers(myId);
	}

	public List<Member> searchMembers(Long myId, List<OptionDTO> dto) {
		if (dto.isEmpty() || dto == null) {
			return repository.recommendMembers(myId);
		}

		List<Long> cond = new ArrayList<>();
		for (OptionDTO optionDto : dto) {
			Category category = Category.valueOf(optionDto.getCategory());
			String optionValue = optionDto.getOption_value();
			Option option = optionRepository.findByCategoryAndValue(category, optionValue);
			cond.add(option.getId());
		}
		return memberRepository.searchMembers(myId, cond, cond.size());
	}

	//dto로 전환
	public MemberDTO convertToDTO(Member member) {
		MemberDTO dto = new MemberDTO();
		dto.setMemberId(member.getId());
		dto.setNickname(member.getNickname());
		dto.setIntroduce(member.getIntroduce());
		return dto;
	}
}
