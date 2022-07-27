package io.reinno.auth.server.ws.login.refresh.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.reinno.auth.server.ws.login.TokenInfo;
import io.reinno.auth.server.ws.login.exceptions.UnauthorizedException;
import io.reinno.auth.server.ws.login.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class RefreshTokenService {

	private final int cycleValidityMinutes;

	private final int refreshTokenValidityMinutes;

	private final RefreshTokenRepository repository;

	@Autowired
	public RefreshTokenService(
			@Value("${refresh.token.cycle.validity.minutes}") int cycleValidityMinutes,
			@Value("${refresh.token.validity.minutes}") int refreshTokenValidityMinutes,
			RefreshTokenRepository repository) {

		this.cycleValidityMinutes = cycleValidityMinutes;
		this.refreshTokenValidityMinutes = refreshTokenValidityMinutes;
		this.repository = repository;
	}

	public TokenInfo startNewRefreshCycle(String jwtId, User user) {

		RefreshToken refreshToken = createRefreshToken(jwtId);
		refreshToken.cycleStartDate = refreshToken.issuedAt;
		refreshToken.cycleEndDate = LocalDateTime.now().plusMinutes(cycleValidityMinutes);
		refreshToken.user = user;

		repository.deleteByUserId(user.id);
		repository.save(refreshToken);

		return createTokenInfo(refreshToken);
	}

	private RefreshToken createRefreshToken(String jwtId) {

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.issuedAt = LocalDateTime.now();
		refreshToken.expiresAt = LocalDateTime.now().plusMinutes(refreshTokenValidityMinutes);
		refreshToken.value = UUID.randomUUID().toString();
		refreshToken.jwtId = jwtId;

		return refreshToken;
	}

	private TokenInfo createTokenInfo(RefreshToken refreshToken) {

		return new TokenInfo(refreshToken.value, refreshToken.expiresAt.atZone(ZoneId.systemDefault()).toEpochSecond());
	}

	public void verify(String refreshTokenValue, DecodedJWT jwt) {

		RefreshToken refreshToken = repository.selectByValue(refreshTokenValue);

		if (refreshToken == null) {

			throw new UnauthorizedException("Invalid refresh token: " + refreshTokenValue);
		}

		if (!jwt.getId().equals(refreshToken.jwtId)) {

			throw new UnauthorizedException("Provided jwt-id doesn't correspond to refresh token's jwt-id!");
		}

		verifyNotExpired(refreshToken);
	}

	private void verifyNotExpired(RefreshToken refreshToken) {

		LocalDateTime now = LocalDateTime.now();

		if (now.isAfter(refreshToken.cycleEndDate)) {

			throw new UnauthorizedException("Expired refresh cycle (period)!");
		}

		if (now.isAfter(refreshToken.expiresAt)) {

			throw new UnauthorizedException("Expired refresh-token!");
		}
	}

	public TokenInfo updateToken(String jwtId, String oldRefreshTokenValue) {

		RefreshToken refreshToken = createRefreshToken(jwtId);

		repository.updateByValue(refreshToken, oldRefreshTokenValue);

		return createTokenInfo(refreshToken);
	}

	public boolean revoke(String refreshTokenValue, String jwtId) {

		return repository.deleteByValueAndJwtId(refreshTokenValue, jwtId);
	}
}
