package hello.roommate.advice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

	/**
	 * Validated 검증에 실패한 모든 필드의 에러 메시지를 담아
	 * HttpStatus 코드 400으로 ErrorResult 객체로 반환
	 *
	 * @param e Validated 검증에 실패했을 때 발생한 에러
	 * @return HttpStatus 코드 400으로 ErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResult handleValidationException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException, message= {}", e.getMessage());

		BindingResult bindingResult = e.getBindingResult();
		List<String> errorMessages = bindingResult.getAllErrors()
			.stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.toList());

		String errorMessage = errorMessages.stream()
			.collect(Collectors.joining(", "));

		ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(),
			errorMessage);

		return errorResult;
	}

	/**
	 * JSON 등의 타입 변환이나 읽기 과정에서 발생한 에러를 잡아
	 * Http400코드를 담은 ErrorResult 객체를 반환
	 *
	 * @param e Http 메시지를 읽을 수 없을 때 발생한 에러
	 * @return Http 400 코드를 담은 ErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ErrorResult handleHttpNotReadable(HttpMessageNotReadableException e) {
		log.error("HttpMessageNotReadableException, message= {}", e.getMessage());
		return new ErrorResult(HttpStatus.BAD_REQUEST.value(), "잘못된 타입 값 입니다.");
	}

	/**
	 * NoSuchElementException 발생 시 예외를 처리하는 메서드
	 * Http400코드를 담은 ErrorResult 객체를 반환
	 *
	 * @param e NoSuchElementException 예외
	 * @return Http 400 코드를 담은 ErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoSuchElementException.class)
	public ErrorResult handleNoSuchElementException(NoSuchElementException e) {
		log.error("NoSuchElementException, message={}", e.getMessage());
		return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

	/**
	 * 존재하지 않는 URL 로 요청했을 때 발생하는 예외
	 * @param e NoResourceFoundException
	 * @return Http 400 코드를 담은 ErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoResourceFoundException.class)
	public ErrorResult handleNoResourceFoundException(NoResourceFoundException e) {
		log.error("NoResourceFoundException, message={}", e.getMessage());
		return new ErrorResult(HttpStatus.BAD_REQUEST.value(), "잘못된 요청 입니다.");
	}

	/**
	 * 기타 정의 되지 않은 모든 에러를 잡아
	 * Http 500 코드를 담은 ErrorResult 객체를 반환
	 *
	 * @param e Exception
	 * @return 500에러 코드를 담은 ErrorResult 객체
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResult handleException(Exception e) {
		log.error("UnHandled exception e {}", e);
		return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생했습니다.");
	}
}
