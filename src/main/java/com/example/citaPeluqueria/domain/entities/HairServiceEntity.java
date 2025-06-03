package com.example.citaPeluqueria.domain.entities;

import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="servicios básicos")
public class HairServiceEntity extends BaseEntity{
    @Column
    @Enumerated(EnumType.STRING)
    private HairService hairService;
    private String customLabel;


}
