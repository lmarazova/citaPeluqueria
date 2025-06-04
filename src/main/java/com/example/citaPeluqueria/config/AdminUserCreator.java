package com.example.citaPeluqueria.config;

import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.util.Constants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
/**
 * Clase de configuración que crea un usuario administrador por defecto al iniciar la aplicación.
 *
 * Implementa {@link CommandLineRunner} para ejecutar código justo después de que el contexto de Spring esté listo.
 *
 * - Comprueba si ya existe un usuario administrador con el nombre definido en {@code Constants.ADMIN_NAME}.
 * - Si no existe, crea un nuevo usuario administrador con roles ADMIN y MODERATOR.
 * - La contraseña del administrador se encripta usando {@link PasswordEncoder}.
 *
 * Esto asegura que siempre haya un usuario administrador disponible para gestionar la aplicación.
 */
@Configuration
public class AdminUserCreator implements CommandLineRunner {
    private final HairdresserRepository hairdresserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserCreator(HairdresserRepository hairdresserRepository, PasswordEncoder passwordEncoder) {
        this.hairdresserRepository = hairdresserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Comprobar si ya existe un usuario administrador
        if (hairdresserRepository.findByUsername(Constants.ADMIN_NAME) == null) {
            HairdresserEntity adminUser = new HairdresserEntity();
            adminUser.setUsername(Constants.ADMIN_NAME);
            adminUser.setEmail(Constants.ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(Constants.ADMIN_PASSWORD)); // Contraseña encriptada
            adminUser.setRoles(Set.of(Role.ADMIN, Role.MODERATOR));
            adminUser.setAdmin(true);
            hairdresserRepository.save(adminUser);

            System.out.println("Administrador creado: admin@domain.com");
        } else {
            System.out.println("El usuario administrador ya existe.");
        }
    }
}
