package com.rhaydae.service;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	

        final String authHeader =
                request.getHeader("Authorization");

        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("No Bearer token found.");

            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {

            String email = jwtService.extractUsername(token);

            System.out.println("Email extracted from JWT: " + email);

            if (email != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                System.out.println(
                        "User found: " + userDetails.getUsername()
                );

                System.out.println(
                        "User authorities: " + userDetails.getAuthorities()
                );
                
                if (jwtService.isTokenValid(
                        token,
                        userDetails)) {

                    System.out.println("JWT is VALID.");

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                } else {

                    System.out.println("JWT is INVALID.");
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT ERROR: " + e.getClass().getName()
            );

            System.out.println(
                    "JWT ERROR MESSAGE: " + e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}

