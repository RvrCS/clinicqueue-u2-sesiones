package lab.clinicqueue;

/** Un paciente en la fila de la clinica. */
public record Patient(int ticket, String username, String name, String phone,
                      String reason, String password, String role) {
}
