-- Base de datos para el proyecto chijeu (catálogo de guitarras)
-- Importar este archivo desde phpMyAdmin (pestaña "Importar") o desde la consola de MySQL

CREATE DATABASE IF NOT EXISTS chijeu_db;
USE chijeu_db;

-- Tabla de usuarios para el login (administrador y trabajador)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(50) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

-- Mismos usuarios que ya tenías guardados en SharedPreferences
INSERT INTO usuarios (usuario, clave, rol) VALUES
('admin_musica', 'guitarra123', 'Administrador'),
('vendedor', 'musica2024', 'Trabajador');

-- Tabla de guitarras, con los mismos campos que tu data class "datos"
CREATE TABLE IF NOT EXISTS guitarras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    tipocuerpo VARCHAR(50),
    tipopastillas VARCHAR(50),
    puente VARCHAR(50),
    madera VARCHAR(50),
    cuerdas VARCHAR(50)
);
