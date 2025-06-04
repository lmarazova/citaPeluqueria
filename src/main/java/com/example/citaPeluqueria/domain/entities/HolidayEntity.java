package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
/**
 * Representa un día festivo (holiday) en el sistema.
 * Extiende de BaseEntity para heredar el campo ID.
 *
 * Cada día festivo tiene una fecha específica y una descripción.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="holiday")
public class HolidayEntity extends BaseEntity{
    /**
     * Fecha del día festivo.
     */
    @Column
    private LocalDate date;
    /**
     * Descripción del día festivo (ejemplo: "Navidad", "Año Nuevo").
     */
    @Column
    private String description;
}
