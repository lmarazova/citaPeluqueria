package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.dtos.HairdresserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.services.HairdresserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HairdresserServiceTest {
    @Mock
    private HairdresserRepository hairdresserRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private HairdresserServiceImpl hairdresserService;

    @Test
    void registerHairdresser_shouldMapEncodeAndSave() {
        HairdresserRegisterDTO dto = new HairdresserRegisterDTO();
        dto.setPassword("plainPassword");

        HairdresserEntity mappedEntity = new HairdresserEntity();

        when(modelMapper.map(dto, HairdresserEntity.class)).thenReturn(mappedEntity);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        // Ejecutamos el método bajo prueba
        hairdresserService.registerHairdresser(dto);

        // Verificamos que se setea la contraseña codificada
        assertEquals("encodedPassword", mappedEntity.getPassword());

        // Verificamos que se asigna el rol MODERATOR
        assertTrue(mappedEntity.getRoles().contains(Role.MODERATOR));

        // Verificamos que se guarda en el repositorio
        verify(hairdresserRepository).save(mappedEntity);
    }
}
