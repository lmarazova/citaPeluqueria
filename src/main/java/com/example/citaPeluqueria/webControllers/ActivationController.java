package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ActivationController {
    @Autowired
    private VerificationTokenRepository tokenRepository;


    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @GetMapping("/activate")
    public String activateUser(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        System.out.println("Activando usuario con token: " + token);

        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o caducado");
            return "redirect:/login";
        }

        ClientEntity client = verificationToken.getClient();
        client.setEnabled(true);
        clientRepository.save(client);
        System.out.println("Usuario activado: " + client.getEmail());

        tokenRepository.delete(verificationToken);

        redirectAttributes.addFlashAttribute("success", "Cuenta activada correctamente");
        return "redirect:/login";
    }
}
