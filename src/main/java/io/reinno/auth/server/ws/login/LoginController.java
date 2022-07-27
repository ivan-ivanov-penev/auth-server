package io.reinno.auth.server.ws.login;

import io.reinno.auth.server.ws.login.exceptions.UnauthorizedException;
import io.reinno.auth.server.ws.login.exceptions.CertificateExpiredException;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/login")
@SecurityScheme(name = "bearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP) // required for Swagger
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	public static final int CERTIFICATE_EXPIRED_CUSTOM_HTTP_CODE = 512;

	private static final String BEARER_PREFIX = "Bearer ";

	private final LoginService loginService;

	@Autowired
	public LoginController(LoginService loginService) {

		this.loginService = loginService;
	}

	@PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public LoginResponse post(@Valid @RequestBody LoginRequest request) {

		return loginService.startNewSession(request.username, request.password, request.mfaCode);
	}

	@PutMapping(path = "/refresh", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "bearerAuth") // required for Swagger
	public RefreshResponse postRefresh(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@Valid @RequestBody RefreshRequest request) {

		String accessToken = extractAccessToken(authorizationHeader);

		return loginService.refreshLogin(accessToken, request.refreshToken);
	}

	private String extractAccessToken(String authorizationHeader) {

		if (authorizationHeader.startsWith(BEARER_PREFIX)) {

			return authorizationHeader.substring(BEARER_PREFIX.length());
		} else {

			throw new UnauthorizedException("Missing 'Bearer' declaration from the 'Authorization' header!");
		}
	}

	@DeleteMapping(
			path = "/refresh/{refreshToken}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "bearerAuth") // required for Swagger
	public void deleteRefresh(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String refreshToken) {

		String accessToken = extractAccessToken(authorizationHeader);

		boolean successfullyDeleted = loginService.revokeRefreshToken(refreshToken, accessToken);

		if (!successfullyDeleted) {

			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public void handleUnauthorizedException(UnauthorizedException e) {

		logExceptionMessageOnlyWithWarnLevel(e);
	}

	private void logExceptionMessageOnlyWithWarnLevel(Exception e) {

		LOGGER.warn(e.getMessage());
	}

	@ExceptionHandler(CertificateExpiredException.class)
	public void handleCertificateExpiredException(CertificateExpiredException e, HttpServletResponse response) {

		logExceptionMessageOnlyWithWarnLevel(e);

		response.setStatus(CERTIFICATE_EXPIRED_CUSTOM_HTTP_CODE);
	}
}
