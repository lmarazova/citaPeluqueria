package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends BaseEntity{
    private String token;
    @OneToOne
    private ClientEntity client;
    private LocalDateTime expiryDate;
}
