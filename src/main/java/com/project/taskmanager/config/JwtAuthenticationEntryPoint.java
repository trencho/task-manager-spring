package com.project.taskmanager.config;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {
        // Respond with a 401-error code for unauthorized access
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: You need to provide a valid JWT token.");
    }

}
