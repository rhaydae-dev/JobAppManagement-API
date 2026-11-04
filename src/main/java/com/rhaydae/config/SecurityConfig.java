package com.rhaydae.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rhaydae.service.JwtAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
            		
            		.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/activate",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                
                // =========================
                // Job offers
                // =========================

                .requestMatchers(HttpMethod.POST, "/api/jobOffer/**")
                    .hasRole("RECRUITER")

                .requestMatchers(HttpMethod.PUT, "/api/jobOffer/**")
                    .hasRole("RECRUITER")

                .requestMatchers(HttpMethod.DELETE, "/api/jobOffer/**")
                    .hasRole("RECRUITER")
                 
                 .requestMatchers(HttpMethod.PATCH, "/api/jobOffer/**")
                    .hasRole("RECRUITER")

                .requestMatchers(HttpMethod.GET, "/api/jobOffer/**")
                    .hasAnyRole("CANDIDATE", "RECRUITER", "ADMIN")
                    
                 // =========================
                    // User management
                    // =========================

                    .requestMatchers(HttpMethod.POST, "/api/users/createRecruiter")
                        .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/users/AllUsers")
                        .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/api/users/getBy/**")
                        .hasAnyRole("RECRUITER", "ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/users/updateUser/**")
                        .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/users/delete/**")
                        .hasRole("ADMIN")
                        
                        // =========================
                        // Application management
                        // =========================         

                        .requestMatchers(
                        	    org.springframework.http.HttpMethod.POST,
                        	    "/api/applications"
                        	).hasRole("CANDIDATE")

                        	.requestMatchers(
                        	    org.springframework.http.HttpMethod.GET,
                        	    "/api/applications/my"
                        	).hasRole("CANDIDATE")

                        	.requestMatchers(
                        	    org.springframework.http.HttpMethod.GET,
                        	    "/api/applications/job/**"
                        	).hasRole("RECRUITER")

                        	.requestMatchers(
                        	    org.springframework.http.HttpMethod.PATCH,
                        	    "/api/applications/*/status"
                        	).hasRole("RECRUITER")
                        	
                            // =========================
                            // Notification management
                            // =========================  
                        	
                      .requestMatchers(HttpMethod.GET, "/api/notification/myNotifications")
                      .hasRole("CANDIDATE")
                      
                      .requestMatchers(HttpMethod.PATCH, "/api/notification/markAsRead/*")
                      .hasRole("CANDIDATE")
                      
                      
                      // =========================
                      // Documents management
                      // =========================  
                      .requestMatchers(HttpMethod.POST, "/api/documents/upload")
                      .hasRole("CANDIDATE")

                      .requestMatchers(HttpMethod.GET, "/api/documents/myDocuments")
                      .hasRole("CANDIDATE")

                      .requestMatchers(HttpMethod.DELETE, "/api/documents/delete/*")
                      .hasRole("CANDIDATE")
                      
                      .requestMatchers(HttpMethod.GET, "/api/documents/download/*")
                      .hasRole("CANDIDATE")    
                      
               

                    // =========================
                    // Other requests
                    // =========================


                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}

