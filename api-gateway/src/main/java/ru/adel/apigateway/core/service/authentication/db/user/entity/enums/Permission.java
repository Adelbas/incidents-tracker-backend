package ru.adel.apigateway.core.service.authentication.db.user.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    USER_PERM("user:permission"),
    ADMIN_PERM("admin:permission");

    private final String permission;
}