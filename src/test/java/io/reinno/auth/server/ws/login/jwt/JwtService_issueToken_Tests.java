package io.reinno.auth.server.ws.login.jwt;

import io.reinno.auth.server.ws.login.exceptions.CertificateExpiredException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.*;

public class JwtService_issueToken_Tests {

	private JwtService sut;

	@Test
	public void Should_ReturnValidTokenInfo_When_NonBlankArgumentsAreProvided() {

		String userId = "my.email@reinno.io";
		String userRole = "SuperRole";

		sut = JwtServiceCreator.create();

		JwtInfo jwtInfo = sut.issueToken(userId, userRole);

		validateJwtInfo(userId, userRole, jwtInfo);
	}

	private void validateJwtInfo(String userId, String userRole, JwtInfo jwtInfo) {

		assertNotNull(jwtInfo);

		assertTrue(StringUtils.isNotBlank(jwtInfo.id));

		assertNotNull(jwtInfo.tokenInfo);
		assertTrue(jwtInfo.tokenInfo.expiresAt > 0);

		assertTrue(StringUtils.isNotBlank(jwtInfo.tokenInfo.value));

		validateJwtContent(userId, userRole, jwtInfo.id, jwtInfo.tokenInfo.expiresAt, jwtInfo.tokenInfo.value);
	}

	private void validateJwtContent(String userId, String userRole, String jti, long expiresAt, String jwt) {

		String[] jwtParts = jwt.split("\\.");

		assertEquals(3, jwtParts.length);
		assertTrue(StringUtils.isNotBlank(jwtParts[0]));
		assertTrue(StringUtils.isNotBlank(jwtParts[1]));
		assertTrue(StringUtils.isNotBlank(jwtParts[2]));

		String jwtPayload = new String(Base64.getDecoder().decode(jwtParts[1]));

		assertTrue(jwtPayload.contains("\"sub\":\"" + userId + "\""));
		assertTrue(jwtPayload.contains("\"role\":\"" + userRole + "\""));
		assertTrue(jwtPayload.contains("\"jti\":\"" + jti + "\""));
		assertTrue(jwtPayload.contains("\"exp\":" + expiresAt));
	}

	@Test(expected = CertificateExpiredException.class)
	public void Should_ThrowCertificateExpiredException_When_TheCertificateHasExpired() {

		sut = JwtServiceCreator.create("private-invalid.p12", "pass123", "auth-server");
		sut.issueToken("valid.user@valid-mail.com", "Admin");
	}
}