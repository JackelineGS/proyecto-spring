CREATE TABLE pagos (
    id_pago            SERIAL PRIMARY KEY,
    id_paciente        BIGINT NOT NULL REFERENCES paciente(id_paciente),
    id_paquete         BIGINT REFERENCES paquete(id_paquete),
    fecha              DATE          NOT NULL DEFAULT CURRENT_DATE,
    monto              NUMERIC(10, 2),
    metodo_pago        VARCHAR(20),
    numero_comprobante VARCHAR(20),
    tipo_comprobante   VARCHAR(10)   NOT NULL DEFAULT 'boleta'
);
