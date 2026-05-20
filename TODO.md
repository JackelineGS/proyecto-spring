# TODO - H2 (configuración) paso a paso

## Paso 1 (hecho/para confirmar)
- Revisar `pom.xml` y decidir dependencias para usar H2 con Spring JDBC (sin implementar tablas todavía).

## Paso 2
- Agregar dependencias al `pom.xml`:
  - `com.h2database:h2`
  - `spring-boot-starter-jdbc`

## Paso 3
- Configurar `application.properties` para:
  - URL H2 en memoria
  - driver
  - `spring.sql.init.mode=never` (para NO crear tablas aún)

## Paso 4
- Crear carpetas/archivos de configuración si hiciera falta (sin `schema.sql` aún).

## Paso 5
- Lanzar app para validar que levanta con H2 sin tablas (solo conexión).

