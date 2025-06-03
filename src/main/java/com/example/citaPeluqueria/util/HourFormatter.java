package com.example.citaPeluqueria.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class HourFormatter {
    public static Set<String> formatToHourOnly(Set<String> dateTimeRanges) {
        return dateTimeRanges.stream()
                .map(range -> {
                    String[] parts = range.split(" - ");
                    String start = parts[0].substring(11, 16); // hh:mm
                    String end = parts[1].substring(11, 16);   // hh:mm
                    return start + " - " + end;
                })
                .collect(Collectors.toCollection(TreeSet::new)); // Mantiene orden
    }
}
