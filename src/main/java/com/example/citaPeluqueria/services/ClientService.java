package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
/**
 * Servicio para gestionar operaciones relacionadas con clientes y usuarios registrados o invitados.
 */
public interface ClientService {

    /**
     * Registra un nuevo usuario, codifica su contraseña, asigna roles y envía un correo de verificación.
     *
     * @param userRegisterDTO DTO con los datos del usuario a registrar.
     */
    void registerUser(UserRegisterDTO userRegisterDTO);

    /**
     * Busca un cliente por teléfono o lo crea como invitado si no existe.
     *
     * @param username nombre del invitado.
     * @param phone teléfono del invitado.
     * @return entidad del cliente existente o recién creado.
     */
    public ClientEntity findOrCreateGuest(String username, String phone);

    /**
     * Busca un cliente por su número de teléfono.
     *
     * @param phone número de teléfono.
     * @return entidad del cliente, o null si no se encuentra.
     */
    ClientEntity findByPhone(String phone);

    /**
     * Envía un correo de verificación con un token de activación al usuario.
     *
     * @param email correo electrónico del destinatario.
     * @param token token de activación.
     */
    void sendVerificationEmail(String email, String token);
}
