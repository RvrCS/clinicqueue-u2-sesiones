package lab.clinicqueue;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas funcionales del estado inicial: iniciar sesion y ver el propio turno.
 * Deben seguir pasando despues de asegurar la sesion en la Tarea 2: el control
 * de seguridad tiene que ser transparente para el usuario legitimo.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ClinicQueueBaselineTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Un paciente inicia sesion y ve su turno")
    void loginYMiTurno() throws Exception {
        MvcResult login = mockMvc.perform(post("/login")
                        .param("username", "ana").param("password", "ana123"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        Cookie session = login.getResponse().getCookie("session");

        mockMvc.perform(get("/mi-turno").cookie(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ana Lopez")));
    }

    @Test
    @DisplayName("Sin sesion, /mi-turno redirige a login")
    void sinSesionRedirige() throws Exception {
        mockMvc.perform(get("/mi-turno"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Recepcion ve la fila completa")
    void recepcionVeLaFila() throws Exception {
        MvcResult login = mockMvc.perform(post("/login")
                        .param("username", "recep").param("password", "recep123"))
                .andReturn();
        Cookie session = login.getResponse().getCookie("session");

        mockMvc.perform(get("/staff").cookie(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Beto Ruiz")));
    }
}
