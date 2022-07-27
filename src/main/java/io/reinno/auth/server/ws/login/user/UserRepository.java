package io.reinno.auth.server.ws.login.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends org.springframework.data.repository.Repository<User, Long> {

	@Query("FROM User WHERE email = :email")
	User findByEmail(@Param("email") String email);
}
