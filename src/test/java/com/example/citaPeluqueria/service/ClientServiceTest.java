package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import com.example.citaPeluqueria.services.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    private UserRegisterDTO userRegisterDTO;

    private ClientEntity mappedClient;
    @Mock
    private ClientRepository clientRepository;
    @Spy
    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    public ModelMapper modelMapper;
    @Mock
    public PasswordEncoder passwordEncoder;
    @Mock
    public VerificationTokenRepository verificationTokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("Maria");
        userRegisterDTO.setPhone("123456789");
        userRegisterDTO.setEmail("maria@gmail.com");
        userRegisterDTO.setPassword("1234");

        mappedClient = new ClientEntity();
        mappedClient.setUsername(userRegisterDTO.getUsername());
        mappedClient.setEmail(userRegisterDTO.getEmail());

    }

    @Test
    void registerUser_shouldThrowException_whenEmailExists() {
        when(clientRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.registerUser(userRegisterDTO);
        });
        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    void registerUser_shouldThrowException_whenPhoneExists() {
        when(clientRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(false);
        when(clientRepository.findByPhone(userRegisterDTO.getPhone())).thenReturn(new ClientEntity());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clientService.registerUser(userRegisterDTO);
        });

        assertEquals("Phone already in use", exception.getMessage());
    }

    @Test
    void registerUser_shouldSaveUserAndSendEmail_whenDataIsValid() {
        when(clientRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(false);
        when(clientRepository.findByPhone(userRegisterDTO.getPhone())).thenReturn(null);
        when(modelMapper.map(userRegisterDTO, ClientEntity.class)).thenReturn(mappedClient);
        when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("encodedPassword");

        doAnswer(invocation -> {
            ClientEntity savedUser = invocation.getArgument(0);
            assertEquals("encodedPassword", savedUser.getPassword());
            assertTrue(savedUser.getRoles().contains(Role.USER));
            return null;
        }).when(clientRepository).save(any(ClientEntity.class));

        doAnswer(invocation -> {
            VerificationToken token = invocation.getArgument(0);
            assertNotNull(token.getToken());
            assertEquals(mappedClient, token.getClient());
            return null;
        }).when(verificationTokenRepository).save(any(VerificationToken.class));


        doNothing().when(clientService).sendVerificationEmail(anyString(), anyString());

        clientService.registerUser(userRegisterDTO);

        verify(clientRepository).save(any(ClientEntity.class));
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(clientService).sendVerificationEmail(eq(userRegisterDTO.getEmail()), anyString());
    }
    @Test
    void findOrCreateGuest_shouldReturnExistingUser_whenUserWithPhoneExists() {
        // Simula que hay un usuario con el teléfono dado
        ClientEntity existingUser = new ClientEntity();
        existingUser.setPhone("123456789");

        when(clientRepository.findByPhone("123456789")).thenReturn(existingUser);

        // Ejecuta el método y espera que devuelva el usuario existente
        ClientEntity result = clientService.findOrCreateGuest("Maria", "123456789");

        // Comprueba que se devolvió el usuario que ya existía
        assertEquals(existingUser, result);

        // Verifica que no se intentó guardar un nuevo usuario
        verify(clientRepository, never()).save(any());
    }

    @Test
    void findOrCreateGuest_shouldCreateAndSaveGuestUser_whenUserDoesNotExist() {
        // Simula que NO hay ningún usuario con ese teléfono
        when(clientRepository.findByPhone("987654321")).thenReturn(null);

        // Captura el usuario que se va a guardar en la base de datos
        ArgumentCaptor<ClientEntity> captor = ArgumentCaptor.forClass(ClientEntity.class);

        // Simula que al guardar devuelve un usuario (puede ser un mock o nuevo objeto)
        ClientEntity savedUser = new ClientEntity();
        when(clientRepository.save(any())).thenReturn(savedUser);

        // Ejecuta el método para crear un nuevo invitado
        ClientEntity result = clientService.findOrCreateGuest("Luis", "987654321");

        // Verifica que se llamó al método save para guardar el nuevo usuario
        verify(clientRepository).save(captor.capture());

        ClientEntity created = captor.getValue();

        // Comprueba que el nuevo usuario tiene los datos correctos y rol invitado
        assertEquals("Luis", created.getUsername());
        assertEquals("987654321", created.getPhone());
        assertTrue(created.isGuest());
        assertTrue(created.getRoles().contains(Role.GUEST));

        // Comprueba que el resultado del método es el usuario guardado
        assertEquals(savedUser, result);
    }

    @Test
    void findByPhone_shouldReturnUser_whenUserExists() {
        // Dado un usuario existente en el repositorio para un teléfono dado
        ClientEntity existingUser = new ClientEntity();
        when(clientRepository.findByPhone("123")).thenReturn(existingUser);

        // Cuando se llama al servicio para buscar por teléfono
        ClientEntity result = clientService.findByPhone("123");

        // Entonces debe devolver el usuario encontrado
        assertEquals(existingUser, result);
    }
    @Test
    void findByPhone_shouldReturnNull_whenUserDoesNotExist() {
        // Dado que no existe usuario con el teléfono solicitado
        when(clientRepository.findByPhone("456")).thenReturn(null);

        // Cuando se busca un usuario por ese teléfono
        ClientEntity result = clientService.findByPhone("456");

        // Entonces debe devolver null (no encontrado)
        assertNull(result);
    }
    @Test
    void sendVerificationEmail_shouldSendEmailWithCorrectContent() {
        String email = "test@example.com";
        String token = "token123";

        // Cuando se invoca el método para enviar email de verificación
        clientService.sendVerificationEmail(email, token);

        // Capturamos el mensaje que se envía para comprobarlo
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        // Verificamos que el email se envió a la dirección correcta
        assertEquals(email, message.getTo()[0]);

        // Verificamos que el asunto es el esperado
        assertEquals("Activa tu cuenta", message.getSubject());

        // Verificamos que el cuerpo del mensaje contiene el token de activación
        assertTrue(message.getText().contains(token));
    }

}
