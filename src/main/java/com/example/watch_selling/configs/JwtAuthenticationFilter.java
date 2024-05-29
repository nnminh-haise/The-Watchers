package com.example.watch_selling.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.watch_selling.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, NullPointerException {
        final String authHeader = request.getHeader("Authorization");
        final String requestPath = request.getRequestURI();
        final String requestMethod = request.getMethod();

        System.out.println("---------------");
        System.out.println("[LOG] Called API: " + requestPath);

        if ((startsWithOneOf(requestPath, List.of(
                "/api/auth",
                "/swagger-ui",
                "/v3/api-docs",
                "/api/watch/all",
                "/api/watch-type/all",
                "/api/watch-brand/all"))
                || (startsWithOneOf(requestPath, List.of("/api/watch")) && "GET".equalsIgnoreCase(requestMethod)))
                && (authHeader == null || !authHeader.startsWith("Bearer "))) {

            System.out.println("skipped");

            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            int statusCode = HttpStatus.UNAUTHORIZED.value();

            String errorMessage = "Unauthorized";
            if (e instanceof ExpiredJwtException) {
                errorMessage = "Token expired";
            } else if (e instanceof MalformedJwtException) {
                errorMessage = "Invalid token format";
            }

            response.setStatus(statusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(createErrorBody(errorMessage));
        } catch (NullPointerException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(createErrorBody("Token not found!"));
        }
    }

    private String createErrorBody(String errorMessage) {
        return "{\"error\": \"" + errorMessage + "\"}";
    }

    private Boolean startsWithOneOf(String target, List<String> prefixes) {
        for (String prefix : prefixes) {
            if (target.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}