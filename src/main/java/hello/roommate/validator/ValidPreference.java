package hello.roommate.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Preference 검증을 위한 커스텀 애노테이션
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PreferenceValidator.class)
public @interface ValidPreference {
	String message() default "선호 하는 룸메의 모든 카테고리에 대해 선택한 값이 존재해야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
