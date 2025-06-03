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
@Table(name="peluquero")
public class HairdresserEntity extends UserEntity{
    @OneToMany(mappedBy = "hairdresser")
    private Set<SlotEntity>slots;
    @Column
    private boolean isAdmin;


}
