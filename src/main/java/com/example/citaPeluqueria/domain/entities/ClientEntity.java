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
 * Entidad que representa a un cliente del sistema.
 * Hereda de UserEntity, por lo que incluye datos de usuario.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="clients")
public class ClientEntity extends UserEntity{
    /**
     * Número de teléfono único del cliente.
     * Es obligatorio y no puede repetirse.
     */
    @Column(nullable = false, unique = true)
    private String phone;
    /**
     * Indica si el cliente es invitado (guest).
     * Por defecto es false.
     */
    @Column(nullable = false)
    private boolean guest = false;
    /**
     * Comentarios adicionales sobre el cliente.
     * Campo de texto libre.
     */
    @Column(columnDefinition = "TEXT")
    private String comments;
    /**
     * Conjunto de franjas horarias (slots) asociadas al cliente.
     * Relación uno a muchos con SlotEntity.
     */
    @OneToMany(mappedBy = "client")
    private Set<SlotEntity>slots;
    /**
     * Conjunto de citas (appointments) asociadas al cliente.
     * Relación uno a muchos con AppointmentEntity.
     */
    @OneToMany(mappedBy = "user")
    private Set<AppointmentEntity>appointments;
    /**
     * Indica si el cliente está habilitado para usar el sistema.
     * Por defecto es false.
     */
    @Column
    private boolean enabled = false;
}
