package io.reinno.auth.server.config;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import io.reinno.auth.server.ws.login.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.util.List;

@Configuration
public class AppConfig {

	private static final String KEYSTORE_INSTANCE = "PKCS12";

	@Bean
	public WebMvcConfigurer corsConfigurer(
			@Value("${cors.path.pattern}") String pathPattern,
			@Value("${cors.allowed.origins:}") List<String> allowedOrigins,
			@Value("${cors.allowed.methods:}") List<String> allowedMethods,
			@Value("${cors.allowed.headers:}") List<String> allowedHeaders,
			@Value("${cors.allow.credentials:false}") boolean allowCredentials) {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				CorsRegistration corsRegistration = registry.addMapping(pathPattern).allowCredentials(allowCredentials);

				allowedOrigins.forEach(corsRegistration::allowedOrigins);
				allowedMethods.forEach(corsRegistration::allowedMethods);
				allowedHeaders.forEach(corsRegistration::allowedHeaders);
			}
		};
	}

	@Bean
	public JwtService jwtService(
			@Value("${jwt.keystore.location}") String keyStoreLocation,
			@Value("${jwt.keystore.password}") char[] keyStorePassword,
			@Value("${jwt.keystore.alias}") String keyStoreAlias,
			@Value("${jwt.validity.minutes}") long jwtValidityMinutes) throws Exception {

		try (FileInputStream stream = new FileInputStream(keyStoreLocation)) {

			KeyStore keyStore = KeyStore.getInstance(KEYSTORE_INSTANCE);
			keyStore.load(stream, keyStorePassword);

			ECPrivateKey privateKey = (ECPrivateKey) keyStore.getKey(keyStoreAlias, keyStorePassword);

			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(keyStoreAlias);

			return new JwtService(privateKey, certificate, jwtValidityMinutes);
		}
	}

	@Bean
	public CodeVerifier mfaCodeVerifier(
			@Value("${mfa.allowed.discrepancy}") int mfaAllowedDiscrepancy,
			@Value("${mfa.time.period.seconds}") int mfaTimePeriodSeconds) {

		TimeProvider timeProvider = new SystemTimeProvider();

		CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);

		DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
		verifier.setAllowedTimePeriodDiscrepancy(mfaAllowedDiscrepancy);
		verifier.setTimePeriod(mfaTimePeriodSeconds);

		return verifier;
	}
}
