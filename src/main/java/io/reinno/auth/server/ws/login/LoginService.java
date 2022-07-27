package io.reinno.auth.server.ws.login;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.samstevens.totp.code.CodeVerifier;
import io.reinno.auth.server.ws.login.exceptions.UnauthorizedException;
import io.reinno.auth.server.ws.login.jwt.JwtInfo;
import io.reinno.auth.server.ws.login.jwt.JwtService;
import io.reinno.auth.server.ws.login.refresh.token.RefreshTokenService;
import io.reinno.auth.server.ws.login.user.PasswordVerifier;
import io.reinno.auth.server.ws.login.user.User;
import io.reinno.auth.server.ws.login.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	private final UserRepository userRepository;

	private final PasswordVerifier passwordVerifier;

	private final JwtService jwtService;

	private final RefreshTokenService refreshTokenService;

	private final CodeVerifier mfaCodeVerifier;

	@Autowired
	public LoginService(
			UserRepository userRepository,
			PasswordVerifier passwordVerifier,
			JwtService jwtService,
			RefreshTokenService refreshTokenService,
			CodeVerifier mfaCodeVerifier) {

		this.userRepository = userRepository;
		this.passwordVerifier = passwordVerifier;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.mfaCodeVerifier = mfaCodeVerifier;
	}

	public LoginResponse startNewSession(String email, String password, String mfaCode) {

		User user = userRepository.findByEmail(email);

		validate(user != null, "User with username '%s' does not exist!", email);
		validate(passwordVerifier.isPasswordMatching(user, password), "Password for '%s' doesn't match!", email);
		validate(mfaCodeVerifier.isValidCode(user.mfaSecret, mfaCode), "Invalid MFA code '%s' for %s", mfaCode, email);

		JwtInfo jwtInfo = jwtService.issueToken(user);

		TokenInfo refreshToken = refreshTokenService.startNewRefreshCycle(jwtInfo.id, user);

		return new LoginResponse(jwtInfo.tokenInfo, refreshToken);
	}

	private void validate(boolean condition, String unauthorizedExceptionMessageFormat, Object...args) {

		if (!condition) {

			throw new UnauthorizedException(String.format(unauthorizedExceptionMessageFormat, args));
		}
	}

	public RefreshResponse refreshLogin(String accessToken, String refreshToken) {

		DecodedJWT jwt = jwtService.decodeAndVerify(accessToken);

		refreshTokenService.verify(refreshToken, jwt);

		JwtInfo jwtInfo = jwtService.issueToken(jwt);

		TokenInfo refreshTokenInfo = refreshTokenService.updateToken(jwtInfo.id, refreshToken);

		return new RefreshResponse(jwtInfo.tokenInfo, refreshTokenInfo);
	}

	public boolean revokeRefreshToken(String refreshToken, String accessToken) {

		DecodedJWT jwt = jwtService.decodeAndVerify(accessToken);

		return refreshTokenService.revoke(refreshToken, jwt.getId());
	}
}
