package com.example.citaPeluqueria.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * Entidad que representa un token de verificación asociado a un cliente.
 *
 * Se utiliza típicamente para procesos como la activación de cuenta o la
 * verificación de correo electrónico. Contiene el token generado, la referencia
 * al cliente al que pertenece, y la fecha de expiración del token.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends BaseEntity{
    /**
     * Token único generado para la verificación.
     */
    private String token;

    /**
     * Cliente asociado a este token de verificación.
     */
    @OneToOne
    private ClientEntity client;

    /**
     * Fecha y hora en que el token expira y deja de ser válido.
     */
    private LocalDateTime expiryDate;
}
