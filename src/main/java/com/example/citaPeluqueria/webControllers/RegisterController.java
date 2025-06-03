package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;

    public RegisterController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/register")
    public String showRegisterController(Model model, UserRegisterDTO userRegisterDTO){
        model.addAttribute("userRegisterDTO", userRegisterDTO);
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult,
                               Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            return "register";
        }
        if(clientRepository.existsByEmail(userRegisterDTO.getEmail())){
            model.addAttribute("error", "Este correo ya está registrado");
            return "register";
        }
        if(clientRepository.existsByPhone(userRegisterDTO.getPhone())){
            model.addAttribute("error", "Este teléfono ya está registrado");
        }
        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())){
            model.addAttribute("error", "las contraseñas elegidas no coinciden");
            return "register";
        }
        clientService.registerUser(userRegisterDTO);
        return "redirect:/login";
    }
}
