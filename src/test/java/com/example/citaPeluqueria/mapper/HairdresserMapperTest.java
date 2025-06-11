package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.HairdresserRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HairdresserMapperTest {

    @InjectMocks
    private HairdresserMapper hairdresserMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SlotMapper slotMapper;

    @Test
    void toDTO_shouldMapEntityToDTOWithSortedSlots() {
        HairdresserEntity entity = new HairdresserEntity();
        entity.setId(10L);
        entity.setUsername("juan");
        entity.setEmail("juan@mail.com");

        SlotRestDTO slot1 = new SlotRestDTO();
        slot1.setId(2L);
        SlotRestDTO slot2 = new SlotRestDTO();
        slot2.setId(1L);

        SlotEntity slotEntity1 = new SlotEntity();
        SlotEntity slotEntity2 = new SlotEntity();

        entity.setSlots(new HashSet<>(List.of(slotEntity1, slotEntity2)));

        // Mock slotMapper.toDTO to return slot DTOs for the entities (order irrelevant)
        when(slotMapper.toDTO(slotEntity1)).thenReturn(slot1);
        when(slotMapper.toDTO(slotEntity2)).thenReturn(slot2);

        HairdresserRestDTO dto = hairdresserMapper.toDTO(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getEmail(), dto.getEmail());

        // Should be sorted by id ascending (1, 2)
        assertEquals(2, dto.getSlots().size());
        assertEquals(1L, dto.getSlots().get(0).getId());
        assertEquals(2L, dto.getSlots().get(1).getId());
    }

    @Test
    void toDTO_shouldHandleNullSlotsGracefully() {
        HairdresserEntity entity = new HairdresserEntity();
        entity.setId(5L);
        entity.setUsername("ana");
        entity.setEmail("ana@mail.com");
        entity.setSlots(null); // No slots

        HairdresserRestDTO dto = hairdresserMapper.toDTO(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertNotNull(dto.getSlots());
        assertTrue(dto.getSlots().isEmpty());
    }

    @Test
    void toDTOList_shouldMapListOfEntities() {
        HairdresserEntity entity1 = new HairdresserEntity();
        entity1.setId(1L);
        HairdresserEntity entity2 = new HairdresserEntity();
        entity2.setId(2L);

        // Spy the mapper to verify calls to toDTO
        HairdresserMapper spyMapper = spy(hairdresserMapper);

        doReturn(new HairdresserRestDTO()).when(spyMapper).toDTO(entity1);
        doReturn(new HairdresserRestDTO()).when(spyMapper).toDTO(entity2);

        List<HairdresserRestDTO> result = spyMapper.toDTOList(List.of(entity1, entity2));

        assertEquals(2, result.size());
        verify(spyMapper).toDTO(entity1);
        verify(spyMapper).toDTO(entity2);
    }

    @Test
    void toEntity_shouldUseModelMapper() {
        HairdresserRestDTO dto = new HairdresserRestDTO();
        HairdresserEntity entity = new HairdresserEntity();

        when(modelMapper.map(dto, HairdresserEntity.class)).thenReturn(entity);

        HairdresserEntity result = hairdresserMapper.toEntity(dto);

        verify(modelMapper).map(dto, HairdresserEntity.class);
        assertSame(entity, result);
    }
}

