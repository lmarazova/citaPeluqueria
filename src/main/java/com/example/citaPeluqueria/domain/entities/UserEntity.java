package com.example.citaPeluqueria.domain.entities;

import com.example.citaPeluqueria.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa un usuario del sistema.
 * Hereda de BaseEntity para incluir el identificador único.
 * Utiliza la estrategia JOINED para herencia en la base de datos.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class UserEntity extends BaseEntity{
    /**
     * Nombre de usuario único.
     */
    @Column
    private String username;
    /**
     * Email del usuario, único y opcional.
     * Validado para que tenga formato de correo electrónico.
     */
    @Column(nullable = true, unique = true)
    @Email
    private String email;
    /**
     * Contraseña del usuario.
     * Tiene longitud mínima de 5 caracteres y es opcional.
     */
    @Column(nullable = true)
    @Size(min = 5)
    private String password;
    /**
     * Conjunto de roles asignados al usuario.
     * Se almacena como colección embebida en tabla separada "user_roles".
     * Se cargan siempre (FetchType.EAGER).
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role>roles = new HashSet<>();


}
