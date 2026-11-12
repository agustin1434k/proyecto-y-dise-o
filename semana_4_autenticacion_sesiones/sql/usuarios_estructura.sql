-- Tabla de usuarios para la Semana 4.
-- Si ya tenés una tabla usuarios, NO la vuelvas a crear: revisá su estructura
-- (phpMyAdmin > usuarios > pestaña Estructura) y pasámela.
-- IF NOT EXISTS evita borrar o pisar una tabla existente.
USE gestion_clientes;

CREATE TABLE IF NOT EXISTS usuarios (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    usuario  VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,      -- guarda el HASH, nunca la contraseña original
    nombre   VARCHAR(100) NOT NULL,
    activo   BOOLEAN DEFAULT TRUE
);

-- Para desactivar una cuenta (prueba del Ejercicio 7):
--   UPDATE usuarios SET activo = 0 WHERE usuario = 'admin';
-- Para volver a activarla:
--   UPDATE usuarios SET activo = 1 WHERE usuario = 'admin';
