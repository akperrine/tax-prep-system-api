package com.skillstorm.taxprepsystemapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/register")
                .and()

                .authorizeHttpRequests(
                        (authorize) ->
                                authorize
                                        .mvcMatchers(HttpMethod.GET, "/login").permitAll()
                                        .mvcMatchers(HttpMethod.POST, "/register").permitAll()
                                        .mvcMatchers("/user/**").authenticated()
                                        .mvcMatchers("/tax/**").authenticated()
                )

                .cors(cors -> {
                    cors.configurationSource(request -> {

                        // configuring how we want to handle cors
                        CorsConfiguration corsConfig = new CorsConfiguration();
                        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));               // what origins are allowed
                        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));        // what http methods are allowed
                        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN"));       // what headers are allowed
                        corsConfig.setAllowCredentials(true);                                               // allow cookies to be sent to backend
                        corsConfig.setMaxAge(3600L);                                                        // how long to cache the cors preflight request (OPTIONS)

                        // setting which endpoints to apply the above cors configurations to
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**", corsConfig);

                        return corsConfig;
                    });
                })
                .httpBasic();


        // @formatter:on
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
