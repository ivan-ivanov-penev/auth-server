package io.reinno.auth.server.ws.login.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.reinno.auth.server.ws.login.exceptions.CertificateExpiredException;
import io.reinno.auth.server.ws.login.exceptions.UnauthorizedException;
import io.reinno.auth.server.ws.login.user.User;

import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class JwtService {

	private static final String ISSUER = "Reinno Auth-Server";

	private static final String AUDIENCE = "Reinno Web API";

	private static final String ROLE_CLAIM_NAME = "role";

	private final ECPrivateKey privateKey;

	private final X509Certificate certificate;

	private final long jwtValidityMinutes;

	public JwtService(ECPrivateKey privateKey, X509Certificate certificate, long jwtValidityMinutes) {

		this.privateKey = privateKey;
		this.certificate = certificate;
		this.jwtValidityMinutes = jwtValidityMinutes;
	}

	public JwtInfo issueToken(User user) {

		return issueToken(user.id.toString(), user.role.getValue());
	}

	public JwtInfo issueToken(DecodedJWT decodedJwt) {

		return issueToken(decodedJwt.getSubject(), decodedJwt.getClaim(ROLE_CLAIM_NAME).asString());
	}

	public JwtInfo issueToken(String userId, String role) {

		if (isCertificateValid()) {

			return doIssueToken(userId, role);
		} else {

			throw new CertificateExpiredException(certificate.getNotAfter());
		}
	}

	private boolean isCertificateValid() {

		LocalDateTime certificateExpirationDate = new Timestamp(certificate.getNotAfter().getTime()).toLocalDateTime();

		return LocalDateTime.now().isBefore(certificateExpirationDate);
	}

	private JwtInfo doIssueToken(String userId, String role) {

		Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(jwtValidityMinutes));

		String jwtId = UUID.randomUUID().toString();

		JWTCreator.Builder jwtBuilder = JWT.create();
		jwtBuilder.withIssuer(ISSUER);
		jwtBuilder.withAudience(AUDIENCE);
		jwtBuilder.withSubject(userId);
		jwtBuilder.withClaim(ROLE_CLAIM_NAME, role);
		jwtBuilder.withIssuedAt(new Date());
		jwtBuilder.withExpiresAt(expiresAt);
		jwtBuilder.withJWTId(jwtId);

		String rawJwtString = jwtBuilder.sign(createAlgorithm());

		return new JwtInfo(rawJwtString, expiresAt.toInstant().getEpochSecond(), jwtId);
	}

	private Algorithm createAlgorithm() {

		return Algorithm.ECDSA512((ECPublicKey) certificate.getPublicKey(), privateKey);
	}

	public DecodedJWT decodeAndVerify(String jwt) {

		try {

			DecodedJWT decodedJwt = JWT.decode(jwt);

			Algorithm algorithm = createAlgorithm();

			algorithm.verify(decodedJwt);

			return decodedJwt;
		} catch (Exception e) {

			throw new UnauthorizedException(e);
		}
	}
}
