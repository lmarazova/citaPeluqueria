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
/**
 * Configuración de seguridad de Spring Security para la aplicación.
 * Define las rutas públicas, protegidas, manejo de sesiones, login, logout,
 * y servicios relacionados con autenticación y codificación de contraseñas.
 */
@Configuration
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad para controlar el acceso a rutas.
     *
     * @param http instancia de {@link HttpSecurity} proporcionada por Spring.
     * @return la cadena de filtros construida.
     * @throws Exception si ocurre un error al construir la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas
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

                        // Rutas específicas por rol
                        .requestMatchers("/moderator/**").hasAnyRole("MODERATOR", "ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // Configuración de login personalizado
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")

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

                // Configuración de logout
                .logout(logout->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                        .permitAll())

                // Política de gestión de sesiones
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();

    }

    /**
     * Servicio personalizado para recuperar usuarios desde múltiples orígenes
     * (clientes y peluqueros en este caso).
     *
     * @param clientRepository repositorio de clientes.
     * @param hairdresserRepository repositorio de peluqueros.
     * @return el servicio de detalles de usuario combinado.
     */
    @Bean
    public UserDetailsService userDetailsService(
            ClientRepository clientRepository,
            HairdresserRepository hairdresserRepository) {
        return new CombinedUserDetailsService(clientRepository, hairdresserRepository);
    }



    /**
     * Bean que define el codificador de contraseñas.
     * Utiliza el algoritmo BCrypt.
     *
     * @return instancia de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
