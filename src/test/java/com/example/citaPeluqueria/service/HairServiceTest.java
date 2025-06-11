package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.services.HairServiceService;
import com.example.citaPeluqueria.services.HairServiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class HairServiceTest {
    @Mock
    private final HairServiceServiceImpl hairServiceService = new HairServiceServiceImpl();

    @Test
    void fromLabel_shouldReturnNull_whenLabelIsCustom() {
        // Etiqueta que no está en el enum => servicio custom
        assertNull(hairServiceService.fromLabel("ServicioCustomNoExistente"));
    }

    @Test
    void fromLabel_shouldReturnNull_whenLabelIsNullOrEmpty() {
        assertNull(hairServiceService.fromLabel(null));
        assertNull(hairServiceService.fromLabel(""));
        assertNull(hairServiceService.fromLabel("    "));
    }

    @Test
    void fromLabel_shouldReturnEnum_whenLabelMatchesBasicService() {
        HairServiceService hairServiceService = new HairServiceServiceImpl();

        // Usa el label definido en el enum CUT para evitar errores por diferencias de texto
        String inputLabel = HairService.CUT.getLabel();

        HairService result = hairServiceService.fromLabel(inputLabel);

        assertEquals(HairService.CUT, result);
    }
}
