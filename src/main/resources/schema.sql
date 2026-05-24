CREATE TABLE IF NOT EXISTS pagos (
    id_pago            SERIAL PRIMARY KEY,
    id_paciente        BIGINT NOT NULL REFERENCES paciente(id_paciente),
    id_paquete         BIGINT REFERENCES paquete(id_paquete),
    fecha              DATE          NOT NULL DEFAULT CURRENT_DATE,
    monto              NUMERIC(10, 2),
    metodo_pago        VARCHAR(20),
    numero_comprobante VARCHAR(20),
    tipo_comprobante   VARCHAR(10)   NOT NULL DEFAULT 'boleta'
);

-- Columnas agregadas al modelo Paciente después de la creación inicial de la tabla
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS fechanacimiento    DATE;
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS sexo               VARCHAR(20);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS residencia         VARCHAR(200);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS gradoinstruccion   VARCHAR(50);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS ocupacion          VARCHAR(100);
ALTER TABLE paciente ADD COLUMN IF NOT EXISTS numhijos           INTEGER;
