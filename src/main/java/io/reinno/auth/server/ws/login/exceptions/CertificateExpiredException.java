package io.reinno.auth.server.ws.login.exceptions;

import java.util.Date;

public class CertificateExpiredException extends RuntimeException {

	public CertificateExpiredException(Date notAfter) {

		super("Certificate has expired! Not-After: " + notAfter);
	}
}
