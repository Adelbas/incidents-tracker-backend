package ru.adel.apigateway.core.service.authentication.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public static UserDetailsImpl of(User user) {
        return new UserDetailsImpl(user);
    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
