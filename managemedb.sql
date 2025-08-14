-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 12, 2025 at 04:48 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `managemedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `deposito`
--

CREATE TABLE `deposito` (
  `id` int(11) NOT NULL,
  `Nome_Utente` varchar(20) NOT NULL,
  `BarCode` varchar(13) NOT NULL,
  `Data_Ora_Inserimento` datetime NOT NULL,
  `Data_Ora_Uscita` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deposito`
--

INSERT INTO `deposito` (`id`, `Nome_Utente`, `BarCode`, `Data_Ora_Inserimento`, `Data_Ora_Uscita`) VALUES
(1, 'craygher', '202501152535', '2025-08-07 17:56:47', NULL),
(3, 'craygher', '8005200005710', '2025-08-07 17:56:47', NULL),
(4, 'craygher', '8005200005710', '2025-08-07 17:56:47', '2025-08-10 20:16:53'),
(5, 'craygher', '202501152535', '2025-08-08 19:44:49', '2025-08-10 20:16:53'),
(6, 'craygher', '202501152535', '2025-08-08 19:47:30', '2025-08-10 20:16:53'),
(7, 'craygher', '202501152535', '2025-08-08 19:47:30', '2025-08-10 20:27:54'),
(8, 'craygher', '202501152535', '2025-08-08 19:47:30', '2025-08-10 20:27:54'),
(9, 'craygher', '202501152535', '2025-08-08 19:47:30', '2025-08-10 20:27:54'),
(29, 'craygher', '8033976480752', '2025-08-08 19:59:05', NULL),
(30, 'craygher', '8033976480752', '2025-08-08 19:59:05', NULL),
(31, 'craygher', '8033976480752', '2025-08-08 19:59:05', NULL),
(32, 'craygher', '8033976480752', '2025-08-08 19:59:05', NULL),
(34, 'craygher', '8033976480752', '2025-08-08 20:10:08', NULL),
(35, 'craygher', '8033976480752', '2025-08-08 20:10:08', NULL),
(36, 'craygher', '8033976480752', '2025-08-08 20:10:08', NULL),
(104, 'craygher', '202501152535', '2025-08-12 16:41:23', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `persona`
--

CREATE TABLE `persona` (
  `id` int(11) NOT NULL,
  `Nome` varchar(30) NOT NULL,
  `Cognome` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `persona`
--

INSERT INTO `persona` (`id`, `Nome`, `Cognome`) VALUES
(1, 'Davide', 'Borrello'),
(8, 'CIcca', 'Galesi');

-- --------------------------------------------------------

--
-- Table structure for table `prodotto`
--

CREATE TABLE `prodotto` (
  `BarCode` varchar(13) NOT NULL,
  `Nome_Prodotto` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prodotto`
--

INSERT INTO `prodotto` (`BarCode`, `Nome_Prodotto`) VALUES
('8033976480752', 'Acqua Fonte Buona'),
('8005200005710', 'Acqua Vera'),
('202501152535', 'BarCode Scanner');

-- --------------------------------------------------------

--
-- Table structure for table `utente`
--

CREATE TABLE `utente` (
  `Nome_Utente` varchar(20) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Data_Iscrizione` datetime NOT NULL,
  `id_Persona` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `utente`
--

INSERT INTO `utente` (`Nome_Utente`, `Password`, `Data_Iscrizione`, `id_Persona`) VALUES
('craygher', '$2a$08$NTXeYLBzKPhDRnsGzjJcLeq1WgX66uh.ULHAoVwQVSJVvlpBV9EwK', '2025-08-07 16:25:42', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `deposito`
--
ALTER TABLE `deposito`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Nome_Utente` (`Nome_Utente`),
  ADD KEY `BarCode` (`BarCode`);

--
-- Indexes for table `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prodotto`
--
ALTER TABLE `prodotto`
  ADD PRIMARY KEY (`BarCode`),
  ADD UNIQUE KEY `Nome_Prodotto` (`Nome_Prodotto`);

--
-- Indexes for table `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`Nome_Utente`),
  ADD KEY `id_Persona` (`id_Persona`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `deposito`
--
ALTER TABLE `deposito`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=105;

--
-- AUTO_INCREMENT for table `persona`
--
ALTER TABLE `persona`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deposito`
--
ALTER TABLE `deposito`
  ADD CONSTRAINT `deposito_ibfk_1` FOREIGN KEY (`Nome_Utente`) REFERENCES `utente` (`Nome_Utente`),
  ADD CONSTRAINT `deposito_ibfk_2` FOREIGN KEY (`BarCode`) REFERENCES `prodotto` (`BarCode`);

--
-- Constraints for table `utente`
--
ALTER TABLE `utente`
  ADD CONSTRAINT `utente_ibfk_1` FOREIGN KEY (`id_Persona`) REFERENCES `persona` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
