# ClinicQueue

Ciberseguridad Aplicada 2026 · Unidad 2, Semana 8 · Gestión segura de sesiones

Fila de atención de una clínica universitaria, construida con Spring Boot y Thymeleaf.
Los pacientes inician sesión para ver su turno; la recepción ve la fila completa.

> Los datos son **ficticios**. Ninguno corresponde a una persona real.

## Cómo ejecutar

```bash
./mvnw spring-boot:run      # luego abre http://localhost:8080
./mvnw test
```

Usuarios de prueba: `ana` / `ana123`, `beto` / `beto123`, `carla` / `carla123` (pacientes),
`recep` / `recep123` (recepción). Requisitos: JDK 21.

## Para la clase

El Pull Request **"feat: login con JWT y cookie de sesión"** agrega la autenticación con
JSON Web Token. Se usa en el Code Review de la Sesión A.
