-- Script para crear la base de datos MySQL para el Sistema de Registro de Estudiantes
-- Ejecutar este script antes de iniciar la aplicación

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS inte_db;
USE inte_db;

-- Crear tabla estudiante
CREATE TABLE IF NOT EXISTS estudiante (
    estudiante_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    genero VARCHAR(20) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    ciudad_residencia VARCHAR(100) NOT NULL,
    numero_lu VARCHAR(20) UNIQUE NOT NULL,
    INDEX idx_dni (dni),
    INDEX idx_numero_lu (numero_lu),
    INDEX idx_genero (genero),
    INDEX idx_ciudad (ciudad_residencia)
);

-- Crear tabla carrera
CREATE TABLE IF NOT EXISTS carrera (
    carrera_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) UNIQUE NOT NULL,
    duracion INT,
    INDEX idx_nombre (nombre)
);

-- Crear tabla estudiante_carrera (tabla de relación con clave compuesta)
CREATE TABLE IF NOT EXISTS estudiante_carrera (
    estudiante_id BIGINT NOT NULL,
    carrera_id BIGINT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    fecha_graduacion DATE NULL,
    graduado BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (estudiante_id, carrera_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(estudiante_id) ON DELETE CASCADE,
    FOREIGN KEY (carrera_id) REFERENCES carrera(carrera_id) ON DELETE CASCADE,
    INDEX idx_fecha_inscripcion (fecha_inscripcion),
    INDEX idx_graduado (graduado),
    INDEX idx_estudiante (estudiante_id),
    INDEX idx_carrera (carrera_id)
);

-- Insertar datos de ejemplo (opcional)
INSERT INTO carrera (nombre, duracion) VALUES 
('Ingeniería en Sistemas', 5),
('Medicina', 7),
('Derecho', 5),
('Psicología', 5),
('Ingeniería Civil', 6),
('Administración de Empresas', 4),
('Contador Público', 5),
('Arquitectura', 6);

INSERT INTO estudiante (nombre, apellido, edad, genero, dni, ciudad_residencia, numero_lu) VALUES 
('Juan', 'Pérez', 22, 'Masculino', '12345678', 'Buenos Aires', 'LU12345'),
('María', 'González', 23, 'Femenino', '87654321', 'Córdoba', 'LU67890'),
('Carlos', 'López', 21, 'Masculino', '11223344', 'Buenos Aires', 'LU11111'),
('Ana', 'Martínez', 24, 'Femenino', '55667788', 'Rosario', 'LU22222'),
('Luis', 'Fernández', 25, 'Masculino', '99887766', 'Mendoza', 'LU33333'),
('Laura', 'Rodríguez', 22, 'Femenino', '44332211', 'La Plata', 'LU44444');

INSERT INTO estudiante_carrera (estudiante_id, carrera_id, fecha_inscripcion, fecha_graduacion, graduado) VALUES 
(1, 1, '2020-03-01', NULL, FALSE),  -- Juan en Ingeniería en Sistemas
(2, 2, '2019-03-01', NULL, FALSE),  -- María en Medicina
(2, 4, '2020-03-01', NULL, FALSE),  -- María también en Psicología
(3, 1, '2021-03-01', NULL, FALSE),  -- Carlos en Ingeniería en Sistemas
(4, 3, '2018-03-01', '2023-12-15', TRUE),  -- Ana en Derecho (graduada)
(5, 5, '2019-03-01', NULL, FALSE),  -- Luis en Ingeniería Civil
(6, 6, '2020-03-01', NULL, FALSE);  -- Laura en Administración

-- Mostrar información de las tablas creadas
SHOW TABLES;
DESCRIBE estudiante;
DESCRIBE carrera;
DESCRIBE estudiante_carrera;

-- Consultas de ejemplo para verificar los datos
SELECT 'Estudiantes por género' as consulta;
SELECT genero, COUNT(*) as cantidad FROM estudiante GROUP BY genero;

SELECT 'Carreras con más estudiantes' as consulta;
SELECT c.nombre, COUNT(ec.estudiante_id) as estudiantes_inscriptos 
FROM carrera c 
LEFT JOIN estudiante_carrera ec ON c.carrera_id = ec.carrera_id 
GROUP BY c.carrera_id, c.nombre 
ORDER BY estudiantes_inscriptos DESC;

SELECT 'Estudiantes graduados vs activos' as consulta;
SELECT 
    SUM(CASE WHEN graduado = TRUE THEN 1 ELSE 0 END) as graduados,
    SUM(CASE WHEN graduado = FALSE THEN 1 ELSE 0 END) as activos
FROM estudiante_carrera;
