package com.example.citaPeluqueria.util;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HourFormatterTest {
    @Test
    void formatToHourOnly_shouldReturnFormattedHourRanges() {
    Set<String> input = Set.of(
            "2025-06-01T09:00 - 2025-06-01T09:30",
            "2025-06-01T11:00 - 2025-06-01T11:30",
            "2025-06-01T10:00 - 2025-06-01T10:30"
    );

    Set<String> expected = new TreeSet<>(List.of(
            "09:00 - 09:30",
            "10:00 - 10:30",
            "11:00 - 11:30"
    ));

    Set<String> result = HourFormatter.formatToHourOnly(input);

    assertEquals(expected, result);
}

    @Test
    void formatToHourOnly_shouldReturnEmptySetIfInputIsEmpty() {
        Set<String> input = Collections.emptySet();

        Set<String> result = HourFormatter.formatToHourOnly(input);

        assertTrue(result.isEmpty());
    }

    @Test
    void formatToHourOnly_shouldMaintainOrder() {
        Set<String> input = Set.of(
                "2025-06-01T13:00 - 2025-06-01T13:30",
                "2025-06-01T08:00 - 2025-06-01T08:30",
                "2025-06-01T10:00 - 2025-06-01T10:30"
        );

        List<String> resultList = new ArrayList<>(HourFormatter.formatToHourOnly(input));

        assertEquals("08:00 - 08:30", resultList.get(0));
        assertEquals("10:00 - 10:30", resultList.get(1));
        assertEquals("13:00 - 13:30", resultList.get(2));
    }

    @Test
    void formatToHourOnly_shouldHandleSingleEntry() {
        Set<String> input = Set.of("2025-06-01T14:15 - 2025-06-01T14:45");

        Set<String> expected = Set.of("14:15 - 14:45");

        Set<String> result = HourFormatter.formatToHourOnly(input);

        assertEquals(expected, result);
    }
}