package emlakcepte.controller.advice;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import emlakcepte.exception.EmlakCepteException;
import emlakcepte.exception.ExceptionResponse;
import emlakcepte.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handle(UserNotFoundException exception) {
		//String message = messageSource.getMessage(exception.getMessage(), null, new Locale("tr"));
		return ResponseEntity.ok(new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(EmlakCepteException.class)
	public ResponseEntity<ExceptionResponse> handle(EmlakCepteException exception) {
		String message = messageSource.getMessage(exception.getKey(), null, new Locale("tr"));	
		return ResponseEntity.ok(new ExceptionResponse(message, HttpStatus.BAD_REQUEST));
	}

}
