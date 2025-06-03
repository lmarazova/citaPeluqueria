package com.example.citaPeluqueria.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CustomServicePackageDTO {
    private Long id;
    private String label;
}
