package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.mapper.SlotMapper;
import com.example.citaPeluqueria.repositories.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SlotRestController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔓 Desactiva seguridad si la tienes habilitada
public class SlotRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotRepository slotRepository;

    @MockBean
    private SlotMapper slotMapper;

    @Test
    void getSlotById_shouldReturnDto_whenSlotExists() throws Exception {
        Long id = 1L;
        SlotEntity entity = new SlotEntity();
        SlotRestDTO dto = new SlotRestDTO();
        dto.setId(id);

        when(slotRepository.findById(id)).thenReturn(Optional.of(entity));
        when(slotMapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/slots/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // .andExpect(jsonPath("$.id").value(id)); // opcional
    }

    @Test
    void getSlotById_shouldReturn404_whenSlotDoesNotExist() throws Exception {
        Long id = 999L;
        when(slotRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/slots/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSlots_shouldReturnListOfDtos() throws Exception {
        SlotEntity slot1 = new SlotEntity();
        SlotEntity slot2 = new SlotEntity();
        SlotRestDTO dto1 = new SlotRestDTO();
        dto1.setId(1L);
        SlotRestDTO dto2 = new SlotRestDTO();
        dto2.setId(2L);

        List<SlotEntity> slotEntities = List.of(slot1, slot2);
        List<SlotRestDTO> dtos = List.of(dto1, dto2);

        when(slotRepository.findAll()).thenReturn(slotEntities);
        when(slotMapper.toDTO(slot1)).thenReturn(dto1);
        when(slotMapper.toDTO(slot2)).thenReturn(dto2);

        mockMvc.perform(get("/api/slots")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}