package io.reinno.auth.server.ws.login;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class RefreshRequest {

	@NotBlank(message = "Refresh-token must not be blank!")
	public String refreshToken;

	@JsonCreator
	public RefreshRequest(String refreshToken) {

		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {

		return "RefreshRequest{" +
				"refreshToken='" + refreshToken + '\'' +
				'}';
	}
}
