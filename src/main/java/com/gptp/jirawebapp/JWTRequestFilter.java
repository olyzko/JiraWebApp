package com.gptp.jirawebapp;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    JWT jwt;

    public JWTRequestFilter(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearer = request.getHeader("Authorization");
        if (bearer == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = bearer.substring("Bearer ".length());

        try {
            JWTContent userDetails = jwt.decode(token);

            // Create an Authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

            // Set the Authentication object in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {}

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
