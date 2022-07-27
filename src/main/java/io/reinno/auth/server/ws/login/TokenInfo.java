package io.reinno.auth.server.ws.login;

public class TokenInfo {

	public final String value;

	public final long expiresAt;

	public TokenInfo(String value, long expiresAt) {

		this.value = value;
		this.expiresAt = expiresAt;
	}

	@Override
	public String toString() {

		return "TokenInfo{" +
				"value='" + value + '\'' +
				", expiresAt=" + expiresAt +
				'}';
	}
}
