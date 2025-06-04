package com.example.citaPeluqueria.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Clase de configuración principal de la aplicación.
 *
 * - Habilita la gestión de transacciones con {@link EnableTransactionManagement}.
 * - Define un bean {@link ModelMapper} para facilitar el mapeo automático entre DTOs y entidades.
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {
    /**
     * Proporciona un bean de {@link ModelMapper} para la conversión y mapeo de objetos.
     *
     * @return instancia de ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
