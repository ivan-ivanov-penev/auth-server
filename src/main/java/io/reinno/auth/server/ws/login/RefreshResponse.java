package io.reinno.auth.server.ws.login;

public class RefreshResponse extends LoginResponse {

	public RefreshResponse(TokenInfo accessToken, TokenInfo refreshToken) {

		super(accessToken, refreshToken);
	}
}
