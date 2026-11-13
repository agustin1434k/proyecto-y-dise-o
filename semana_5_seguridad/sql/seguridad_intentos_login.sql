-- Semana 5: tabla para limitar los intentos de login fallidos.
-- Es ADITIVA: solo crea una tabla nueva (IF NOT EXISTS). No toca clientes, pedidos ni usuarios.
-- Si no la creás, el login funciona igual pero SIN límite de intentos (se registra un aviso en logs/errores.log).
USE gestion_clientes;

CREATE TABLE IF NOT EXISTS intentos_login (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL,
    ip      VARCHAR(45) NOT NULL,
    fecha   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_intentos_usuario (usuario, ip, fecha)
);

-- Para desbloquear a alguien durante las pruebas:
--   DELETE FROM intentos_login;
