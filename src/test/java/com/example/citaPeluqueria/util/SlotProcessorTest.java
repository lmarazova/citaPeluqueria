package com.example.citaPeluqueria.util;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlotProcessorTest {
    @Test
    void generateHourRanges_shouldReturnCorrectRangesForValidCombinations() {
        // Mock de tres slots consecutivos
        SlotOutputDTO slot1 = new SlotOutputDTO();
        slot1.setSlotStartHour("09:00");
        slot1.setSlotFinalHour("09:15");

        SlotOutputDTO slot2 = new SlotOutputDTO();
        slot2.setSlotStartHour("09:15");
        slot2.setSlotFinalHour("09:30");

        SlotOutputDTO slot3 = new SlotOutputDTO();
        slot3.setSlotStartHour("09:30");
        slot3.setSlotFinalHour("09:45");

        List<List<SlotOutputDTO>> combinations = List.of(
                List.of(slot1, slot2, slot3)
        );

        List<String> result = SlotProcessor.generateHourRanges(combinations);

        assertEquals(1, result.size());
        assertEquals("09:00 - 09:45", result.get(0));
    }

    @Test
    void generateHourRanges_shouldSkipCombinationsWithLessThanThreeSlots() {
        SlotOutputDTO slot1 = new SlotOutputDTO();
        slot1.setSlotStartHour("10:00");
        slot1.setSlotFinalHour("10:15");

        SlotOutputDTO slot2 = new SlotOutputDTO();
        slot2.setSlotStartHour("10:15");
        slot2.setSlotFinalHour("10:30");

        List<List<SlotOutputDTO>> combinations = List.of(
                List.of(slot1, slot2) // Solo dos slots
        );

        List<String> result = SlotProcessor.generateHourRanges(combinations);

        assertTrue(result.isEmpty());
    }

    @Test
    void generateHourRanges_shouldHandleMultipleValidCombinations() {
        SlotOutputDTO a1 = new SlotOutputDTO();
        a1.setSlotStartHour("08:00");
        a1.setSlotFinalHour("08:15");

        SlotOutputDTO a2 = new SlotOutputDTO();
        a2.setSlotStartHour("08:15");
        a2.setSlotFinalHour("08:30");

        SlotOutputDTO a3 = new SlotOutputDTO();
        a3.setSlotStartHour("08:30");
        a3.setSlotFinalHour("08:45");

        SlotOutputDTO b1 = new SlotOutputDTO();
        b1.setSlotStartHour("11:00");
        b1.setSlotFinalHour("11:15");

        SlotOutputDTO b2 = new SlotOutputDTO();
        b2.setSlotStartHour("11:15");
        b2.setSlotFinalHour("11:30");

        SlotOutputDTO b3 = new SlotOutputDTO();
        b3.setSlotStartHour("11:30");
        b3.setSlotFinalHour("11:45");

        List<List<SlotOutputDTO>> combinations = List.of(
                List.of(a1, a2, a3),
                List.of(b1, b2, b3)
        );

        List<String> result = SlotProcessor.generateHourRanges(combinations);

        assertEquals(2, result.size());
        assertEquals("08:00 - 08:45", result.get(0));
        assertEquals("11:00 - 11:45", result.get(1));
    }

    @Test
    void generateHourRanges_shouldReturnEmptyListWhenInputIsEmpty() {
        List<List<SlotOutputDTO>> combinations = Collections.emptyList();

        List<String> result = SlotProcessor.generateHourRanges(combinations);

        assertTrue(result.isEmpty());
    }

    @Test
    void generateHourRanges_shouldReturnEmptyListWhenCombinationsAreEmptyLists() {
        List<List<SlotOutputDTO>> combinations = List.of(
                Collections.emptyList(),
                Collections.emptyList()
        );

        List<String> result = SlotProcessor.generateHourRanges(combinations);

        assertTrue(result.isEmpty());
    }
}