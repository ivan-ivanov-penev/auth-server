package io.reinno.auth.server.ws.login.refresh.token;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

	@Query("FROM RefreshToken WHERE value = :value")
	RefreshToken selectByValue(@Param("value") String value);

	@Modifying
	@Transactional
	@Query("UPDATE RefreshToken" +
			" SET issuedAt = :#{#token.issuedAt}" +
			", expiresAt = :#{#token.expiresAt}" +
			", jwtId = :#{#token.jwtId}" +
			", value = :#{#token.value}" +
			" WHERE value = :oldRefreshTokenValue")
	void updateByValue(
			@Param("token") RefreshToken refreshToken, @Param("oldRefreshTokenValue") String oldRefreshTokenValue);

	@Modifying
	@Transactional
	@Query("DELETE FROM RefreshToken WHERE user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM RefreshToken WHERE value = :value AND jwtId = :jwtId")
	boolean deleteByValueAndJwtId(@Param("value") String value, @Param("jwtId") String jwtId);
}
