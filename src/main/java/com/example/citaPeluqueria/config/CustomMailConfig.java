package com.example.citaPeluqueria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
/**
 * Configuración personalizada para el envío de correos electrónicos mediante SMTP.
 *
 * Define un bean {@link JavaMailSender} configurado para usar el servidor SMTP de Gmail.
 * Incluye las propiedades necesarias para autenticación y seguridad TLS.
 */
@Configuration
public class CustomMailConfig {
    /**
     * Crea y configura un bean de {@link JavaMailSender} para el envío de emails.
     *
     * @return una instancia configurada de JavaMailSender.
     */
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuración del servidor SMTP de Gmail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("soporteempresatressc@gmail.com");
        mailSender.setPassword("ffej jilx spgx tmhx");

        // Propiedades adicionales para la conexión SMTP segura
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");  // <== confía en cualquier certificado
        props.put("mail.debug", "true");

        return mailSender;
    }
}
