package com.maveric.thinknxt.todo.config;

import com.maveric.thinknxt.todo.security.JwtAuthenticationEntryPoint;
import com.maveric.thinknxt.todo.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrf)-> csrf.disable())
                .authorizeHttpRequests((authorize) -> {
                    /*authorize.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN","USER");
                    authorize.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN","USER");
                    authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll();*/
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());

        httpSecurity.exceptionHandling(exception-> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails bala = User.builder()
                .username("bala")
                .password(passwordEncoder().encode("bala"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, bala);
    }*/
}
