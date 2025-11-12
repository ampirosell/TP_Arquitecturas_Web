package com.example.microservicioparada.security;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Locale;

@Component
public class RoleValidator {

    private static final String HEADER_NAME = "X-User-Role";

    public void require(String roleHeader, UserRole... allowedRoles) {
        if (allowedRoles == null || allowedRoles.length == 0) {
            return;
        }

        UserRole role = parseRole(roleHeader);

        boolean allowed = Arrays.stream(allowedRoles).anyMatch(r -> r == role);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("El rol %s no posee permisos suficientes", role));
        }
    }

    private UserRole parseRole(String roleHeader) {
        if (roleHeader == null || roleHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("Debe enviar el header %s", HEADER_NAME));
        }
        String normalized = roleHeader.trim().toUpperCase(Locale.ROOT);
        try {
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol inválido: " + normalized);
        }
    }
}


