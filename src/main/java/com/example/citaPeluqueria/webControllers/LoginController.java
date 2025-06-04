package com.example.citaPeluqueria.webControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controlador para gestionar la página de inicio de sesión.
 */
@Controller
public class LoginController {

    /**
     * Maneja la solicitud GET para mostrar el formulario de login.
     *
     * @return Nombre de la vista de login ("login").
     */
    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }
}
