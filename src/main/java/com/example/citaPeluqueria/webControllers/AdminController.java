package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.*;
import com.example.citaPeluqueria.util.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * Controlador para operaciones administrativas de la aplicación.
 * Permite gestionar clientes, moderadores, servicios y festivos.
 *
 * Endpoints:
 *  - GET /admin: Muestra el panel de administración.
 *  - GET /admin/createAdmin: Crea el usuario administrador por defecto si no existe.
 *  - POST /admin/deleteModerator/{id}: Elimina un moderador (peluquero) y sus franjas horarias.
 *  - POST /admin/holidays/add: Añade un día festivo desde el panel admin.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final HairdresserRepository hairdresserRepository;
    private final SlotRepository slotRepository;
    private final ServiceRepository serviceRepository;
    private final HolidayRepository holidayRepository;

    public AdminController(ClientRepository clientRepository, PasswordEncoder passwordEncoder,
                           HairdresserRepository hairdresserRepository,
                           SlotRepository slotRepository,
                           ServiceRepository serviceRepository,
                           HolidayRepository holidayRepository) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.hairdresserRepository = hairdresserRepository;
        this.slotRepository = slotRepository;
        this.serviceRepository = serviceRepository;
        this.holidayRepository = holidayRepository;
    }

    /**
     * Muestra el panel de administración.
     *
     * @param model Modelo para la vista.
     * @param principal Información del usuario autenticado.
     * @return Vista "admin.html".
     */
    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        List<HairdresserEntity>hairdressers = hairdresserRepository.findAll();
        List<ClientEntity>clients = clientRepository.findAll();

        model.addAttribute("moderators", hairdressers);
        model.addAttribute("clients", clients);
        model.addAttribute("packages", serviceRepository.findAll());


        //Verificar si el usuario actual (admin) ya es peluquero
        String currentUsername = principal.getName(); // nombre del usuario actual
        HairdresserEntity currentHairdresser = hairdresserRepository.findByUsername(currentUsername);

        model.addAttribute("isAdminHairdresserRegistered", currentHairdresser!=null);
        List<ServiceEntity>service=serviceRepository.findAll();
        service.forEach(s -> System.out.println(s.toString()));

        return "admin";  // Esto cargará la vista 'admin.html'
    }

    /**
     * Crea el usuario administrador por defecto si no existe.
     *
     * @return Mensaje de éxito o aviso si ya existe.
     */
    @GetMapping("/createAdmin")
    @ResponseBody
    public String createAdmin() {
        // Comprobar si el usuario administrador ya existe
        if (clientRepository.findByUsername(Constants.ADMIN_NAME) == null) {
            ClientEntity adminUser = new ClientEntity();
            adminUser.setUsername(Constants.ADMIN_NAME);
            adminUser.setEmail(Constants.ADMIN_EMAIL);
            adminUser.setPhone(Constants.ADMIN_PHONE);
            adminUser.setPassword(passwordEncoder.encode(Constants.ADMIN_PASSWORD)); // Contraseña encriptada
            adminUser.setRoles(Set.of(Role.ADMIN, Role.MODERATOR));
            clientRepository.save(adminUser);

            return "Admin creado correctamente.";
        }
        return "El usuario administrador ya existe.";
    }

    /**
     * Elimina un moderador (peluquero) y sus franjas horarias.
     *
     * @param id ID del moderador a eliminar.
     * @return Redirección al panel de administración.
     */
    @PostMapping("/deleteModerator/{id}")
    public String deleteModerator(@PathVariable Long id) {
        Optional<HairdresserEntity> optionalHairdresser = hairdresserRepository.findById(id);
        if (optionalHairdresser.isPresent()) {
            HairdresserEntity hairdresser = optionalHairdresser.get();
            slotRepository.deleteAll(hairdresser.getSlots());
            hairdresserRepository.delete(hairdresser);
        }
        return "redirect:/admin";
    }

    /**
     * Añade un nuevo día festivo desde el panel admin.
     *
     * @param newHoliday Entidad del nuevo festivo.
     * @return Redirección al panel de administración.
     */
    @PostMapping("/holidays/add")
    public String addHoliday(@ModelAttribute HolidayEntity newHoliday) {
        holidayRepository.save(newHoliday);
        return "redirect:/admin";
    }

}
