-- ============================================================
-- OPCIONAL: datos de prueba para probar la paginación (40 clientes ficticios).
-- Solo inserta; no borra nada. Ejecutalo UNA sola vez (si lo corrés dos veces
-- se duplican los clientes). Si tu tabla clientes ya tiene datos, no hace falta.
-- Antes de ejecutarlo, comprobá en phpMyAdmin que tu tabla tenga las columnas
-- nombre, email y ciudad.
-- ============================================================
CREATE DATABASE IF NOT EXISTS gestion_clientes CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE gestion_clientes;

CREATE TABLE IF NOT EXISTS clientes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(120),
  ciudad VARCHAR(80)
);

INSERT INTO clientes (nombre, email, ciudad) VALUES
  ('Ana Gómez', 'ana.gomez@example.com', 'Olavarría'),
  ('Juan Pérez', 'juan.perez@example.com', 'Azul'),
  ('Lucía Fernández', 'lucia.fernandez@example.com', 'Tandil'),
  ('Martín Rodríguez', 'martin.rodriguez@example.com', 'Bahía Blanca'),
  ('Sofía López', 'sofia.lopez@example.com', 'Necochea'),
  ('Carlos Díaz', 'carlos.diaz@example.com', 'Olavarría'),
  ('Valentina Torres', 'valentina.torres@example.com', 'Azul'),
  ('Diego Sosa', 'diego.sosa@example.com', 'Tandil'),
  ('Camila Ruiz', 'camila.ruiz@example.com', 'Bahía Blanca'),
  ('Nicolás Herrera', 'nicolas.herrera@example.com', 'Necochea'),
  ('Florencia Castro', 'florencia.castro@example.com', 'Olavarría'),
  ('Matías Romero', 'matias.romero@example.com', 'Azul'),
  ('Julieta Medina', 'julieta.medina@example.com', 'Tandil'),
  ('Tomás Silva', 'tomas.silva@example.com', 'Bahía Blanca'),
  ('Agustina Navarro', 'agustina.navarro@example.com', 'Necochea'),
  ('Facundo Ríos', 'facundo.rios@example.com', 'Olavarría'),
  ('Paula Acosta', 'paula.acosta@example.com', 'Azul'),
  ('Gonzalo Molina', 'gonzalo.molina@example.com', 'Tandil'),
  ('Micaela Vega', 'micaela.vega@example.com', 'Bahía Blanca'),
  ('Lucas Ortiz', 'lucas.ortiz@example.com', 'Necochea'),
  ('Romina Paz', 'romina.paz@example.com', 'Olavarría'),
  ('Franco Benítez', 'franco.benitez@example.com', 'Azul'),
  ('Daniela Aguirre', 'daniela.aguirre@example.com', 'Tandil'),
  ('Santiago Luna', 'santiago.luna@example.com', 'Bahía Blanca'),
  ('Brenda Cabrera', 'brenda.cabrera@example.com', 'Necochea'),
  ('Ezequiel Flores', 'ezequiel.flores@example.com', 'Olavarría'),
  ('Carolina Ponce', 'carolina.ponce@example.com', 'Azul'),
  ('Ignacio Rojas', 'ignacio.rojas@example.com', 'Tandil'),
  ('Natalia Peralta', 'natalia.peralta@example.com', 'Bahía Blanca'),
  ('Joaquín Méndez', 'joaquin.mendez@example.com', 'Necochea'),
  ('Belén Giménez', 'belen.gimenez@example.com', 'Olavarría'),
  ('Ramiro Núñez', 'ramiro.nunez@example.com', 'Azul'),
  ('Milagros Ibáñez', 'milagros.ibanez@example.com', 'Tandil'),
  ('Hernán Cardozo', 'hernan.cardozo@example.com', 'Bahía Blanca'),
  ('Antonella Suárez', 'antonella.suarez@example.com', 'Necochea'),
  ('Pablo Domínguez', 'pablo.dominguez@example.com', 'Olavarría'),
  ('Rocío Sánchez', 'rocio.sanchez@example.com', 'Azul'),
  ('Leandro Arias', 'leandro.arias@example.com', 'Tandil'),
  ('Yamila Campos', 'yamila.campos@example.com', 'Bahía Blanca'),
  ('Ariel Godoy', 'ariel.godoy@example.com', 'Necochea');
