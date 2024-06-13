package ru.adel.apigateway.core.service.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.refresh_token.RefreshTokenDbService;
import ru.adel.apigateway.core.service.authentication.db.user.UserDbService;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;
import ru.adel.apigateway.core.service.authentication.db.user.entity.enums.Role;
import ru.adel.apigateway.core.service.authentication.security.JwtService;
import ru.adel.apigateway.public_interface.authentication.AuthenticationService;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationRequestDto;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationResponseDto;
import ru.adel.apigateway.public_interface.authentication.dto.RegistrationRequestDto;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;

    private final UserDbService userDbService;

    private final RefreshTokenDbService refreshTokenDbService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto register(RegistrationRequestDto registrationRequestDto) {
        if (userDbService.findByEmail(registrationRequestDto.email()).isPresent())
            throw new RuntimeException(String.format("User %s already exists", registrationRequestDto.email()));

        var user = User.builder()
                .firstName(registrationRequestDto.firstname())
                .lastName(registrationRequestDto.lastname())
                .email(registrationRequestDto.email())
                .password(passwordEncoder.encode(registrationRequestDto.password()))
                .role(Role.USER)
                .active(true)
                .build();

        userDbService.save(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenDbService.save(refreshToken);

        return AuthenticationResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.email(), authenticationRequestDto.password()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        User user = userDbService.getByEmail(authenticationRequestDto.email());

        var accessToken = jwtService.generateAccessToken(user);
        refreshTokenDbService.removeOldToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenDbService.save(refreshToken);

        return AuthenticationResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);

        log.info("Refreshing token for {}", username);

        if (username != null) {
            var user = userDbService.getByEmail(username);

            if (refreshTokenDbService.isValid(refreshToken)) {
                var accessToken = jwtService.generateAccessToken(user);
                var authResponse = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
