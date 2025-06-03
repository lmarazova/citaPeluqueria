package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="citas")
public class AppointmentEntity extends BaseEntity{
    @ManyToOne
    private ClientEntity user;
    @ManyToOne
    private ServiceEntity service;
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<SlotEntity> slots;
    private String selectedHourRange;
    private String date;


}
