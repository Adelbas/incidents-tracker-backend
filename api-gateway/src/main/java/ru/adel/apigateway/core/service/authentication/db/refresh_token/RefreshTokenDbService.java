package ru.adel.apigateway.core.service.authentication.db.refresh_token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.refresh_token.entity.RefreshToken;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenDbService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiredAt().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            return false;
        }

        return true;
    }

    public void removeOldToken(User user) {
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
    }
}
