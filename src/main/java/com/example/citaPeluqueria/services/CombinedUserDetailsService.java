package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Servicio personalizado de autenticación que permite validar credenciales
 * tanto de clientes como de peluqueros.
 *
 * Implementa {@link UserDetailsService} y es utilizado por Spring Security
 * durante el proceso de login.
 *
 * La autenticación se intenta primero con los clientes y luego con los peluqueros.
 */
public class CombinedUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final HairdresserRepository hairdresserRepository;

    /**
     * Constructor para inyectar los repositorios de cliente y peluquero.
     *
     * @param clientRepository repositorio de clientes.
     * @param hairdresserRepository repositorio de peluqueros.
     */
    public CombinedUserDetailsService(ClientRepository clientRepository,
                                      HairdresserRepository hairdresserRepository) {
        this.clientRepository = clientRepository;
        this.hairdresserRepository = hairdresserRepository;
    }

    /**
     * Carga los detalles del usuario (cliente o peluquero) a partir de su correo electrónico.
     * <p>
     * Primero busca el correo en la base de datos de clientes. Si no lo encuentra,
     * lo busca entre los peluqueros. Si tampoco se encuentra, lanza una excepción.
     *
     * @param email correo electrónico usado como nombre de usuario.
     * @return los detalles del usuario para autenticación.
     * @throws UsernameNotFoundException si no se encuentra el usuario en ninguna tabla.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("=== CombinedUserDetailsService ACTIVO ===");
        System.out.println("Intentando loguear: " + email);

        // Buscar en clientes
        ClientEntity user = clientRepository.findByEmail(email);

        if (user != null) {
            System.out.println("Usuario encontrado: " + user.getEmail() + ", enabled: " + user.isEnabled());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),   // enabled = true o false según activación
                    true,              // accountNonExpired (puedes ajustar si quieres)
                    true,              // credentialsNonExpired
                    true,              // accountNonLocked
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList())

            );
        }

        // Buscar en peluqueros
        HairdresserEntity hairdresser = hairdresserRepository.findByEmail(email);
        if (hairdresser != null) {
            return new org.springframework.security.core.userdetails.User(
                    hairdresser.getEmail(),
                    hairdresser.getPassword(),
                    hairdresser.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList())
            );

        }

        throw new UsernameNotFoundException("No se encontró ningún usuario con el email: " + email);
    }
}
