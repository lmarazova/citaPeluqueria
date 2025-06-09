package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
/**
 * Implementación de ClientService.
 * Maneja el registro de usuarios, gestión de invitados, recuperación por teléfono
 * y el envío de correos electrónicos para verificación de cuentas.
 */
@Service
public class ClientServiceImpl implements ClientService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    /**
     * Registra un nuevo usuario codificando su contraseña y enviando un correo de verificación.
     */
    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {

        if(clientRepository.existsByEmail(userRegisterDTO.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }

        if(clientRepository.findByPhone(userRegisterDTO.getPhone()) != null){
            throw new IllegalArgumentException("Phone already in use");
        }
        ClientEntity user = modelMapper.map(userRegisterDTO, ClientEntity.class);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRoles(Set.of(Role.USER));
        clientRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setClient(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(verificationToken);

        sendVerificationEmail(user.getEmail(), token);
    }

    /**
     * Busca o crea un cliente con rol de invitado si no existe.
     */
    @Override
    public ClientEntity findOrCreateGuest(String username, String phone) {

        ClientEntity existingUser = clientRepository.findByPhone(phone);
        if(existingUser != null){
            return existingUser;
        }
        ClientEntity newUser = new ClientEntity();
        newUser.setUsername(username);
        newUser.setPhone(phone);
        newUser.setGuest(true);
        newUser.setRoles(Set.of(Role.GUEST));

        return clientRepository.save(newUser);
    }

    /**
     * Devuelve un cliente según su número de teléfono.
     */
    @Override
    public ClientEntity findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    /**
     * Envía un correo electrónico con un enlace de activación.
     */
    @Override
    public void sendVerificationEmail(String email, String token) {
        String url = "http://localhost:8082/activate?token=" + token;
        String subject = "Activa tu cuenta";
        String content = "Haz clic en el siguiente enlace para activar tu cuenta:\n" + url;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }


}
