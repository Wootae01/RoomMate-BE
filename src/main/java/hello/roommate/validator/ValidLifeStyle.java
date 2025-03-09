package hello.roommate.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * LifeStyle 검증을 위한 커스텀 애노테이션
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LifeStyleValidator.class)
public @interface ValidLifeStyle {
	String message() default "생활 패턴의 모든 카테고리에 대해 선택한 값이 존재해야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
