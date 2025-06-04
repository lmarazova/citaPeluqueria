package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Clase base para todas las entidades con un identificador único.
 * Utiliza la estrategia de generación automática de ID para la clave primaria.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    /**
     * Identificador único de la entidad.
     * Se genera automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
