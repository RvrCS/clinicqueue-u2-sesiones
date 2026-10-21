package lab.clinicqueue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Directorio y fila de la clinica, en memoria. Datos FICTICIOS.
 *
 * Hay pacientes (ven su propio turno) y personal de recepcion (ve toda la fila).
 */
@Service
public class ClinicDirectory {

    private final List<Patient> queue = List.of(
            new Patient(1, "ana",   "Ana Lopez",  "644-000-0101", "Control anual",        "ana123",  "PATIENT"),
            new Patient(2, "beto",  "Beto Ruiz",  "644-000-0102", "Dolor de garganta",    "beto123", "PATIENT"),
            new Patient(3, "carla", "Carla Soto", "644-000-0103", "Seguimiento",          "carla123","PATIENT"),
            new Patient(0, "recep", "Recepcion",  "644-000-0100", "-",                    "recep123","STAFF")
    );

    public List<Patient> queue() {
        return queue.stream().filter(p -> !p.role().equals("STAFF")).toList();
    }

    public Optional<Patient> authenticate(String username, String password) {
        return queue.stream()
                .filter(p -> p.username().equals(username) && p.password().equals(password))
                .findFirst();
    }

    public Optional<Patient> byUsername(String username) {
        return queue.stream().filter(p -> p.username().equals(username)).findFirst();
    }
}
