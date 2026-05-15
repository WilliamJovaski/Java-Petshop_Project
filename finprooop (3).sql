-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2026 at 10:53 PM
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
-- Database: `finprooop`
--

-- --------------------------------------------------------

--
-- Table structure for table `hewan`
--

CREATE TABLE `hewan` (
  `id` int(11) NOT NULL,
  `kategori` enum('Kucing','Anjing','Burung','Ular') NOT NULL,
  `jenis_hewan` varchar(100) NOT NULL,
  `nama_hewan` varchar(100) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `harga` int(11) NOT NULL,
  `gambar` varchar(255) DEFAULT NULL,
  `file_suara` varchar(255) DEFAULT NULL,
  `info` text DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hewan`
--

INSERT INTO `hewan` (`id`, `kategori`, `jenis_hewan`, `nama_hewan`, `tanggal_lahir`, `harga`, `gambar`, `file_suara`, `info`, `status`) VALUES
(1, 'Kucing', 'Kucing', 'Luna', '2024-07-15', 3500000, 'cat_default.jpg', 'cat_default.mp3', 'Peranakan Persia–Anggora dengan keturunan Persia dominan, berbulu panjang.', 'Dibooking'),
(2, 'Kucing', 'Kucing', 'Milo', '2024-01-10', 4200000, 'cat_default.jpg', 'cat_default.mp3', 'Peranakan British Shorthair–Scottish Fold, keturunan British murni.', 'Tersedia'),
(3, 'Kucing', 'Kucing', 'Simba', '2025-05-05', 6000000, 'cat_default.jpg', 'cat_default.mp3', 'Peranakan Maine Coon lokal, keturunan Maine Coon generasi kedua.', 'Terjual'),
(4, 'Kucing', 'Kucing', 'Bella', '2025-02-20', 5000000, 'cat_default.jpg', 'cat_default.mp3', 'Peranakan Persia Himalaya, keturunan Persia peaknose.', 'Tersedia'),
(5, 'Kucing', 'Kucing', 'Oliver', '2022-12-12', 4800000, 'cat_default.jpg', 'cat_default.mp3', 'Peranakan Bengal–domestik, keturunan Bengal F3 dengan pola loreng.', 'Terjual'),
(6, 'Anjing', 'Anjing', 'Bruno', '2024-01-12', 6500000, 'dog_default.jpg', 'dog_default.mp3', 'Anjing peranakan Golden Retriever–Labrador dengan keturunan Golden dominan.', 'Terjual'),
(7, 'Anjing', 'Anjing', 'Max', '2025-02-25', 8000000, 'dog_default.jpg', 'dog_default.mp3', 'Anjing peranakan Siberian Husky–Alaskan Malamute, keturunan Husky murni.', 'Terjual'),
(8, 'Anjing', 'Anjing', 'Rocky', '2022-12-18', 7200000, 'dog_default.jpg', 'dog_default.mp3', 'Anjing peranakan German Shepherd lokal, keturunan Shepherd generasi kedua.', 'Terjual'),
(9, 'Anjing', 'Anjing', 'Buddy', '2024-07-10', 5800000, 'dog_default.jpg', 'dog_default.mp3', 'Anjing peranakan Beagle–Cocker Spaniel dengan sifat aktif.', 'Booking'),
(10, 'Anjing', 'Anjing', 'Coco', '2023-10-05', 6000000, 'dog_default.jpg', 'dog_default.mp3', 'Anjing peranakan Poodle–Maltese, keturunan Poodle dominan.', 'Booking'),
(11, 'Burung', 'Burung Pipit', 'Piko', '2025-05-10', 150000, 'bird_default.jpg', 'bird_default.mp3', 'Burung pipit lokal dengan suara cicitan kecil, cepat, dan berulang. Umum hidup berkelompok.', 'Booking'),
(12, 'Burung', 'Burung Cendrawasih', 'Aruna', '2022-12-18', 15000000, 'bird_default.jpg', 'bird_default.mp3', 'Burung cendrawasih Papua dengan suara khas lembut dan visual bulu ekor yang sangat indah.', 'Terjual'),
(13, 'Burung', 'Burung Hantu Salju', 'Snow', '2022-01-05', 12000000, 'bird_default.jpg', 'bird_default.mp3', 'Burung hantu salju dengan suara rendah dan tenang, aktif di malam hari (nokturnal).', 'Terjual'),
(14, 'Burung', 'Burung Sirpu', 'Liris', '2024-07-22', 750000, 'bird_default.jpg', 'bird_default.mp3', 'Burung sirpu (chirping bird) dengan suara nyaring, cepat, dan ritmis, sering aktif di pagi hari.', 'Terjual'),
(15, 'Burung', 'Burung Sogok Ontong', 'Nektar', '2024-03-30', 900000, 'bird_default.jpg', 'bird_default.mp3', 'Burung sogok ontong pemakan nektar dengan suara halus, pendek, dan bernada tinggi.', 'Terjual'),
(17, 'Anjing', 'Golden Retriever', 'Happy', '2022-01-15', 12000000, 'golden.jpg', '', 'Sangat pintar dan penurut', 'Terjual'),
(18, 'Burung', 'Kakatua Putih', 'Smiski', '2020-11-20', 15000000, 'kakatua.jpg', '', 'Bisa menirukan suara manusia', 'Tersedia'),
(19, 'Kucing', 'Kucing Persia', 'Perki', '2024-08-01', 4500000, 'persia.jpg', '', 'Bulu panjang dan hidung pesek', 'Terjual'),
(20, 'Anjing', 'Pug', 'Jimmy', '2021-03-25', 7000000, 'pug.jpg', '', 'Kecil, aktif, dan lucu', 'Tersedia'),
(21, 'Burung', 'Lovebird Blue', 'cimot', '2025-02-10', 500000, 'lovebird.jpg', '', 'Warna biru cerah', 'Terjual'),
(30, 'Kucing', 'Maine Coon', 'Leo', '2023-05-10', 8500000, 'mainecoon.jpg', NULL, 'Kucing Maine Coon raksasa, sangat ramah, manja, dan sudah vaksin lengkap.', 'Tersedia'),
(31, 'Burung', 'Kakatua Putih', 'Smiski', '2020-11-20', 15000000, 'kakatua.jpg', NULL, 'Burung Kakatua cerdas, sudah jinak total, dan bisa menirukan beberapa kata manusia.', 'Tersedia'),
(33, 'Anjing', 'Pug', 'Jimmy', '2021-03-25', 7000000, 'pug.jpg', NULL, 'Anjing Pug bertubuh gempal, sangat aktif, lucu, dan ekspresi wajah menggemaskan.', 'Tersedia'),
(34, 'Burung', 'Lovebird Blue', 'Cimot', '2025-02-10', 500000, 'lovebird.jpg', NULL, 'Lovebird warna biru cerah, sehat, lincah, dan sangat rajin berkicau/ngekek.', 'Tersedia'),
(35, 'Kucing', 'British Shorthair', 'Milo', '2024-05-20', 11000000, 'bsh.jpg', NULL, 'Kucing BSH dengan bulu abu-abu tebal, pipi chubby, dan karakter yang mandiri.', 'Tersedia'),
(36, 'Anjing', 'Beagle', 'Snoopy', '2023-08-12', 5500000, 'beagle.jpg', NULL, 'Anjing Beagle yang sangat ceria, suka mengendus, dan ramah terhadap anak-anak.', 'Terjual'),
(37, 'Kucing', 'Ragdoll', 'Snowy', '2024-02-20', 13500000, 'ragdoll.jpg', NULL, 'Kucing Ragdoll dengan mata biru indah, bulu halus, dan sifat yang sangat tenang.', 'Tersedia'),
(38, 'Burung', 'Kenari', 'Sunny', '2025-01-05', 450000, 'kenari.jpg', NULL, 'Burung Kenari dengan warna kuning cerah dan kicauan yang sangat merdu.', 'Tersedia'),
(39, 'Anjing', 'Corgi', 'Shorty', '2022-11-30', 18000000, 'corgi.jpg', NULL, 'Anjing Corgi kaki pendek yang aktif, sangat pintar, dan menggemaskan.', 'Terjual'),
(40, 'Kucing', 'Sphynx', 'Dobby', '2021-06-15', 25000000, 'sphynx.jpg', NULL, 'Kucing Sphynx unik tanpa bulu, sangat manja, dan memerlukan perawatan kulit khusus.', 'Terjual');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(11) NOT NULL,
  `kode_booking` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `hewan_id` int(11) DEFAULT NULL,
  `total_harga` int(11) NOT NULL,
  `pembeli` varchar(100) DEFAULT NULL,
  `tanggal_transaksi` datetime DEFAULT current_timestamp(),
  `waktu_expired` datetime DEFAULT (current_timestamp() + interval 2 hour),
  `status` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `kode_booking`, `user_id`, `hewan_id`, `total_harga`, `pembeli`, `tanggal_transaksi`, `waktu_expired`, `status`) VALUES
(1, 'BK-150188', 3, 14, 750000, NULL, '2026-01-07 03:47:03', '2026-01-07 05:47:03', 'Sudah Dibayar'),
(2, 'BK-636570', 1, 1, 3500000, NULL, '2026-01-07 04:24:27', '2026-01-07 06:24:27', 'Menunggu Pembayaran'),
(4, 'BK-112679', 3, 15, 900000, NULL, '2026-01-07 04:38:04', '2026-01-07 06:38:04', 'Sudah Dibayar'),
(5, 'BK-478012', 3, 13, 12000000, NULL, '2026-01-07 04:42:48', '2026-01-07 06:42:48', 'Sudah Dibayar'),
(6, 'BK-612570', 3, 12, 15000000, NULL, '2026-01-07 04:43:28', '2026-01-07 06:43:28', 'Sudah Dibayar'),
(8, 'BK-481869', 3, 10, 6000000, 'Vanessa', '2026-01-07 04:54:41', NULL, 'Booking'),
(12, 'BK-900933', 3, 5, 4800000, 'Vanessa', '2026-01-07 05:18:20', '2026-01-07 07:18:20', 'Sudah Dibayar'),
(13, 'ONS-70259', 1, 3, 6000000, 'jmes', '2026-01-07 16:11:10', '2026-01-07 18:11:10', 'Sudah Dibayar'),
(14, 'ONS-271467', 1, 8, 7200000, 'Agnes', '2026-01-07 21:14:31', '2026-01-07 23:14:31', 'Sudah Dibayar'),
(15, 'BK-375756', 3, 2, 4200000, 'Vanessa', '2026-01-07 21:16:15', '2026-01-07 23:16:15', 'Sudah Dibayar'),
(16, 'ONS-516359', 1, 19, 4500000, 'Klemen', '2026-01-07 22:08:36', NULL, 'Sudah Dibayar'),
(17, 'BK-690831', 3, 21, 500000, 'Vanessa', '2026-01-07 22:11:30', NULL, 'Sudah Dibayar'),
(18, 'ONS-71903', 1, 17, 12000000, 'Jimis', '2026-01-07 22:17:51', NULL, 'Sudah Dibayar'),
(20, 'ONS-27456', 1, 7, 8000000, 'Kimmy', '2026-01-07 22:33:47', NULL, 'Sudah Dibayar'),
(22, 'ONS-62004', 1, 36, 5500000, 'Lousandria', '2026-01-07 22:51:02', NULL, 'Sudah Dibayar'),
(23, 'BK-207817', 3, 39, 18000000, 'Vanessa', '2026-01-07 22:53:27', NULL, 'Sudah Dibayar'),
(24, 'BK-176079', 3, 40, 25000000, 'Vanessa', '2026-01-20 21:42:56', NULL, 'Sudah Dibayar');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('admin','customer') DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `status`, `last_login`) VALUES
(1, 'baruUsername', '1234', 'admin', 'Online', '2026-01-20 22:05:55'),
(3, 'Vanessa', 'Jojo1234', 'customer', 'Offline', '2026-01-20 21:41:50'),
(4, 'MrWilliam', 'James123', 'customer', NULL, NULL),
(7, 'Jameson', '123', 'customer', 'Online', '2025-12-20 19:14:17'),
(8, 'Misterwes', '1234', 'customer', 'Online', '2025-12-20 19:45:30'),
(9, 'sumiw', '1234', 'customer', NULL, NULL),
(10, 'OFFLINE_GUEST', 'offline', 'customer', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `hewan`
--
ALTER TABLE `hewan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode_booking` (`kode_booking`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `hewan_id` (`hewan_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `hewan`
--
ALTER TABLE `hewan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`hewan_id`) REFERENCES `hewan` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
