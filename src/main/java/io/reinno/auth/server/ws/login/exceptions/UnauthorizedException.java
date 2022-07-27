package io.reinno.auth.server.ws.login.exceptions;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {

		super(message);
	}

	public UnauthorizedException(Exception parentException) {

		super(parentException);
	}
}
