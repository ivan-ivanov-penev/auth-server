package io.reinno.auth.server.ws.login.user;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordVerifier_isPasswordMatching_Tests {

	public static final String DEFAULT_PASSWORD = "password";

	public static final String DEFAULT_PASSWORD_CORRESPONDING_HASH =
			"$argon2id$v=19$m=10,t=1,p=1$d+K+cPUY3a4$IFzePrPctHZTlBG/m5I9eA"; // corresponds to "password"

	private final PasswordVerifier sut = new PasswordVerifier();

	@Test
	public void Should_ReturnTrue_When_PasswordMatchesHash() {

		boolean passwordMatching = sut.isPasswordMatching(DEFAULT_PASSWORD_CORRESPONDING_HASH, DEFAULT_PASSWORD);

		assertTrue(passwordMatching);
	}

	@Test
	public void Should_ReturnFalse_When_PasswordDoesNotMatchHash() {

		boolean passwordMatching = sut.isPasswordMatching(DEFAULT_PASSWORD_CORRESPONDING_HASH, "nonMatchingPassword");

		assertFalse(passwordMatching);
	}

	@Ignore // this is an impossible case because the plainTextPassword is checked by the Controller
	@Test
	public void Should_ReturnFalse_When_ProvidedPasswordIsNull() {

		boolean passwordMatching = sut.isPasswordMatching(DEFAULT_PASSWORD_CORRESPONDING_HASH, null);

		assertFalse(passwordMatching);
	}

	@Test
	public void Should_ReturnFalse_When_ProvidedPasswordHashIsNull() {

		boolean passwordMatching = sut.isPasswordMatching((String) null, "someValidPassword");

		assertFalse(passwordMatching);
	}

	@Test
	public void Should_ReturnFalse_When_ProvidedPasswordHashIsInvalid() {

		boolean passwordMatching = sut.isPasswordMatching("invalidHash", "otherValidPassword");

		assertFalse(passwordMatching);
	}

	@Test(expected = NullPointerException.class)
	public void Should_ReturnFalse_When_ProvidedUserIsNull() {

		sut.isPasswordMatching((User) null, "SuperStr0ngP@assWord!");
	}
}