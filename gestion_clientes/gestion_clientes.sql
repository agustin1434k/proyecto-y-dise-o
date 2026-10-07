-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 07-10-2026 a las 23:28:51
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestion_clientes`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `apellido`, `email`, `telefono`) VALUES
(2, 'Martina', 'Gómez', 'martina.gomez@gmail.com', '2262123457'),
(3, 'Lucas', 'Fernández', 'lucas.fernandez@gmail.com', '2262123458'),
(4, 'Sofía', 'Rodríguez', 'sofia.rodriguez@gmail.com', '2262123459'),
(5, 'Mateo', 'López', 'mateo.lopez@gmail.com', '2262123460'),
(6, 'Valentina', 'Martínez', 'valentina.martinez@gmail.com', '2262123461'),
(7, 'Nicolás', 'García', 'nicolas.garcia@gmail.com', '2262123462'),
(8, 'Camila', 'Sánchez', 'camila.sanchez@gmail.com', '2262123463'),
(9, 'Tomás', 'Díaz', 'tomas.diaz@gmail.com', '2262123464'),
(10, 'Agustina', 'Romero', 'agustina.romero@gmail.com', '2262123465');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `producto` varchar(100) NOT NULL,
  `importe` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id_pedido`, `id_cliente`, `fecha`, `producto`, `importe`) VALUES
(2, 2, '2026-09-02', 'Mouse gamer', 18000.00),
(3, 3, '2026-09-03', 'Auriculares', 32000.00),
(4, 4, '2026-09-04', 'Monitor 24 pulgadas', 185000.00),
(5, 5, '2026-09-05', 'Webcam HD', 45000.00),
(7, 6, '2026-09-08', 'Teclado mecánico', 55000.00),
(8, 7, '2026-09-10', 'Memoria RAM 8GB', 35000.00),
(9, 8, '2026-09-12', 'Disco SSD 480GB', 65000.00),
(10, 9, '2026-09-14', 'Parlantes', 28000.00),
(11, 10, '2026-09-15', 'Cable HDMI', 9000.00),
(12, 2, '2026-09-17', 'Soporte para monitor', 22000.00),
(13, 4, '2026-09-20', 'Mouse gamer', 18000.00),
(14, 6, '2026-09-22', 'Auriculares', 32000.00),
(15, 8, '2026-09-25', 'Teclado inalámbrico', 25000.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD KEY `id_cliente` (`id_cliente`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
