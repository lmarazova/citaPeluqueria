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
/**
 * Controlador para gestionar el registro de peluqueros/moderadores.
 * Permite a los usuarios registrarse como peluqueros y precargar datos si ya están logueados.
 */
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


    /**
     * Muestra el formulario de registro de peluquero/moderador.
     *
     * @param self Indica si el usuario se está registrando a sí mismo como peluquero.
     * @param model Modelo para pasar información a la vista.
     * @param principal Usuario autenticado.
     * @return Nombre de la vista de registro de moderador ("register-moderator").
     */
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

    /**
     * Procesa el registro de un peluquero/moderador.
     *
     * @param hairdresserRegisterDTO Datos del peluquero a registrar.
     * @param bindingResult Resultado de la validación del formulario.
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la página de administrador si el registro es exitoso,
     *         o muestra el formulario nuevamente si hay errores.
     */
    @PostMapping("/register-moderator")
    public String registerUser(@Valid HairdresserRegisterDTO hairdresserRegisterDTO,
                               BindingResult bindingResult,
                               Model model){
        // Validar errores en el formulario
        if(bindingResult.hasErrors()){
            model.addAttribute("hairdresserRegisterDTO", hairdresserRegisterDTO);
            return "register-moderator";
        }
        // Verificar si el email ya está registrado
        if(hairdresserRepository.existsByEmail(hairdresserRegisterDTO.getEmail())){
            model.addAttribute("error", "Este correo ya está registrado");
            return "register-moderator";
        }
        // Verificar si las contraseñas coinciden
        if(!hairdresserRegisterDTO.getPassword().equals(hairdresserRegisterDTO.getConfirmPassword())){
            model.addAttribute("error", "las contraseñas elegidas no coinciden");
            return "register-moderator";
        }
        // Registrar al peluquero/moderador
        hairdresserService.registerHairdresser(hairdresserRegisterDTO);
        return "redirect:/admin";
    }
}

