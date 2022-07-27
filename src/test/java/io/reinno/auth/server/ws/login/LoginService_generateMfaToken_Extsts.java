package io.reinno.auth.server.ws.login;

import io.reinno.auth.server.ws.login.exceptions.UnauthorizedException;
import io.reinno.auth.server.ws.login.user.PasswordVerifier;
import io.reinno.auth.server.ws.login.user.User;
import io.reinno.auth.server.ws.login.user.UserRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static io.reinno.auth.server.ws.login.user.PasswordVerifier_isPasswordMatching_Tests.DEFAULT_PASSWORD;
import static io.reinno.auth.server.ws.login.user.PasswordVerifier_isPasswordMatching_Tests.DEFAULT_PASSWORD_CORRESPONDING_HASH;

@RunWith(MockitoJUnitRunner.class)
public class LoginService_generateMfaToken_Extsts {
/*
	private LoginService sut;

	@Mock
	private UserRepository userRepositoryMock;

	@Before
	public void setup() {

		PasswordVerifier passwordVerifier = new PasswordVerifier();

		sut = new LoginService(userRepositoryMock, passwordVerifier, null, null, null);
	}

	@Ignore
	@Test
	public void Should_ReturnValidMfaToken_When_CredentialsAreValid() {

		String username = "validUsername@reinno.io";

		setupUserInDatabaseWithDefaultPassword(username);

//		String mfaToken = sut.generateMfaToken(username, DEFAULT_PASSWORD);
//
//		assertNotNull(mfaToken);
//		assertFalse(mfaToken.isBlank());
	}

	private void setupUserInDatabaseWithDefaultPassword(String username) {

		User user = new User();
		user.passwordHash = DEFAULT_PASSWORD_CORRESPONDING_HASH;

		when(userRepositoryMock.findByEmail(username)).thenReturn(user);
	}

	@Ignore
	@Test(expected = UnauthorizedException.class)
	public void Should_ThrowUnauthorizedException_When_UsernameIsNotPresentInDatabase() {

		String username = "nonexistent@reinno.io";

		when(userRepositoryMock.findByEmail(username)).thenReturn(null);

//		sut.generateMfaToken(username, DEFAULT_PASSWORD);
	}

	@Ignore
	@Test(expected = UnauthorizedException.class)
	public void Should_ThrowUnauthorizedException_When_InvalidPasswordIsProvided() {

		String username = "hacker";

		setupUserInDatabaseWithDefaultPassword(username);

//		sut.generateMfaToken(username, "invalidPassword");
	}

	@Ignore
	@Test(expected = UnauthorizedException.class)
	public void Should_ThrowUnauthorizedException_When_NullUsernameIsProvided() {

//		sut.generateMfaToken(null, "anotherLikelyInvalidPassword");
	}

	@Ignore
	@Test(expected = UnauthorizedException.class)
	public void Should_ThrowUnauthorizedException_When_NullPasswordIsProvided() {

		String username = "goodUser@goodEmail.good";

		setupUserInDatabaseWithDefaultPassword(username);

//		sut.generateMfaToken(username, null);
	}
 */
}
