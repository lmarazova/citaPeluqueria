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


public class CombinedUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final HairdresserRepository hairdresserRepository;

    public CombinedUserDetailsService(ClientRepository clientRepository,
                                      HairdresserRepository hairdresserRepository) {
        this.clientRepository = clientRepository;
        this.hairdresserRepository = hairdresserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar primero en usuarios
        System.out.println("=== CombinedUserDetailsService ACTIVO ===");
        System.out.println("Intentando loguear: " + email);
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
