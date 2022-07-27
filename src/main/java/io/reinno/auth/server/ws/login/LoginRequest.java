package io.reinno.auth.server.ws.login;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {

	@NotBlank(message = "Username must not be blank!")
	public final String username;

	@NotBlank(message = "Password must not be blank!")
	public final String password;

	@NotBlank(message = "MFA Code must not be blank!")
	@Digits(message = "MFA Code must contain only digits!", fraction = 0, integer = 7) // Integer is like max Length
	@Size(message = "MFA Code must be exactly 6 digits long!", min = 6, max = 6)
	public final String mfaCode;

	@JsonCreator
	public LoginRequest(String username, String password, String mfaCode) {

		this.username = username;
		this.password = password;
		this.mfaCode = mfaCode;
	}

	@Override
	public String toString() {

		return "LoginRequest{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", mfaCode='" + mfaCode + '\'' +
				'}';
	}
}
