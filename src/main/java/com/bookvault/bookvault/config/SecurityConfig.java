package com.bookvault.bookvault.config;

import com.bookvault.bookvault.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // public
                        .requestMatchers("/api/auth/login").permitAll()

                        // member allowed reads
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/books/**").hasAnyRole("LIBRARIAN", "MEMBER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/members/*/loans").hasAnyRole("LIBRARIAN", "MEMBER")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/loans/*/return").hasAnyRole("LIBRARIAN", "MEMBER")

                        // librarian-only operations
                        .requestMatchers("/api/**").hasRole("LIBRARIAN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // for simplicity (fresher project)
    }
}
