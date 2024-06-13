package ru.adel.apigateway.public_interface.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationRequestDto;
import ru.adel.apigateway.public_interface.authentication.dto.AuthenticationResponseDto;
import ru.adel.apigateway.public_interface.authentication.dto.RegistrationRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegistrationRequestDto registrationRequestDto);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
}
