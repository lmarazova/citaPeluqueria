package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Controlador para la gestión del restablecimiento de contraseñas.
 * Permite a los usuarios solicitar un enlace de recuperación y cambiar su contraseña.
 */
@Controller
public class PasswordResetController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Muestra la página para solicitar el restablecimiento de contraseña.
     *
     * @return Nombre de la vista "forgot-password".
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    /**
     * Procesa la solicitud de restablecimiento de contraseña.
     *
     * @param email Correo electrónico del usuario que solicita el restablecimiento.
     * @param redirectAttributes Atributos para mensajes de feedback en la redirección.
     * @return Redirección a la página de inicio de sesión o solicitud de restablecimiento.
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        RedirectAttributes redirectAttributes) {
        ClientEntity client = clientRepository.findByEmail(email);
        if (client == null) {
            redirectAttributes.addFlashAttribute("error", "No hay cuenta registrada con ese email.");
            return "redirect:/forgot-password";
        }

        // Crear token y guardar
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setClient(client);
        vt.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(vt);

        // Construir link
        String resetLink = "http://localhost:8082/reset-password?token=" + token;

        // Enviar correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(client.getEmail());
        message.setSubject("Restablecer contraseña");
        message.setText("Haz clic aquí para restablecer tu contraseña: " + resetLink);
        mailSender.send(message);

        redirectAttributes.addFlashAttribute("success", "Se ha enviado un correo con instrucciones.");
        return "redirect:/login";
    }

    /**
     * Muestra el formulario de restablecimiento de contraseña si el token es válido.
     *
     * @param token Token de verificación para validar la solicitud.
     * @param model Modelo para pasar información a la vista.
     * @return Nombre de la vista "reset-password".
     */
    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        VerificationToken vt = tokenRepository.findByToken(token);
        if (vt == null || vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "El token es inválido o ha expirado.");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }


    /**
     * Maneja la actualización de la contraseña del usuario.
     *
     * @param token Token de verificación recibido por el usuario.
     * @param password Nueva contraseña ingresada.
     * @param confirmPassword Confirmación de la nueva contraseña.
     * @param redirectAttributes Atributos para mensajes de feedback en la redirección.
     * @return Redirección a la página de inicio de sesión o a la solicitud de restablecimiento.
     */
    @PostMapping("/reset-password")
    @Transactional
    public String handlePasswordReset(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/reset-password?token=" + token;
        }

        VerificationToken vt = tokenRepository.findByToken(token);
        if (vt == null || vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/forgot-password";
        }

        ClientEntity client = vt.getClient();
        client.setPassword(new BCryptPasswordEncoder().encode(password));
        clientRepository.save(client);

        tokenRepository.delete(vt);

        redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente.");
        return "redirect:/login";
    }

}
