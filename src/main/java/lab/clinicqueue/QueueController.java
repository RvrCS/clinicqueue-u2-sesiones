package lab.clinicqueue;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/** Fila de la clinica y paginas protegidas. */
@Controller
public class QueueController {

    private final ClinicDirectory clinic;
    private final JwtService jwt;

    public QueueController(ClinicDirectory clinic, JwtService jwt) {
        this.clinic = clinic;
        this.jwt = jwt;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("size", clinic.queue().size());
        return "index";
    }

    /** El paciente ve su propio turno. Requiere un token valido. */
    @GetMapping("/mi-turno")
    public String miTurno(HttpServletRequest request, Model model) {
        Optional<Map<String, Object>> claims = currentUser(request);
        if (claims.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("claims", claims.get());
        return "mi-turno";
    }

    /** Recepcion ve toda la fila con datos de contacto. */
    @GetMapping("/staff")
    public String staff(HttpServletRequest request, Model model) {
        Optional<Map<String, Object>> claims = currentUser(request);
        if (claims.isEmpty()) {
            return "redirect:/login";
        }
        if (!"STAFF".equals(claims.get().get("role"))) {
            model.addAttribute("denied", true);
            return "mi-turno";
        }
        model.addAttribute("queue", clinic.queue());
        return "staff";
    }

    private Optional<Map<String, Object>> currentUser(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> AuthController.COOKIE.equals(c.getName()))
                .map(c -> {
                    try {
                        return jwt.verify(c.getValue());
                    } catch (RuntimeException e) {
                        return null;
                    }
                })
                .filter(m -> m != null)
                .findFirst();
    }
}
