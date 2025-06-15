-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 15, 2025 at 12:02 PM
-- Server version: 8.4.3
-- PHP Version: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbban`
--

-- --------------------------------------------------------

--
-- Table structure for table `alamat_user`
--

CREATE TABLE `alamat_user` (
  `idAlamat` int NOT NULL,
  `idUser` int DEFAULT NULL,
  `alamat` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alamat_user`
--

INSERT INTO `alamat_user` (`idAlamat`, `idUser`, `alamat`) VALUES
(1, 3, 'Nama :\nNo.HP :\nAlamat :\nId Transfer :\n');

-- --------------------------------------------------------

--
-- Table structure for table `detail_pembelian`
--

CREATE TABLE `detail_pembelian` (
  `idDetailPembelian` int NOT NULL,
  `idPembelian` int NOT NULL,
  `idAlamat` int NOT NULL,
  `jumlah` int NOT NULL,
  `subTotal` decimal(15,2) NOT NULL,
  `totalHarga` decimal(15,2) NOT NULL,
  `gambar` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `detail_produk`
--

CREATE TABLE `detail_produk` (
  `idDetailProduk` int NOT NULL,
  `idProduk` int NOT NULL,
  `size` varchar(50) NOT NULL,
  `stok` int DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `detail_produk`
--

INSERT INTO `detail_produk` (`idDetailProduk`, `idProduk`, `size`, `stok`) VALUES
(54, 16, 'XXL', 0),
(55, 17, 'XXL', 0),
(56, 17, 'KKLL', 1),
(61, 19, 'xcv', 0),
(62, 19, 'fwef', 0),
(63, 19, 'fs', 0),
(64, 19, 'dsf', 0),
(65, 20, 'da', 0),
(66, 20, 'cs', 0),
(67, 20, 'dad', 0),
(68, 20, 'xcd', 0);

-- --------------------------------------------------------

--
-- Table structure for table `gambar`
--

CREATE TABLE `gambar` (
  `idGambar` int NOT NULL,
  `namaFile` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `uploaded_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `idProduk` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `gambar`
--

INSERT INTO `gambar` (`idGambar`, `namaFile`, `path`, `uploaded_at`, `idProduk`) VALUES
(7, 'product_image', 'produk/produk2.png', '2025-05-26 08:26:23', 16),
(8, 'product_image', 'produk/produk3.png', '2025-05-26 15:06:39', 17),
(9, 'product_image', 'produk/gdgdfgdf.png', '2025-06-15 16:30:47', 19),
(10, 'product_image', 'produk/Basis data.jpg', '2025-06-15 16:38:14', 20);

-- --------------------------------------------------------

--
-- Table structure for table `game_progress`
--

CREATE TABLE `game_progress` (
  `id` int NOT NULL,
  `idUser` int NOT NULL,
  `level` int DEFAULT '1',
  `playerHP` int DEFAULT '100',
  `maxHP` int DEFAULT '100',
  `monsterHP` int DEFAULT '100',
  `monsterMaxHP` int DEFAULT '100',
  `waktu_update` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `histori_detail`
--

CREATE TABLE `histori_detail` (
  `id_detail` int NOT NULL,
  `id_transaksi` int DEFAULT NULL,
  `nama_barang` varchar(100) DEFAULT NULL,
  `ukuran` varchar(50) DEFAULT NULL,
  `jumlah` int DEFAULT NULL,
  `harga` int DEFAULT NULL,
  `idProduk` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `histori_detail`
--

INSERT INTO `histori_detail` (`id_detail`, `id_transaksi`, `nama_barang`, `ukuran`, `jumlah`, `harga`, `idProduk`) VALUES
(1, 1, 'das', 'adsadsa', 2, 50000, NULL),
(2, 2, 'nasipadang', 'fs', 1, 50000, NULL),
(3, 3, 'nasipadang', 'fsfsfse', 1, 50000, NULL),
(4, 4, 'nasipadang', 'fs', 1, 50000, NULL),
(5, 5, 'nasipadang', 'fs', 1, 50000, NULL),
(6, 6, 'nasipadang', 'fsfsfse', 2, 50000, NULL),
(7, 7, 'nasipadang', 'fs', 1, 423442300, NULL),
(8, 8, 'nasipadang', 'fs', 2, 423442300, NULL),
(9, 10, 'nasipadang', 'fs', 2, 423442300, NULL),
(10, 11, 'nasipadang', 'fsfsfse', 1, 423442300, NULL),
(11, 15, 'nasipadang', 'fs', 2, 423442300, NULL),
(12, 17, 'das', 'dasd', 1, 2341300, NULL),
(13, 19, 'Maxam', 'XXL', 1, 400000000, 16),
(14, 20, 'Maxam', 'XXL', 2, 400000000, 16),
(15, 22, 'Maxam', 'XXL', 1, 400000000, 16);

-- --------------------------------------------------------

--
-- Table structure for table `histori_transaksi`
--

CREATE TABLE `histori_transaksi` (
  `id_transaksi` int NOT NULL,
  `tanggal` datetime DEFAULT NULL,
  `total` int DEFAULT NULL,
  `idUser` int DEFAULT NULL,
  `status` varchar(50) DEFAULT 'Menunggu',
  `path_bukti` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `histori_transaksi`
--

INSERT INTO `histori_transaksi` (`id_transaksi`, `tanggal`, `total`, `idUser`, `status`, `path_bukti`) VALUES
(1, '2025-05-23 17:52:07', 100000, 3, 'Disetujui', NULL),
(2, '2025-05-23 18:21:53', 50000, 3, 'Disetujui', NULL),
(3, '2025-05-24 11:57:16', 50000, 3, 'Disetujui', NULL),
(4, '2025-05-24 11:58:33', 50000, 4, 'Disetujui', NULL),
(5, '2025-05-24 13:33:32', 50000, 4, 'Disetujui', NULL),
(6, '2025-05-24 13:37:00', 100000, 4, 'Disetujui', NULL),
(7, '2025-05-25 14:04:59', 423442300, 3, 'Disetujui', NULL),
(8, '2025-05-25 14:49:11', 846884600, 3, 'Disetujui', NULL),
(9, '2025-05-25 14:49:35', 0, 3, 'Disetujui', 'bukti_transfer/gdgdfgdf.png'),
(10, '2025-05-25 18:14:44', 846884600, 3, 'Disetujui', NULL),
(11, '2025-05-26 07:34:13', 423442300, 3, 'Disetujui', NULL),
(12, '2025-05-26 07:34:41', 0, 3, 'Disetujui', 'bukti_transfer/0df784374686e9bbfc70f480ad43bd94df.jpg'),
(13, '2025-05-26 07:37:07', 0, 3, 'Disetujui', 'bukti_transfer/0df784374686e9bbfc70f480ad43bd94df.jpg'),
(14, '2025-05-26 07:37:10', 0, 3, 'Disetujui', 'bukti_transfer/0df784374686e9bbfc70f480ad43bd94df.jpg'),
(15, '2025-05-26 07:38:06', 846884600, 3, 'Disetujui', NULL),
(16, '2025-05-26 07:38:23', 0, 3, 'Disetujui', 'bukti_transfer/1hx5mc.jpg'),
(17, '2025-05-26 08:18:04', 2341300, 3, 'Disetujui', NULL),
(18, '2025-05-26 08:18:19', 0, 3, 'Disetujui', 'bukti_transfer/1hx5mc.jpg'),
(19, '2025-05-26 14:40:01', 400000000, 1, 'Disetujui', NULL),
(20, '2025-05-26 15:04:12', 800000000, 3, 'Disetujui', NULL),
(21, '2025-05-26 15:05:02', 0, 3, 'Menunggu', 'bukti_transfer/1d658bd20ab83b716a79a4e62aac4dc4.jpg'),
(22, '2025-06-15 18:30:27', 400000000, 3, 'Menunggu', NULL),
(23, '2025-06-15 18:37:20', 4233211, 3, 'Menunggu', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `keranjang`
--

CREATE TABLE `keranjang` (
  `idKeranjang` int NOT NULL,
  `idUser` int NOT NULL,
  `idProduk` int NOT NULL,
  `ukuran` varchar(50) DEFAULT NULL,
  `jumlah` int DEFAULT '1',
  `tanggal_ditambahkan` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `keranjang`
--

INSERT INTO `keranjang` (`idKeranjang`, `idUser`, `idProduk`, `ukuran`, `jumlah`, `tanggal_ditambahkan`) VALUES
(2, 3, 20, 'cs', 1, '2025-06-15 10:37:11');

-- --------------------------------------------------------

--
-- Table structure for table `leaderboard`
--

CREATE TABLE `leaderboard` (
  `idUser` int NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `highestLevel` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifikasi`
--

CREATE TABLE `notifikasi` (
  `id` int NOT NULL,
  `idUser` int DEFAULT NULL,
  `pesan` text,
  `tanggal` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `notifikasi`
--

INSERT INTO `notifikasi` (`id`, `idUser`, `pesan`, `tanggal`) VALUES
(1, 3, 'Transaksi dengan ID 17 telah disetujui oleh admin.', '2025-05-26 00:19:01'),
(2, 1, 'Transaksi dengan ID 19 telah disetujui oleh admin.', '2025-05-26 07:07:02');

-- --------------------------------------------------------

--
-- Table structure for table `notifikasi_user`
--

CREATE TABLE `notifikasi_user` (
  `id_notif` int NOT NULL,
  `idUser` int DEFAULT NULL,
  `pesan` text,
  `waktu` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `notifikasi_user`
--

INSERT INTO `notifikasi_user` (`id_notif`, `idUser`, `pesan`, `waktu`) VALUES
(1, 3, 'Pesanan Anda dengan ID 15 telah disetujui oleh admin.', '2025-05-26 00:02:52');

-- --------------------------------------------------------

--
-- Table structure for table `pembelian`
--

CREATE TABLE `pembelian` (
  `idPembelian` int NOT NULL,
  `idProduk` int NOT NULL,
  `kodePembelian` varchar(50) NOT NULL,
  `jumlah` int NOT NULL,
  `subTotal` decimal(15,2) NOT NULL,
  `totalHarga` decimal(15,2) NOT NULL,
  `idAlamat` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`idPembelian`, `idProduk`, `kodePembelian`, `jumlah`, `subTotal`, `totalHarga`, `idAlamat`) VALUES
(7, 16, 'PB-1748241600674', 1, 400000000.00, 400000000.00, 0),
(8, 16, 'PB-1748243051755', 2, 400000000.00, 800000000.00, 0),
(9, 16, 'PB-1749983426952', 1, 400000000.00, 400000000.00, 0);

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `idProduk` int NOT NULL,
  `brand` varchar(255) NOT NULL,
  `harga` decimal(15,2) NOT NULL,
  `deskripsi` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`idProduk`, `brand`, `harga`, `deskripsi`) VALUES
(16, 'Maxam', 4000000.00, '\"Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit, sed do \neiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,\nquis nostrud exercitation'),
(17, 'Uniset', 400000.00, 'lorem ipsum sir der amen'),
(19, 'Maxam', 123123.00, 'csfdsf'),
(20, 'da', 233211.00, 'werq');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `idUser` int NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`idUser`, `username`, `password`, `role`) VALUES
(1, 'admin', 'admin123', 'admin'),
(2, 'admin', 'admin123', 'admin'),
(3, 'user1', 'user123', 'user'),
(4, 'user2', 'user234', 'user'),
(5, 'Rifsan', 'makan', 'user'),
(6, 'Rifsan', 'nasipadang', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alamat_user`
--
ALTER TABLE `alamat_user`
  ADD PRIMARY KEY (`idAlamat`),
  ADD KEY `idUser` (`idUser`);

--
-- Indexes for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  ADD PRIMARY KEY (`idDetailPembelian`),
  ADD KEY `idPembelian` (`idPembelian`);

--
-- Indexes for table `detail_produk`
--
ALTER TABLE `detail_produk`
  ADD PRIMARY KEY (`idDetailProduk`),
  ADD UNIQUE KEY `unik_produk_ukuran` (`idProduk`,`size`);

--
-- Indexes for table `gambar`
--
ALTER TABLE `gambar`
  ADD PRIMARY KEY (`idGambar`),
  ADD KEY `fk_gambar_produk` (`idProduk`);

--
-- Indexes for table `game_progress`
--
ALTER TABLE `game_progress`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idUser` (`idUser`);

--
-- Indexes for table `histori_detail`
--
ALTER TABLE `histori_detail`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `fk_histori_produk` (`idProduk`);

--
-- Indexes for table `histori_transaksi`
--
ALTER TABLE `histori_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `fk_histori_user` (`idUser`);

--
-- Indexes for table `keranjang`
--
ALTER TABLE `keranjang`
  ADD PRIMARY KEY (`idKeranjang`),
  ADD KEY `idUser` (`idUser`),
  ADD KEY `idProduk` (`idProduk`);

--
-- Indexes for table `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD PRIMARY KEY (`idUser`);

--
-- Indexes for table `notifikasi`
--
ALTER TABLE `notifikasi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notifikasi_user`
--
ALTER TABLE `notifikasi_user`
  ADD PRIMARY KEY (`id_notif`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`idPembelian`),
  ADD KEY `idProduk` (`idProduk`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`idProduk`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alamat_user`
--
ALTER TABLE `alamat_user`
  MODIFY `idAlamat` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  MODIFY `idDetailPembelian` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `detail_produk`
--
ALTER TABLE `detail_produk`
  MODIFY `idDetailProduk` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT for table `gambar`
--
ALTER TABLE `gambar`
  MODIFY `idGambar` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `game_progress`
--
ALTER TABLE `game_progress`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `histori_detail`
--
ALTER TABLE `histori_detail`
  MODIFY `id_detail` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `histori_transaksi`
--
ALTER TABLE `histori_transaksi`
  MODIFY `id_transaksi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `keranjang`
--
ALTER TABLE `keranjang`
  MODIFY `idKeranjang` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `notifikasi`
--
ALTER TABLE `notifikasi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `notifikasi_user`
--
ALTER TABLE `notifikasi_user`
  MODIFY `id_notif` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `pembelian`
--
ALTER TABLE `pembelian`
  MODIFY `idPembelian` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `idProduk` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `idUser` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alamat_user`
--
ALTER TABLE `alamat_user`
  ADD CONSTRAINT `alamat_user_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `detail_pembelian`
--
ALTER TABLE `detail_pembelian`
  ADD CONSTRAINT `detail_pembelian_fk_pembelian` FOREIGN KEY (`idPembelian`) REFERENCES `pembelian` (`idPembelian`) ON DELETE CASCADE;

--
-- Constraints for table `detail_produk`
--
ALTER TABLE `detail_produk`
  ADD CONSTRAINT `detail_produk_ibfk_1` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`) ON DELETE CASCADE,
  ADD CONSTRAINT `detail_produk_ibfk_2` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`);

--
-- Constraints for table `gambar`
--
ALTER TABLE `gambar`
  ADD CONSTRAINT `fk_gambar_produk` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`) ON DELETE CASCADE;

--
-- Constraints for table `game_progress`
--
ALTER TABLE `game_progress`
  ADD CONSTRAINT `game_progress_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `histori_detail`
--
ALTER TABLE `histori_detail`
  ADD CONSTRAINT `fk_histori_produk` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`) ON DELETE SET NULL,
  ADD CONSTRAINT `histori_detail_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `histori_transaksi` (`id_transaksi`);

--
-- Constraints for table `histori_transaksi`
--
ALTER TABLE `histori_transaksi`
  ADD CONSTRAINT `fk_histori_user` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `keranjang`
--
ALTER TABLE `keranjang`
  ADD CONSTRAINT `keranjang_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE,
  ADD CONSTRAINT `keranjang_ibfk_2` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`) ON DELETE CASCADE;

--
-- Constraints for table `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`);

--
-- Constraints for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD CONSTRAINT `pembelian_ibfk_1` FOREIGN KEY (`idProduk`) REFERENCES `produk` (`idProduk`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
