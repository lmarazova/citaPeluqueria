package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.dtos.HairdresserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.HairdresserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class RegisterModeratorController {
    @Autowired
    private HairdresserRegisterDTO hairdresserRegisterDTO;
    @Autowired
    private HairdresserService hairdresserService;
    @Autowired
    private HairdresserRepository hairdresserRepository;
    @Autowired
    private ClientRepository clientRepository;


    @GetMapping("/register-moderator")
    public String showRegisterModerator(@RequestParam(value = "self", required = false) Boolean self,
                                        Model model,
                                        Principal principal) {
        HairdresserRegisterDTO dto = new HairdresserRegisterDTO();

        // Si se registra él mismo como peluquero, precargar datos
        if (Boolean.TRUE.equals(self) && principal != null) {
            String username = principal.getName();
            ClientEntity user = clientRepository.findByUsername(username); // puedes usar Optional también aquí
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
            }
        }

        model.addAttribute("hairdresserRegisterDTO", dto);
        return "register-moderator";
    }
    @PostMapping("/register-moderator")
    public String registerUser(@Valid HairdresserRegisterDTO hairdresserRegisterDTO,
                               BindingResult bindingResult,
                               Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("hairdresserRegisterDTO", hairdresserRegisterDTO);
            return "register-moderator";
        }
        if(hairdresserRepository.existsByEmail(hairdresserRegisterDTO.getEmail())){
            model.addAttribute("error", "Este correo ya está registrado");
            return "register-moderator";
        }
        if(!hairdresserRegisterDTO.getPassword().equals(hairdresserRegisterDTO.getConfirmPassword())){
            model.addAttribute("error", "las contraseñas elegidas no coinciden");
            return "register-moderator";
        }
        hairdresserService.registerHairdresser(hairdresserRegisterDTO);
        return "redirect:/admin";
    }
}

