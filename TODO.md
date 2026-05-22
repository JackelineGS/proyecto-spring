# TODO - Persistir reservas en BD

- [ ] Revisar campos faltantes en `Cita` para enlazar `Comprobante` (faltan `getComprobante/setComprobante`).
- [ ] Actualizar `src/main/java/com/springboot/proyectospring/model/Cita.java` para agregar getter/setter de `comprobante`.
- [x] Actualizar `src/main/java/com/springboot/proyectospring/service/CitaService.java`:
  - [x] Inyectar `CitaRepository` y `PacienteRepository`.
  - [x] Marcar `confirmarReserva` con `@Transactional`.
  - [x] Crear `Paciente` y persistir con `pacienteRepository.save`.
  - [x] Crear `Cita` con `paciente`, `paquete`, `especialistaId`, `fecha`, `hora`, `numeroSesion` y `creadoEn`.
  - [x] Crear `Pago` y asociarlo a `cita`.
  - [x] Crear `Comprobante`, enlazarlo con `cita`, y asignarlo en `cita`.
  - [x] Persistir con `citaRepository.save(cita)` (para que cascade guarde `Pago` y `Comprobante`).
- [ ] Asegurar que el `Map confirmacion` siga mostrando datos correctos (idealmente IDs de entidades creadas).
- [ ] Ejecutar build/arranque y validar que al llamar `/reservar/exito` se crea al menos 1 registro en `cita`/`pago`/`comprobante`.

