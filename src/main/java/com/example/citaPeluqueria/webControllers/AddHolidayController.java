package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import com.example.citaPeluqueria.repositories.HolidayRepository;
import com.example.citaPeluqueria.services.SlotService;
import com.example.citaPeluqueria.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
/**
 * Controlador encargado de la gestión de días festivos (alta, baja, actualización).
 *
 * Endpoints:
 *  - GET /add-holidays: Muestra el formulario para añadir festivos.
 *  - POST /add-holidays: Añade un nuevo festivo si no existe ya para la fecha.
 *  - POST /delete/{id}: Elimina un festivo por ID.
 *  - POST /admin/update-slots-holidays: Elimina todas las franjas horarias que caen en festivos.
 */
@Controller
public class AddHolidayController {

    private final HolidayRepository holidayRepository;
    @Autowired
    private SlotService slotService;

    public AddHolidayController(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /**
     * Muestra el formulario para agregar festivos.
     *
     * @param model Modelo para la vista.
     * @return Página "add-holidays".
     */
    @GetMapping("add-holidays")
    public String showAddHolidayForm(Model model){
        model.addAttribute("newHoliday", new HolidayEntity());
        model.addAttribute("holidays", holidayRepository.findAll());
        model.addAttribute("startHour", Constants.START_HOUR);
        model.addAttribute("workedHours", Constants.WORKED_HOURS);
        return "add-holidays";
    }

    /**
     * Procesa el envío del formulario para añadir un nuevo festivo.
     *
     * @param holiday Nuevo festivo a guardar.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la misma página con mensaje de éxito o error.
     */
    @PostMapping("add-holidays")
    public String addHoliday(@ModelAttribute("newHoliday") HolidayEntity holiday,
                             RedirectAttributes redirectAttributes) {

        Optional<HolidayEntity> existingHoliday = holidayRepository.findByDate(holiday.getDate());

        if (existingHoliday.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un festivo para esa fecha");
        } else {
            holidayRepository.save(holiday);
            redirectAttributes.addFlashAttribute("success", "Festivo añadido correctamente");
        }

        return "redirect:/add-holidays";
    }

    /**
     * Elimina un festivo por ID.
     *
     * @param id ID del festivo a eliminar.
     * @return Redirección a la página de festivos.
     */
    @PostMapping("/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        holidayRepository.deleteById(id);
        return "redirect:/add-holidays";    }

    /**
     * Elimina las franjas horarias que coinciden con días festivos.
     *
     * @return Redirección a la página de festivos.
     */
    @PostMapping("/admin/update-slots-holidays")
    public String updateSlotsHolidays() {
        slotService.deleteSlotsInHolidays();
        return "redirect:/add-holidays";
    }


}
