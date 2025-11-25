package com.example.microserviciouser.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;

@Component
public class RoleValidator {

    public void require(UserRole... allowedRoles) {
        if (allowedRoles == null || allowedRoles.length == 0) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        UserRole userRole = null;

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();
            if (authorityName.startsWith("ROLE_")) {
                String roleName = authorityName.substring(5); // Remove "ROLE_" prefix
                try {
                    UserRole foundRole = UserRole.valueOf(roleName);
                    userRole = foundRole;
                    break;
                } catch (IllegalArgumentException e) {
                    // Continue searching
                }
            }
        }

        if (userRole == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se pudo determinar el rol del usuario");
        }

        final UserRole finalUserRole = userRole;
        boolean allowed = Arrays.stream(allowedRoles).anyMatch(r -> r == finalUserRole);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("El rol %s no posee permisos suficientes", userRole));
        }
    }

    // Método de compatibilidad para mantener el header-based approach si es necesario
    public void require(String roleHeader, UserRole... allowedRoles) {
        if (roleHeader != null && !roleHeader.isBlank()) {
            // Si se proporciona header, usar el método anterior (para compatibilidad)
            UserRole role = parseRole(roleHeader);
            boolean allowed = Arrays.stream(allowedRoles).anyMatch(r -> r == role);
            if (!allowed) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        String.format("El rol %s no posee permisos suficientes", role));
            }
        } else {
            // Si no hay header, usar JWT del contexto de seguridad
            require(allowedRoles);
        }
    }

    private UserRole parseRole(String roleHeader) {
        if (roleHeader == null || roleHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Debe enviar el header X-User-Role o un token JWT válido");
        }
        String normalized = roleHeader.trim().toUpperCase();
        try {
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol inválido: " + normalized);
        }
    }
}


