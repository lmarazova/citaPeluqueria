package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.HairdresserRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.mapper.HairdresserMapper;
import com.example.citaPeluqueria.mapper.SlotMapper;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HairDresserRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HairdresserRepository hairdresserRepository;

    @Mock
    private SlotMapper slotMapper;

    @Mock
    private HairdresserMapper hairdresserMapper;

    @InjectMocks
    private HairdresserRestController hairdresserRestController;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        hairdresserRestController = new HairdresserRestController(hairdresserRepository);

        // Inyectamos los mocks en los campos privados
        ReflectionTestUtils.setField(hairdresserRestController, "slotMapper", slotMapper);
        ReflectionTestUtils.setField(hairdresserRestController, "hairdresserMapper", hairdresserMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(hairdresserRestController).build();
    }

    @Test
    void getHairdresserById_whenExists_shouldReturnHairdresserDTO() throws Exception {
        Long id = 1L;
        HairdresserEntity entity = new HairdresserEntity();
        entity.setId(id);
        entity.setUsername("ana");
        entity.setEmail("ana@mail.com");

        HairdresserRestDTO dto = new HairdresserRestDTO();
        dto.setId(id);
        dto.setUsername("ana");
        dto.setEmail("ana@mail.com");

        when(hairdresserRepository.findById(id)).thenReturn(Optional.of(entity));
        when(hairdresserMapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/hairdressers/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value("ana"))
                .andExpect(jsonPath("$.email").value("ana@mail.com"));
    }

    @Test
    void getHairdresserById_whenNotFound_shouldReturn404() throws Exception {
        Long id = 1L;
        when(hairdresserRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/hairdressers/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllHairdressers_shouldReturnListOfHairdressersWithSlots() throws Exception {
        HairdresserEntity h1 = new HairdresserEntity();
        h1.setId(1L);
        h1.setUsername("maria");
        h1.setEmail("maria@mail.com");

        SlotEntity s1 = new SlotEntity();
        s1.setId(10L);
        SlotEntity s2 = new SlotEntity();
        s2.setId(5L);
        h1.setSlots(new HashSet<>(Arrays.asList(s1, s2)));

        when(hairdresserRepository.findAll()).thenReturn(List.of(h1));

        // Creamos mocks de SlotRestDTO que devuelven los valores necesarios
        SlotRestDTO slotDTO1 = mock(SlotRestDTO.class);
        when(slotDTO1.getId()).thenReturn(5L);
        SlotRestDTO slotDTO2 = mock(SlotRestDTO.class);
        when(slotDTO2.getId()).thenReturn(10L);

        // Mapeamos según orden de slots
        when(slotMapper.toDTO(s1)).thenReturn(slotDTO2); // s1 tiene id=10
        when(slotMapper.toDTO(s2)).thenReturn(slotDTO1); // s2 tiene id=5

        mockMvc.perform(get("/api/hairdressers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("maria"))
                .andExpect(jsonPath("$[0].email").value("maria@mail.com"))
                .andExpect(jsonPath("$[0].slots[0].id").value(5))
                .andExpect(jsonPath("$[0].slots[1].id").value(10));
    }

}
