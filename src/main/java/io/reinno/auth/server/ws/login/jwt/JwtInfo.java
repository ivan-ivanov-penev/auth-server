package io.reinno.auth.server.ws.login.jwt;

import io.reinno.auth.server.ws.login.TokenInfo;

public class JwtInfo {

	public final TokenInfo tokenInfo;

	public final String id;

	public JwtInfo(String value, long expiresAt, String id) {

		this(new TokenInfo(value, expiresAt), id);
	}

	public JwtInfo(TokenInfo tokenInfo, String id) {

		this.tokenInfo = tokenInfo;
		this.id = id;
	}
}
