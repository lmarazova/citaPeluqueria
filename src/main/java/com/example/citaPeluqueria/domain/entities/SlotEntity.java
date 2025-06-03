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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "franjas")
@BatchSize(size = 500)
public class SlotEntity extends BaseEntity{


    @Column
    private LocalDateTime startHour;

    @Column
    private LocalDateTime finalHour;

    @Column
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    @ManyToOne
    private HairdresserEntity hairdresser;

    @ManyToOne
    private ClientEntity client;

    @ManyToOne
    private ServiceEntity service;
    @ManyToOne
    private AppointmentEntity appointment;

}
