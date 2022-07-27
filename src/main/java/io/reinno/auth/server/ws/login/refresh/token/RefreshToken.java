package io.reinno.auth.server.ws.login.refresh.token;

import io.reinno.auth.server.ws.login.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
		name = "refresh_token",
		indexes = { @Index(columnList = "value"), @Index(columnList = "jwt_id") , @Index(columnList = "user_id") })
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
	public User user;

	@Column(name = "value", nullable = false)
	public String value;

	@Column(name = "jwt_id", nullable = false)
	public String jwtId;

	@Column(name = "issued_at", nullable = false)
	public LocalDateTime issuedAt;

	@Column(name = "expires_at", nullable = false)
	public LocalDateTime expiresAt;

	@Column(name = "cycle_start_date", nullable = false)
	public LocalDateTime cycleStartDate;

	@Column(name = "cycle_end_date", nullable = false)
	public LocalDateTime cycleEndDate;

	@Override
	public String toString() {

		return "RefreshToken{" +
				"id=" + id +
				", user=" + user +
				", value='" + value + '\'' +
				", jwtId='" + jwtId + '\'' +
				", issuedAt=" + issuedAt +
				", expiresAt=" + expiresAt +
				", cycleStartDate=" + cycleStartDate +
				", cycleEndDate=" + cycleEndDate +
				'}';
	}
}
