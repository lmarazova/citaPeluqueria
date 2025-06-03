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
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="clients")
public class ClientEntity extends UserEntity{

    @Column(nullable = false, unique = true)
    private String phone;
    @Column(nullable = false)
    private boolean guest = false;

    @Column(columnDefinition = "TEXT")
    private String comments;
    @OneToMany(mappedBy = "client")
    private Set<SlotEntity>slots;
    @OneToMany(mappedBy = "user")
    private Set<AppointmentEntity>appointments;
    @Column
    private boolean enabled = false;
}
