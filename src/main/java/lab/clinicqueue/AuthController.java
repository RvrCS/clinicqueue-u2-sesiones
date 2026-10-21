package lab.clinicqueue;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/** Inicio y cierre de sesion. */
@Controller
public class AuthController {

    public static final String COOKIE = "session";

    private final ClinicDirectory clinic;
    private final JwtService jwt;

    public AuthController(ClinicDirectory clinic, JwtService jwt) {
        this.clinic = clinic;
        this.jwt = jwt;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpServletResponse response, Model model) {
        Optional<Patient> p = clinic.authenticate(username, password);
        if (p.isEmpty()) {
            model.addAttribute("error", "Usuario o contrasena incorrectos");
            return "login";
        }
        String token = jwt.issue(p.get());

        Cookie cookie = new Cookie(COOKIE, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/mi-turno";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
