package com.example.citaPeluqueria.util;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para procesar combinaciones de slots y generar rangos de horas legibles.
 */
@Component
@Getter
@Setter
public class SlotProcessor {

    /**
     * Genera rangos horarios a partir de combinaciones de slots disponibles.
     *
     * Cada combinación representa una secuencia de slots continuos que juntos componen un servicio.
     * Este método toma el inicio del primer slot y el final del último para formar un rango.
     *
     * @param availableSlotCombinations lista de combinaciones de slots disponibles.
     * @return lista de rangos horarios como cadenas (ej: "09:00 - 09:45").
     */

    public static List<String> generateHourRanges(List<List<SlotOutputDTO>> availableSlotCombinations) {
        // Lista donde vamos a almacenar los rangos de horas como cadenas
        List<String> hourRanges = new ArrayList<>();

        // Iterar sobre las combinaciones de franjas
        for (List<SlotOutputDTO> combination : availableSlotCombinations) {
            // Asegurarse de que hay al menos 3 franjas para que no cause IndexOutOfBounds
            if (combination.size() >= 3) {
                // Obtener el slotStartHour de la primera franja y el slotFinalHour de la última franja
                String startHour = combination.getFirst().getSlotStartHour();
                String finalHour = combination.getLast().getSlotFinalHour();

                // Crear la cadena con el formato requerido
                String hourRange = startHour + " - " + finalHour;

                // Añadir a la lista de resultados
                hourRanges.add(hourRange);
            }
        }

        return hourRanges;
    }

}
