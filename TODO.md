# TODO - Historia Clínica (persistencia + endpoints)

## Paso 1
Implementar modelos de Historia Clínica y módulos relacionados:
- HistoriaClinica ✅
- Anamnesis (1:1) ✅
- Diagnostico (1:N) ✅
- Visita (1:N) ✅
- Psicofarmaco (1:N) ✅
- Evaluaciones (1:N) ✅

## Paso 2
Implementar `HistoriaClinicaRepository` en memoria (CRUD + búsqueda por id).
(ya implementado en el código)



## Paso 3
Implementar `HistoriaClinicaService` (crear/guardar/obtener).

## Paso 4
Implementar `HistoriaClinicaController` con endpoints REST:
- GET /api/historias/{id}
- POST /api/historias (crear/actualizar)
- (opcional) GET /api/historias por dni si lo definimos.

## Paso 5
Aplicar JPA + H2 disco (no solo Historia Clínica):
- pom.xml ✅
- application.properties ✅

## Paso 6
Convertir modelos a `@Entity` por bloques (Paciente/Especialista/Servicio/Horario primero), reemplazar repos por `JpaRepository` y ajustar `DatosIniciales`.


