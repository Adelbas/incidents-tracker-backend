package ru.adel.apigateway.core.ws;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.adel.apigateway.core.service.authentication.security.JwtService;
import ru.adel.apigateway.core.service.authentication.security.details.UserDetailsImpl;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    String userEmail = jwtService.extractUsername(token);

                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                        if (jwtService.isTokenValid(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            StompPrincipal stompPrincipal = new StompPrincipal(((UserDetailsImpl) userDetails).getId());
                            accessor.setUser(
                                    new UsernamePasswordAuthenticationToken(
                                            stompPrincipal,
                                            token,
                                            userDetails.getAuthorities()
                                    )
                            );
                            accessor.setLeaveMutable(true);
                        }
                    }
                } catch (ExpiredJwtException e) {
                    log.error("Token expired");
                } catch (SignatureException e) {
                    log.error("Signature invalid");
                }
            } else {
                throw new RuntimeException("Authentication token is required");
            }
        }

        return message;
    }
}
