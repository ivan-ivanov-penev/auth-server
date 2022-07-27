package io.reinno.auth.server.ws.login;

public class LoginResponse {

	public final TokenInfo accessToken;

	public final TokenInfo refreshToken;

	public LoginResponse(TokenInfo accessToken, TokenInfo refreshToken) {

		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {

		return "Step2Response{" +
				"accessToken=" + accessToken +
				", refreshToken=" + refreshToken +
				'}';
	}
}
