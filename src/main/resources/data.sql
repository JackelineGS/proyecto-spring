-- data.sql (vacío por ahora) 
-- data.sql
-- SCRIPT COMPLETO DE INYECCIÓN DE DATOS (CATÁLOGO)

-- 1. Insertar las Categorías (Tipos de Paquete)
INSERT INTO tipo_paquete (nombre) VALUES ('Terapia Individual');
INSERT INTO tipo_paquete (nombre) VALUES ('Terapia familiar y de pareja');
INSERT INTO tipo_paquete (nombre) VALUES ('Acompañamiento adolescente');
INSERT INTO tipo_paquete (nombre) VALUES ('Evaluación psicológica');

-- 2. Insertar Paquetes (Alineado a los campos actuales de tu clase Java)
INSERT INTO paquete (nombre, precio, cantidad_visitas, duracion_por_visita, tipo_paquete_id) VALUES
('1 sesión de terapia individual', 150.0, 1, '45 a 50 minutos (por sesión)', 1),
('4 sesiones de terapia individual', 560.0, 4, '45 a 50 minutos (por sesión)', 1),
('10 sesiones de terapia individual', 1300.0, 10, '45 a 50 minutos (por sesión)', 1),

('1 sesión de terapia familiar', 180.0, 1, '45 a 50 minutos (por sesión)', 2),
('4 sesiones de terapia familiar', 680.0, 4, '45 a 50 minutos (por sesión)', 2),
('10 sesiones de terapia familiar', 1600.0, 10, '45 a 50 minutos (por sesión)', 2),

('1 sesión adolescente', 150.0, 1, '45 a 50 minutos (por sesión)', 3),
('4 sesiones adolescente', 560.0, 4, '45 a 50 minutos (por sesión)', 3),

('1 sesión de evaluación', 200.0, 1, '45 a 50 minutos (por sesión)', 4);

-- Especialistas
INSERT INTO especialista (nombre, titulo, colegiatura, foto_url, descripcion_resumen) VALUES
('Giulia Hernandez', 'Psicóloga clínica', 'C.Ps.P.34659', 'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=600&q=80', 'Psicóloga Clínica por la Universidad Femenina del Sagrado Corazón, Perú. · C.Ps.P.34659'),
('Emma Aguilar', 'Psicóloga clínica', 'C.Ps.P.28934', 'https://images.unsplash.com/photo-1594824476967-48c8b964273f?w=600&q=80', 'Psicóloga Clínica por la Pontificia Universidad Católica del Perú. · C.Ps.P.28934'),
('Henry Cano', 'Psicólogo clínico', 'C.Ps.P.31102', 'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=600&q=80', 'Psicólogo Clínico por la Universidad Peruana Cayetano Heredia. · C.Ps.P.31102'),
('Chloe Campos', 'Psicóloga clínica', 'C.Ps.P.40015', 'https://images.unsplash.com/photo-1551836022-deb4988cc6c0?w=600&q=80', 'Psicóloga Clínica por la Universidad de Lima. · C.Ps.P.40015'),
('Gabriel Cruz', 'Psicólogo clínico', 'C.Ps.P.29871', 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=600&q=80', 'Psicólogo Clínico por la Universidad San Marcos. · C.Ps.P.29871'),
('Louise Duarte', 'Psicóloga clínica', 'C.Ps.P.33580', 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=600&q=80', 'Psicóloga Clínica por la Universidad Ricardo Palma. · C.Ps.P.33580');

-- Colecciones de elementos (@ElementCollection) de Especialista (los nombres de las tablas asumen el estándar de Hibernate)
INSERT INTO especialista_especialidades (especialista_id, especialidades) VALUES
(1, 'Especialista en duelo neonatal'), (1, 'Terapia cognitivo-conductual'),
(2, 'Terapia de pareja y familiar'), (2, 'Acompañamiento adolescente'),
(3, 'Ansiedad y depresión'), (3, 'Psicoterapia individual'),
(4, 'Evaluación psicológica'), (4, 'Psicoterapia individual'),
(5, 'Adolescentes'), (5, 'Conducta y autoestima'),
(6, 'Duelo'), (6, 'Terapia familiar');

INSERT INTO especialista_formacion (especialista_id, formacion) VALUES
(1, 'Psicóloga Clínica por la Universidad Femenina del Sagrado Corazón, Perú.'), (1, 'Colegiada y Habilitada por el Colegio de Psicólogos del Perú'),
(2, 'Psicóloga Clínica por la Pontificia Universidad Católica del Perú.'), (2, 'Maestría en Psicoterapia de pareja y familia.'),
(3, 'Psicólogo Clínico por la Universidad Peruana Cayetano Heredia.'), (3, 'Especialización en trastornos del estado de ánimo.'),
(4, 'Psicóloga Clínica por la Universidad de Lima.'),
(5, 'Psicólogo Clínico por la Universidad San Marcos.'),
(6, 'Psicóloga Clínica por la Universidad Ricardo Palma.'); 
