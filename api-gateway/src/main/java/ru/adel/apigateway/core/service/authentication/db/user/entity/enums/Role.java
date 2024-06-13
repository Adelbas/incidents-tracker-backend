package ru.adel.apigateway.core.service.authentication.db.user.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.adel.apigateway.core.service.authentication.db.user.entity.enums.Permission.ADMIN_PERM;
import static ru.adel.apigateway.core.service.authentication.db.user.entity.enums.Permission.USER_PERM;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Set.of(USER_PERM)),
    ADMIN(Set.of(USER_PERM,ADMIN_PERM));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}