package com.example.citaPeluqueria.webControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showLoginForm(HttpServletRequest request, Model model){
        Object error = request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("errorMessage", error.toString());
            request.getSession().removeAttribute("error"); // Limpieza aquí
        }
        return "login";
    }
}
