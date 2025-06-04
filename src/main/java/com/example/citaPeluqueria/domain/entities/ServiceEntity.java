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
/**
 * Entidad que representa un servicio ofrecido en la aplicación.
 *
 * Un servicio está asociado a un paquete predefinido {@link ServicePackageEnum},
 * que indica duración, patrón de franjas horarias, y nombre para mostrar.
 * Además, contiene el precio, duración total (que puede coincidir o no con el paquete),
 * una descripción libre, y una lista de servicios básicos {@link HairService} que incluye.
 *
 * También mantiene relaciones con las franjas horarias ({@link SlotEntity}) y
 * las citas ({@link AppointmentEntity}) en las que se reserva este servicio.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="servicio")
public class ServiceEntity extends BaseEntity{
    /**
     * Paquete al que pertenece el servicio, que define características como duración y patrón de slots.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ServicePackageEnum packageEnum;
    /**
     * Precio del servicio.
     */
    @Column
    private double price;
    /**
     * Duración total del servicio en minutos.
     */
    @Column
    private int totalDuration;
    /**
     * Descripción textual que detalla características del servicio.
     */
    @Column
    private String description;

    /**
     * Indica si el servicio está activo y disponible para reserva.
     */
    @Column
    private boolean isActive = true;

    /**
     * Lista de servicios básicos {@link HairService} que componen este servicio.
     * Por ejemplo: corte, lavado, secado, etc.
     */
    @ElementCollection(targetClass = HairService.class)
    @Enumerated(EnumType.STRING)
    private List<HairService>services;

    /**
     * Conjunto de franjas horarias ({@link SlotEntity}) asociadas a este servicio.
     */
    @OneToMany(mappedBy = "service")
    private Set<SlotEntity>slots;

    /**
     * Conjunto de citas ({@link AppointmentEntity}) en las que se ha reservado este servicio.
     */
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
