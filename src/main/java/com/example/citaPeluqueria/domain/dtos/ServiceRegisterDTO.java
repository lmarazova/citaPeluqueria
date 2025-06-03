package com.example.citaPeluqueria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ServiceRegisterDTO {
    String displayName;
    int durationMinutes;
    List<String>slotPattern;
    double price; 
}
