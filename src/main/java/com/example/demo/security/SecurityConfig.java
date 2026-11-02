package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfException;

import java.io.IOException;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint unauthorized = (req, res, e) -> {
            res.setHeader("WWW-Authenticate", "Basic realm=\"exam\"");
            error(res, HttpServletResponse.SC_UNAUTHORIZED, "authentication required");
        };
        http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/error", "/api/auth/csrf", "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/exams/**", "/api/exam-tasks/**").authenticated()
                        .requestMatchers("/api/exams/**", "/api/exam-tasks/**").hasRole(User.TEACHER)
                        .anyRequest().authenticated())
                .httpBasic(b -> b.authenticationEntryPoint(unauthorized))
                // Basic-аутентификация идёт на каждом запросе, поэтому токен не перевыпускается при «входе»
                .csrf(c -> c.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(unauthorized)
                        .accessDeniedHandler((req, res, ex) -> error(res, HttpServletResponse.SC_FORBIDDEN,
                                ex instanceof CsrfException ? "invalid or missing CSRF token" : "access denied")));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User.withUsername(u.username)
                        .password(u.password)
                        .roles(u.role)
                        .disabled(User.BLOCKED.equals(u.status))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private static void error(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
