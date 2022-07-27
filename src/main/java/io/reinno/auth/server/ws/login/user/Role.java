package io.reinno.auth.server.ws.login.user;

public enum Role {

	ADMIN("admin"), USER("user");

	private final String value;

	Role(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}
}
