-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 21, 2024 at 12:35 PM
-- Server version: 8.0.30
-- PHP Version: 8.3.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kelompok4_penjemputansampah`
--

DROP TABLE IF EXISTS `permintaanpenjemputan`;
DROP TABLE IF EXISTS `history`;
DROP TABLE IF EXISTS `lokasi_dropbox`;
DROP TABLE IF EXISTS `jenis_kategori`;

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
                           `idRiwayat` int NOT NULL,
                           `waktuSelesai` datetime NOT NULL,
                           `lokasi` varchar(255) NOT NULL,
                           `kategoriSampah` varchar(255) NOT NULL,
                           `beratSampah` decimal(10,2) NOT NULL,
                           `harga` decimal(10,2) NOT NULL,
                           `statusPenyelesaian` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `history`
--

INSERT INTO `history` VALUES
                          (1, '2024-12-10 14:30:00', 'Jakarta', 'Plastik', 5.00, 25000.00, 'Selesai'),
                          (2, '2024-12-11 10:00:00', 'Bandung', 'Kertas', 3.00, 15000.00, 'Belum Selesai'),
                          (3, '2024-12-12 12:45:00', 'Surabaya', 'Elektronik', 2.00, 50000.00, 'Selesai'),
                          (4, '2024-12-13 08:30:00', 'Yogyakarta', 'Baterai', 1.50, 30000.00, 'Belum Selesai'),
                          (5, '2024-12-14 16:00:00', 'Medan', 'Organik', 10.00, 5000.00, 'Selesai');

-- --------------------------------------------------------

--
-- Table structure for table `jenis_kategori`
--

CREATE TABLE `jenis_kategori` (
                                  `id` int NOT NULL,
                                  `nama` varchar(255) NOT NULL,
                                  `icon` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jenis_kategori`
--

INSERT INTO `jenis_kategori` VALUES
                                 (1, 'Peralatan Rumah Tangga', 'house.png'),
                                 (2, 'Perangkat Komputer', 'computer.png'),
                                 (3, 'Perangkat Komunikasi', 'smartphone.png'),
                                 (4, 'Perangkat Musik', 'live-music.png'),
                                 (5, 'Perangkat Elektronik Medis', 'syringe.png'),
                                 (6, 'Peralatan Pencahayaan', 'light-bulb.png');

-- --------------------------------------------------------

--
-- Table structure for table `lokasi_dropbox`
--

CREATE TABLE `lokasi_dropbox` (
                                  `id` int NOT NULL,
                                  `nama_dropbox` varchar(255) NOT NULL,
                                  `alamat` varchar(255) NOT NULL,
                                  `jarak` decimal(10,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `lokasi_dropbox`
--

INSERT INTO `lokasi_dropbox` VALUES
                                 (1, 'Dropbox Cipedes', 'Jl.Setiabudi Regency', 1.8),
                                 (2, 'Dropbox Gegerkalong', 'Jl.Gegerkalong Hilir', 2.3),
                                 (3, 'Dropbox Ledeng', 'Jl.Setiabudi', 5.0);

-- --------------------------------------------------------

--
-- Table structure for table `permintaanpenjemputan`
--

CREATE TABLE `permintaanpenjemputan` (
                                         `IDpermintaan` int NOT NULL,
                                         `namaPelanggan` varchar(255) NOT NULL,
                                         `alamat` varchar(255) NOT NULL,
                                         `kategoriSampah` varchar(255) NOT NULL,
                                         `berat` decimal(10,2) NOT NULL,
                                         `harga` decimal(10,2) NOT NULL,
                                         `waktuPermintaan` datetime NOT NULL,
                                         `status` varchar(50) NOT NULL,
                                         `dropbox_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `permintaanpenjemputan`
--

INSERT INTO `permintaanpenjemputan` VALUES
                                        (1, 'John Doe', 'Jl. Merdeka No. 1', 'Organik', 3.00, 50000.00, '2024-12-21 09:00:00', 'Menunggu', 1),
                                        (2, 'Jane Smith', 'Jl. Pahlawan No. 2', 'Anorganik', 2.00, 30000.00, '2024-12-21 10:00:00', 'Menunggu', 1),
                                        (3, 'Andi Wijaya', 'Jl. Raya No. 3', 'Bahan Keras', 4.00, 75000.00, '2024-12-21 11:00:00', 'Menunggu', 2),
                                        (4, 'Dewi Lestari', 'Jl. Kebangsaan No. 4', 'Organik', 3.50, 45000.00, '2024-12-21 12:00:00', 'Menunggu', 2),
                                        (5, 'Budi Santoso', 'Jl. Taman No. 5', 'Elektronik', 5.00, 100000.00, '2024-12-21 13:00:00', 'Menunggu', 3);

--
-- Indexes for dumped tables
--

ALTER TABLE `history`
    ADD PRIMARY KEY (`idRiwayat`);

ALTER TABLE `jenis_kategori`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `lokasi_dropbox`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `permintaanpenjemputan`
    ADD PRIMARY KEY (`IDpermintaan`),
  ADD KEY `dropbox_id` (`dropbox_id`);

--
-- AUTO_INCREMENT for dumped tables
--

ALTER TABLE `history`
    MODIFY `idRiwayat` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `jenis_kategori`
    MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `lokasi_dropbox`
    MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `permintaanpenjemputan`
    MODIFY `IDpermintaan` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

ALTER TABLE `permintaanpenjemputan`
    ADD CONSTRAINT `permintaanpenjemputan_ibfk_1` FOREIGN KEY (`dropbox_id`) REFERENCES `lokasi_dropbox` (`id`);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;