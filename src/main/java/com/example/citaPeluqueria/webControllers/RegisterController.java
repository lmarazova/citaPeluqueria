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
/**
 * Controlador para gestionar el registro de nuevos usuarios.
 * Permite visualizar el formulario de registro y procesar las solicitudes de inscripción.
 */
@Controller
public class RegisterController {

    private final ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;


    /**
     * Constructor del controlador, inyectando el repositorio de clientes.
     *
     * @param clientRepository Repositorio que gestiona los datos de los clientes.
     */
    public RegisterController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    /**
     * Muestra el formulario de registro de usuario.
     *
     * @param model Modelo para pasar información a la vista.
     * @param userRegisterDTO Objeto que almacena los datos ingresados por el usuario.
     * @return Nombre de la vista de registro ("register").
     */
    @GetMapping("/register")
    public String showRegisterController(Model model, UserRegisterDTO userRegisterDTO){
        model.addAttribute("userRegisterDTO", userRegisterDTO);
        return "register";
    }

    /**
     * Procesa el registro de un nuevo usuario.
     *
     * @param userRegisterDTO Datos del usuario a registrar.
     * @param bindingResult Resultado de la validación del formulario.
     * @param model Modelo para pasar datos a la vista.
     * @return Redirección a la página de inicio de sesión si el registro es exitoso,
     *         o muestra el formulario de registro nuevamente si hay errores.
     */
    @PostMapping("/register")
    public String registerUser(@Valid UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult,
                               Model model){
        // Validar errores en el formulario
        if(bindingResult.hasErrors()){
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            return "register";
        }

        // Verificar si el email ya está registrado
        if(clientRepository.existsByEmail(userRegisterDTO.getEmail())){
            model.addAttribute("error", "Este correo ya está registrado");
            return "register";
        }

        // Verificar si el teléfono ya está registrado
        if(clientRepository.existsByPhone(userRegisterDTO.getPhone())){
            model.addAttribute("error", "Este teléfono ya está registrado");
        }

        // Verificar si las contraseñas coinciden
        if(!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())){
            model.addAttribute("error", "las contraseñas elegidas no coinciden");
            return "register";
        }

        // Registrar al usuario
        clientService.registerUser(userRegisterDTO);
        return "redirect:/register?success=success";    }
}
