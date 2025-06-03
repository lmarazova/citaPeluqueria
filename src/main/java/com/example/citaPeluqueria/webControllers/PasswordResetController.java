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

@Controller
public class PasswordResetController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

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
