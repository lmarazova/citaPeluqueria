package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.enums.HairService;
import org.springframework.stereotype.Service;
/**
 * Implementación de {@link HairServiceService} que permite obtener valores del enum {@link HairService}
 * a partir de su etiqueta textual.
 */
@Service
public class HairServiceServiceImpl implements HairServiceService{

    /**
     * Busca un valor del enum {@link HairService} cuyo label coincida (ignorando mayúsculas/minúsculas)
     * con el proporcionado.
     *
     * @param label La etiqueta textual del servicio (por ejemplo, "Corte").
     * @return El valor correspondiente de {@link HairService}, o {@code null} si no se encuentra.
     */
    @Override
    public HairService fromLabel(String label) {
        for(HairService service : HairService.values()){
            if(service.getLabel().equalsIgnoreCase(label.trim())){
                return service;
            }
        }
        return null;
    }
}
