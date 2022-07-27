package io.reinno.auth.server.ws.login;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import io.reinno.auth.server.config.AppConfig;
import io.reinno.auth.server.ws.login.jwt.JwtService;
import io.reinno.auth.server.ws.login.refresh.token.RefreshTokenRepository;
import io.reinno.auth.server.ws.login.refresh.token.RefreshTokenService;
import io.reinno.auth.server.ws.login.user.Role;
import io.reinno.auth.server.ws.login.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginService_generateLoginTokenPair_Extsts {

	/*
	private LoginService sut;

	@Mock
	private RefreshTokenRepository refreshTokenRepositoryMock;

	@Before
	public void setup() throws Exception {

		AppConfig appConfig = new AppConfig();

		String privateKey = getClass().getClassLoader().getResource("private.p12").getFile();

		JwtService jwtService = appConfig.jwtService(privateKey, "pass123".toCharArray(), "auth-server", 20);

		RefreshTokenService refreshTokenService = new RefreshTokenService(240, 20, refreshTokenRepositoryMock);

		CodeVerifier codeVerifier = appConfig.mfaCodeVerifier(1, 30);

		sut = new LoginService(null, null, jwtService, refreshTokenService, codeVerifier);
	}

	@Test
	public void Should_ReturnValidTokens_When_ValidMfaTokenAndCodeAreProvided() throws Exception {

		String mfaToken = "mfaToken";

		User user = new User();
		user.mfaSecret = "GZVUIHEI7BND42YHW6Z77EUYSWHNKS5CSUZWHM3AMAQP5CY4T5FLSTJSGPKCKL46";
		user.id = 123L;
		user.role = Role.ADMIN;

		String mfaCode = generateValidMfaCode(user.mfaSecret);

//		Step2Response actualResponse = sut.generateLoginTokenPair(mfaToken, mfaCode);
//
//		assertNotNull(actualResponse);
//		assertNotNull(actualResponse.accessToken);
//		assertNotNull(actualResponse.refreshToken);
	}

	private String generateValidMfaCode(String mfaSecret) throws Exception {

		CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
		return codeGenerator.generate(mfaSecret, new SystemTimeProvider().getTime() / 30);
	}
	*/
}