package com.example.citaPeluqueria.domain.entities;

import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entidad que representa un servicio de peluquería básico.
 *
 * Esta entidad contiene un tipo predefinido del enum HairService,
 * y un label personalizado que puede ser definido por el administrador.
 *
 * Ejemplo: un servicio predefinido "CUT" con un customLabel "Corte de Verano".
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="servicios básicos")
public class HairServiceEntity extends BaseEntity{
    /**
     * Tipo de servicio predefinido (ej. CUT, COLOR, etc).
     * Se almacena como String en la base de datos.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private HairService hairService;
    /**
     * Etiqueta personalizada para el servicio,
     * para mostrar en la app algo distinto al enum si se desea.
     */
    private String customLabel;


}
