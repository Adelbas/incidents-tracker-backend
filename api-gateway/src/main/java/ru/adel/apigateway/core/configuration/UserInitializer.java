package ru.adel.apigateway.core.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.adel.apigateway.core.service.authentication.db.user.UserDbService;
import ru.adel.apigateway.public_interface.authentication.AuthenticationService;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationRequestDto;
import ru.adel.apigateway.public_interface.authentication.dto.RegistrationRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInitializer {

    private static final String DEFAULT_EMAIL = "user@mail.ru";

    private final AuthenticationService authenticationService;

    private final UserDbService userDbService;

    @PostConstruct
    public void init() {
        if (!clientWithEmailExists(DEFAULT_EMAIL)) {
            authenticationService.register(
                    RegistrationRequestDto.builder()
                            .firstname("user")
                            .lastname("user")
                            .email(DEFAULT_EMAIL)
                            .password("user")
                            .build()
            );
        }

        if (!clientWithEmailExists("adel@mail.ru")) {
            authenticationService.register(
                    RegistrationRequestDto.builder()
                            .firstname("user")
                            .lastname("user")
                            .email("adel@mail.ru")
                            .password("adel")
                            .build()
            );
        }

        String jwt = authenticationService.authenticate(
                AuthenticationRequestDto.builder()
                        .email(DEFAULT_EMAIL)
                        .password("user")
                        .build()
        ).accessToken();

        log.info("Jwt token: {}", jwt);
    }

    private boolean clientWithEmailExists(String email) {
        return userDbService.findByEmail(email).isPresent();
    }
}
