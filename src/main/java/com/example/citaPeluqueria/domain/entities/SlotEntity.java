package com.example.citaPeluqueria.domain.entities;

import com.example.citaPeluqueria.domain.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Representa una franja horaria (slot) en el sistema de reservas.
 *
 * Cada franja tiene una hora de inicio y fin, un estado que indica si está libre u ocupada,
 * y está asociada a un peluquero, un cliente, un servicio y una cita (appointment).
 *
 * Esta entidad es fundamental para gestionar la disponibilidad y las reservas en el sistema.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "franjas")
@BatchSize(size = 500)
public class SlotEntity extends BaseEntity{

    /**
     * Hora y fecha de inicio de la franja.
     */
    @Column
    private LocalDateTime startHour;

    /**
     * Hora y fecha de fin de la franja.
     */
    @Column
    private LocalDateTime finalHour;

    /**
     * Estado del slot (free, occupied, etc.).
     */
    @Column
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    /**
     * Peluquero asignado a esta franja.
     */
    @ManyToOne
    private HairdresserEntity hairdresser;

    /**
     * Cliente que ha reservado esta franja (puede ser null si está libre).
     */
    @ManyToOne
    private ClientEntity client;

    /**
     * Servicio asociado a esta franja.
     */
    @ManyToOne
    private ServiceEntity service;

    /**
     * Cita a la que pertenece esta franja.
     */
    @ManyToOne
    private AppointmentEntity appointment;

}
