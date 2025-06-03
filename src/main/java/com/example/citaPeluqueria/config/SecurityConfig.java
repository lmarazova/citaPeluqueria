package com.example.citaPeluqueria.config;

import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.CombinedUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/create-service",
                                "/show-blocks",
                                "/forgot-password",
                                "/reset-password",
                                "/activate",
                                "/").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/show-blocks").permitAll()
                        .requestMatchers("/moderator/**").hasAnyRole("MODERATOR", "ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        /*.successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin");
                            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))) {
                                response.sendRedirect("/moderator");
                            } else {
                                response.sendRedirect("/home");
                            }
                        })*/
                        .successHandler((request, response, authentication) -> {
                            System.out.println("=== Usuario autenticado con roles:");
                            authentication.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));

                            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin");
                            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))) {
                                response.sendRedirect("/moderator");
                            } else {
                                response.sendRedirect("/home");
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            if (exception.getClass().isAssignableFrom(DisabledException.class)) {
                                request.getSession().setAttribute("error", "Tu cuenta no está activada. Revisa tu correo.");
                                response.sendRedirect("/login?error");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })


                        .permitAll()
                )

                .logout(logout->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                        .permitAll())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();

    }
    @Bean
    public UserDetailsService userDetailsService(
            ClientRepository clientRepository,
            HairdresserRepository hairdresserRepository) {
        return new CombinedUserDetailsService(clientRepository, hairdresserRepository);
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
