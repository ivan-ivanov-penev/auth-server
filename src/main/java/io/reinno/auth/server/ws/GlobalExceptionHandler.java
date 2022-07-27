package io.reinno.auth.server.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		LOGGER.warn("Invalid request: {}", e.getMessage());

		return new ErrorMessage(e.getBindingResult().getFieldError().getDefaultMessage());
	}

	private static class ErrorMessage {

		public final String message;

		private ErrorMessage(String message) {

			this.message = message;
		}
	}
}
