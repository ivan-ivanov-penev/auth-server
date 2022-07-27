package io.reinno.auth.server.ws.login.jwt;

import io.reinno.auth.server.config.AppConfig;

public class JwtServiceCreator {

	private static final String KEYSTORE_LOCATION = "private.p12";

	private static final String KEYSTORE_PASSWORD = "pass123";

	private static final String KEYSTORE_ALIAS = "auth-server";

	private static final int DEFAULT_JWT_VALIDITY_MINUTES = 5;

	public static JwtService create() {

		return create(KEYSTORE_LOCATION, KEYSTORE_PASSWORD, KEYSTORE_ALIAS, DEFAULT_JWT_VALIDITY_MINUTES);
	}

	public static JwtService create(String keystoreLocationInResources, String keystorePassword, String keystoreAlias) {

		return create(keystoreLocationInResources, keystorePassword, keystoreAlias, DEFAULT_JWT_VALIDITY_MINUTES);
	}

	public static JwtService create(
			String keystoreLocationInResources, String keystorePassword, String keystoreAlias, int jwtValidityMinutes) {

		try {

			return new AppConfig().jwtService(
					JwtServiceCreator.class.getClassLoader().getResource(keystoreLocationInResources).getFile(),
					keystorePassword.toCharArray(),
					keystoreAlias,
					jwtValidityMinutes);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}
}
