package lab.clinicqueue;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Fila de la clinica. */
@Controller
public class QueueController {

    private final ClinicDirectory clinic;

    public QueueController(ClinicDirectory clinic) {
        this.clinic = clinic;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("size", clinic.queue().size());
        return "index";
    }
}
