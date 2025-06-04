package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
/**
 * Entidad que representa una cita en el sistema.
 * Contiene información del cliente, servicio, franjas horarias, fecha y hora seleccionada.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="citas")
public class AppointmentEntity extends BaseEntity{
    /**
     * Cliente que realiza la cita.
     * Relación muchos a uno con ClientEntity.
     */
    @ManyToOne
    private ClientEntity user;
    /**
     * Servicio asociado a la cita.
     * Relación muchos a uno con ServiceEntity.
     */
    @ManyToOne
    private ServiceEntity service;
    /**
     * Lista de franjas horarias relacionadas con esta cita.
     * Relación uno a muchos con SlotEntity.
     * Las operaciones en Appointment se propagan a las franjas horarias (cascade ALL).
     */
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<SlotEntity> slots;
    /**
     * Rango horario seleccionado para la cita (por ejemplo, "10:00-11:00").
     */
    private String selectedHourRange;
    /**
     * Fecha de la cita en formato String.
     */
    private String date;


}
