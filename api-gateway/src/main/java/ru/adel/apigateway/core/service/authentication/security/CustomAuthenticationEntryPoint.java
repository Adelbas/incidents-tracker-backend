package ru.adel.apigateway.core.service.authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.adel.apigateway.public_interface.exception.ErrorResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid request or token!")
                .path(request.getRequestURI())
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        objectMapper.setDateFormat(df);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}