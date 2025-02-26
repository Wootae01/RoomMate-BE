package hello.roommate.member.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hello.roommate.recommendation.domain.enums.Category;
import lombok.Getter;
import lombok.Setter;

/**
 * 필터 조건 정보를 담을 클래스
 */
@Getter
@Setter
public class FilterCond {

	private Map<Category, List<Long>> cond = new HashMap<>();

}
