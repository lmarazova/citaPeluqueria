package com.example.citaPeluqueria.domain.entities;

import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="servicio")
public class ServiceEntity extends BaseEntity{
    @Column
    @Enumerated(EnumType.STRING)
    private ServicePackageEnum packageEnum;
    @Column
    private double price;
    @Column
    private int totalDuration;
    @Column
    private String description;
    @Column
    private boolean isActive = true;
    @ElementCollection(targetClass = HairService.class)
    @Enumerated(EnumType.STRING)
    private List<HairService>services;
    @OneToMany(mappedBy = "service")
    private Set<SlotEntity>slots;
    @OneToMany(mappedBy = "service")
    private Set<AppointmentEntity>appointments;

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "totalDuracion=" + totalDuration +
                ", name='" + (packageEnum != null ? packageEnum.getDisplayName() : "null") + '\'' +
                ", durationMinutes='" + (packageEnum != null ? packageEnum.getDurationMinutes() : "null") + '\'' +
                ", slotPattern='" + (packageEnum != null
                ? packageEnum.getSlotPattern().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                : "null") + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", services='" + (services != null
                ? services.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                : "null") + '\'' +
                '}';
    }



}
