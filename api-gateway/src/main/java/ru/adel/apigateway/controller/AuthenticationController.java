package ru.adel.apigateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.public_interface.authentication.AuthenticationService;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationRequestDto;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationResponseDto;
import ru.adel.apigateway.public_interface.authentication.dto.RegistrationRequestDto;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponseDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        log.info("Registration for {}", registrationRequestDto.email());
        return authenticationService.register(registrationRequestDto);
    }

    @PostMapping("/login")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        log.info("Authentication for {}", authenticationRequestDto.email());
        return authenticationService.authenticate(authenticationRequestDto);
    }

    @GetMapping("/refresh")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.refreshAccessToken(request, response);
    }
}
