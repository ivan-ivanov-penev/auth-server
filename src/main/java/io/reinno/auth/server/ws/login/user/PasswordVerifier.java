package io.reinno.auth.server.ws.login.user;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

@Service
public class PasswordVerifier {

	private static final int SALT_LENGTH = 16;

	private static final int HASH_LENGTH = 32;

	public boolean isPasswordMatching(User user, String plainTextPassword) {

		return isPasswordMatching(user.passwordHash, plainTextPassword);
	}

	public boolean isPasswordMatching(String passwordHash, String plainTextPassword) {

		return passwordHash != null && createHashingAlgorithm().verify(passwordHash, plainTextPassword.toCharArray());
	}

	private Argon2 createHashingAlgorithm() {

		return Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, SALT_LENGTH, HASH_LENGTH);
	}
}
