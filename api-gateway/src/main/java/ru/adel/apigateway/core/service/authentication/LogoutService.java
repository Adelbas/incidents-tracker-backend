package ru.adel.apigateway.core.service.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.refresh_token.RefreshTokenDbService;
import ru.adel.apigateway.core.service.authentication.db.user.UserDbService;
import ru.adel.apigateway.core.service.authentication.security.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;

    private final UserDbService userDbService;

    private final RefreshTokenDbService refreshTokenDbService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        String username;
        try {
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            username = e.getClaims().getSubject();
        }

        if (username != null) {
            log.info("Logging out user {}", username);
            var user = userDbService.getByEmail(username);
            refreshTokenDbService.removeOldToken(user);
            SecurityContextHolder.clearContext();
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
