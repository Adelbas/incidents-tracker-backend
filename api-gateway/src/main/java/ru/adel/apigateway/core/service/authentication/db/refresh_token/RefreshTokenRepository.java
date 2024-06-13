package ru.adel.apigateway.core.service.authentication.db.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.adel.apigateway.core.service.authentication.db.refresh_token.entity.RefreshToken;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);
}
