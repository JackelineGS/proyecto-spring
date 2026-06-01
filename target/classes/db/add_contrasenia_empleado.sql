-- Añade la columna de contraseña a empleado (alineado con Empleado.java y el esquema original).
-- Seguro de ejecutar varias veces: IF NOT EXISTS no falla si la columna ya existe.

ALTER TABLE empleado
    ADD COLUMN IF NOT EXISTS contrasenia VARCHAR(50);

COMMENT ON COLUMN empleado.contrasenia IS 'Contraseña en texto plano (desarrollo). Migrar a hash en producción.';

-- ---------------------------------------------------------------------------
-- Opcional: contraseñas iniciales solo donde aún esté NULL (ajusta correos/valores)
-- ---------------------------------------------------------------------------
-- UPDATE empleado
-- SET contrasenia = 'admin123'
-- WHERE LOWER(correo) = 'admin@digitalhealth.pe'
--   AND contrasenia IS NULL;
--
-- UPDATE empleado
-- SET contrasenia = 'esp123'
-- WHERE LOWER(rol) LIKE '%especialista%'
--   AND contrasenia IS NULL;

-- Verificación
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'empleado'
  AND column_name = 'contrasenia';
