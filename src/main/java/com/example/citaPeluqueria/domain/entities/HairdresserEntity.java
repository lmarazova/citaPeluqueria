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
 * Entidad que representa a un peluquero en el sistema.
 * Hereda de UserEntity, por lo que incluye datos básicos de usuario.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="peluquero")
public class HairdresserEntity extends UserEntity{
    /**
     * Conjunto de franjas horarias asignadas a este peluquero.
     * Relación uno a muchos con SlotEntity.
     */
    @OneToMany(mappedBy = "hairdresser")
    private Set<SlotEntity>slots;
    /**
     * Indica si el peluquero tiene permisos de administrador.
     */
    @Column
    private boolean isAdmin;


}
