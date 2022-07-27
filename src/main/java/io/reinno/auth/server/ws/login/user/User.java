package io.reinno.auth.server.ws.login.user;

import javax.persistence.*;

@Entity
@Table(name = "user", indexes = { @Index(columnList = "email") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(name = "email")
	public String email;

	@Column(name = "password_hash")
	public String passwordHash;

	@Column(name = "mfa_secret")
	public String mfaSecret;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	public Role role;

	@Override
	public String toString() {

		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", passwordHash='" + passwordHash + '\'' +
				", mfaSecret='" + mfaSecret + '\'' +
				", role=" + role +
				'}';
	}
}
