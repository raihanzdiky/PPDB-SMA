-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Jul 2025 pada 14.49
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ppdbsaya`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_asalsekolah`
--

CREATE TABLE `tb_asalsekolah` (
  `id_sekolah` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `nama_sekolah` varchar(50) NOT NULL,
  `alamat_sekolah` varchar(100) NOT NULL,
  `tahun_lulus` year(4) NOT NULL,
  `nilai_raport` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_asalsekolah`
--

INSERT INTO `tb_asalsekolah` (`id_sekolah`, `id_user`, `nama_sekolah`, `alamat_sekolah`, `tahun_lulus`, `nilai_raport`) VALUES
(1, 1, 'smp', 'cibaracakhttps://maps.app.goo.gl/HFfD8dcbGeqM6SCD9', '2021', 78.00),
(2, 3, 'atha', 'gfae', '2020', 79.00),
(3, 4, 'kfk', 'fdf', '2025', 33.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_datacalonsiswa`
--

CREATE TABLE `tb_datacalonsiswa` (
  `id_calon` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `nisn` varchar(20) NOT NULL,
  `tempat_lahir` varchar(50) DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `jenis_kelamin` enum('L','P') DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `no_tlp` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_datacalonsiswa`
--

INSERT INTO `tb_datacalonsiswa` (`id_calon`, `id_user`, `nama_lengkap`, `nisn`, `tempat_lahir`, `tanggal_lahir`, `jenis_kelamin`, `alamat`, `no_tlp`, `email`) VALUES
(1, 1, 'diky raihan s', '24552011194', 'bandung', '2025-07-02', 'L', 'cijerah', '357515435', 'gjf'),
(2, 3, 'dfs', '24452', 'dgfhh', '2025-07-02', 'L', 'dgsh', '5367', 'dshzjz'),
(3, 4, 'p', '1', 'g', '2025-07-03', 'L', 'f', 'f', 'f');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_dokumen`
--

CREATE TABLE `tb_dokumen` (
  `id_dokumen` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `akte_kelahiran` varchar(255) NOT NULL,
  `kartu_keluarga` varchar(255) NOT NULL,
  `nilai_raport` varchar(255) NOT NULL,
  `ijazah` varchar(255) NOT NULL,
  `foto_latarmerah` varchar(255) NOT NULL,
  `status_verifikasi` enum('SUDAH DIVERIFIKASI','BELUM DIVERIFIKASI') DEFAULT 'BELUM DIVERIFIKASI'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_dokumen`
--

INSERT INTO `tb_dokumen` (`id_dokumen`, `id_user`, `akte_kelahiran`, `kartu_keluarga`, `nilai_raport`, `ijazah`, `foto_latarmerah`, `status_verifikasi`) VALUES
(1, 1, 'C:\\Users\\raiha\\OneDrive\\Dokumen\\245520011194_DikyRS.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\24552011194_Diky_LAPORAN REVIEW PAPER.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\245520011194_DikyRS.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\24552011194_Diky_LAPORAN REVIEW PAPER.docx', 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\foto_test.jpeg', 'BELUM DIVERIFIKASI'),
(2, 3, 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\AKTE_test.pdf', 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\kk_test.pdf', 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\RAPORT_TEST.pdf', 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\IJAZAH_test.pdf', 'C:\\Users\\sitii\\OneDrive\\ドキュメント\\foto_test.jpeg', 'SUDAH DIVERIFIKASI'),
(3, 4, 'C:\\Users\\raiha\\OneDrive\\Dokumen\\245520011194_DikyRS.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\24552011194_Diky_LAPORAN REVIEW PAPER.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\245520011194_DikyRS.docx', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\HSI.txt', 'C:\\Users\\raiha\\OneDrive\\Dokumen\\245520011194_DikyRS.docx', 'BELUM DIVERIFIKASI');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_jalur_jurusan`
--

CREATE TABLE `tb_jalur_jurusan` (
  `id_jalur_jurusan` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `jurusan` enum('IPA','IPS') NOT NULL,
  `jalur_pendaftaran` enum('transkripsi nilai','prestasi','zonasi') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_jalur_jurusan`
--

INSERT INTO `tb_jalur_jurusan` (`id_jalur_jurusan`, `id_user`, `jurusan`, `jalur_pendaftaran`) VALUES
(1, 1, 'IPS', 'prestasi'),
(2, 3, 'IPA', 'transkripsi nilai'),
(3, 4, 'IPA', 'prestasi');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_kuota`
--

CREATE TABLE `tb_kuota` (
  `id_kuota` int(11) NOT NULL,
  `jurusan` enum('IPA','IPS') NOT NULL,
  `jalur_pendaftaran` enum('zonasi','prestasi','transkripsi nilai') NOT NULL,
  `kuota` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_kuota`
--

INSERT INTO `tb_kuota` (`id_kuota`, `jurusan`, `jalur_pendaftaran`, `kuota`) VALUES
(1, 'IPA', 'zonasi', 50),
(2, 'IPA', 'prestasi', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_ortu`
--

CREATE TABLE `tb_ortu` (
  `id_ortu` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `nama_lengkap` varchar(50) NOT NULL,
  `pendidikan_terakhir` enum('SD','SMP','SMA','D3','D4','S1','S2') NOT NULL,
  `pekerjaan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_ortu`
--

INSERT INTO `tb_ortu` (`id_ortu`, `id_user`, `nama_lengkap`, `pendidikan_terakhir`, `pekerjaan`) VALUES
(1, 1, 'wakasa', 'SMA', 'ngusep'),
(2, 3, 'shstu', 'SD', 'gragav'),
(3, 4, 'f', 'SD', 'f');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_pengumuman`
--

CREATE TABLE `tb_pengumuman` (
  `id_pengumuman` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `status_akhir` enum('DINYATAKAN_LULUS','DINYATAKAN_TIDAK_LULUS') NOT NULL,
  `tanggal` datetime DEFAULT current_timestamp(),
  `id_verifikasi` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_pengumuman`
--

INSERT INTO `tb_pengumuman` (`id_pengumuman`, `id_user`, `status_akhir`, `tanggal`, `id_verifikasi`) VALUES
(1, 1, 'DINYATAKAN_LULUS', '2025-07-20 11:15:30', 5),
(3, 3, 'DINYATAKAN_TIDAK_LULUS', '2025-07-20 11:27:42', 6);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_peserta`
--

CREATE TABLE `tb_peserta` (
  `id_user` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','siswa') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_peserta`
--

INSERT INTO `tb_peserta` (`id_user`, `username`, `password`, `role`) VALUES
(1, 'siswa01', 'siswapass', 'siswa'),
(2, 'admin01', 'adminpass', 'admin'),
(3, 'siswa02', 'siswapass', 'siswa'),
(4, 'p', 'p', 'siswa');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_prestasi`
--

CREATE TABLE `tb_prestasi` (
  `id_prestasi` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `nama_prestasi` varchar(100) NOT NULL,
  `tingkat` enum('Sekolah','Kabupaten','Provinsi','Nasional','Internasional') NOT NULL,
  `juara_ke` int(11) NOT NULL,
  `tahun` year(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_prestasi`
--

INSERT INTO `tb_prestasi` (`id_prestasi`, `id_user`, `nama_prestasi`, `tingkat`, `juara_ke`, `tahun`) VALUES
(1, 1, 'iyaa', 'Kabupaten', 1, '2020'),
(2, 3, 'p', 'Sekolah', 1, '2022'),
(3, 4, 'd', 'Sekolah', 1, '2025');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_transkripsi_nilai`
--

CREATE TABLE `tb_transkripsi_nilai` (
  `id` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `semester1` double DEFAULT NULL,
  `semester2` double DEFAULT NULL,
  `semester3` double DEFAULT NULL,
  `semester4` double DEFAULT NULL,
  `semester5` double DEFAULT NULL,
  `rata_rata` double GENERATED ALWAYS AS ((`semester1` + `semester2` + `semester3` + `semester4` + `semester5`) / 5) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_transkripsi_nilai`
--

INSERT INTO `tb_transkripsi_nilai` (`id`, `id_user`, `semester1`, `semester2`, `semester3`, `semester4`, `semester5`) VALUES
(1, 1, 90, 90, 90, 90, 80),
(2, 3, 90, 90, 80, 12, 12);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_verifikasi`
--

CREATE TABLE `tb_verifikasi` (
  `id_verifikasi` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `status` enum('diterima','ditolak') NOT NULL,
  `jadwal_wawancara` datetime DEFAULT NULL,
  `jadwal_tes` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_verifikasi`
--

INSERT INTO `tb_verifikasi` (`id_verifikasi`, `id_user`, `status`, `jadwal_wawancara`, `jadwal_tes`) VALUES
(5, 1, 'ditolak', NULL, NULL),
(6, 3, 'diterima', '2025-07-04 00:00:00', '2025-07-20 02:21:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_zonasi`
--

CREATE TABLE `tb_zonasi` (
  `id_zonasi` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `alamat_rumah` varchar(150) NOT NULL,
  `jarak_km` decimal(5,2) NOT NULL,
  `waktu_tempuh` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_zonasi`
--

INSERT INTO `tb_zonasi` (`id_zonasi`, `id_user`, `alamat_rumah`, `jarak_km`, `waktu_tempuh`) VALUES
(2, 1, 'cibaracak', 5.00, '100');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_asalsekolah`
--
ALTER TABLE `tb_asalsekolah`
  ADD PRIMARY KEY (`id_sekolah`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_datacalonsiswa`
--
ALTER TABLE `tb_datacalonsiswa`
  ADD PRIMARY KEY (`id_calon`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_dokumen`
--
ALTER TABLE `tb_dokumen`
  ADD PRIMARY KEY (`id_dokumen`),
  ADD UNIQUE KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_jalur_jurusan`
--
ALTER TABLE `tb_jalur_jurusan`
  ADD PRIMARY KEY (`id_jalur_jurusan`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_kuota`
--
ALTER TABLE `tb_kuota`
  ADD PRIMARY KEY (`id_kuota`),
  ADD UNIQUE KEY `jurusan` (`jurusan`,`jalur_pendaftaran`);

--
-- Indeks untuk tabel `tb_ortu`
--
ALTER TABLE `tb_ortu`
  ADD PRIMARY KEY (`id_ortu`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_pengumuman`
--
ALTER TABLE `tb_pengumuman`
  ADD PRIMARY KEY (`id_pengumuman`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_verifikasi` (`id_verifikasi`);

--
-- Indeks untuk tabel `tb_peserta`
--
ALTER TABLE `tb_peserta`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indeks untuk tabel `tb_prestasi`
--
ALTER TABLE `tb_prestasi`
  ADD PRIMARY KEY (`id_prestasi`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_transkripsi_nilai`
--
ALTER TABLE `tb_transkripsi_nilai`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_transkrip` (`id_user`);

--
-- Indeks untuk tabel `tb_verifikasi`
--
ALTER TABLE `tb_verifikasi`
  ADD PRIMARY KEY (`id_verifikasi`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `tb_zonasi`
--
ALTER TABLE `tb_zonasi`
  ADD PRIMARY KEY (`id_zonasi`),
  ADD UNIQUE KEY `id_user` (`id_user`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_asalsekolah`
--
ALTER TABLE `tb_asalsekolah`
  MODIFY `id_sekolah` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_datacalonsiswa`
--
ALTER TABLE `tb_datacalonsiswa`
  MODIFY `id_calon` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_dokumen`
--
ALTER TABLE `tb_dokumen`
  MODIFY `id_dokumen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_jalur_jurusan`
--
ALTER TABLE `tb_jalur_jurusan`
  MODIFY `id_jalur_jurusan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_kuota`
--
ALTER TABLE `tb_kuota`
  MODIFY `id_kuota` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `tb_ortu`
--
ALTER TABLE `tb_ortu`
  MODIFY `id_ortu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_pengumuman`
--
ALTER TABLE `tb_pengumuman`
  MODIFY `id_pengumuman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_peserta`
--
ALTER TABLE `tb_peserta`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `tb_prestasi`
--
ALTER TABLE `tb_prestasi`
  MODIFY `id_prestasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `tb_transkripsi_nilai`
--
ALTER TABLE `tb_transkripsi_nilai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `tb_verifikasi`
--
ALTER TABLE `tb_verifikasi`
  MODIFY `id_verifikasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `tb_zonasi`
--
ALTER TABLE `tb_zonasi`
  MODIFY `id_zonasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `tb_asalsekolah`
--
ALTER TABLE `tb_asalsekolah`
  ADD CONSTRAINT `tb_asalsekolah_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tb_datacalonsiswa`
--
ALTER TABLE `tb_datacalonsiswa`
  ADD CONSTRAINT `tb_datacalonsiswa_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`);

--
-- Ketidakleluasaan untuk tabel `tb_dokumen`
--
ALTER TABLE `tb_dokumen`
  ADD CONSTRAINT `tb_dokumen_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tb_jalur_jurusan`
--
ALTER TABLE `tb_jalur_jurusan`
  ADD CONSTRAINT `tb_jalur_jurusan_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`);

--
-- Ketidakleluasaan untuk tabel `tb_ortu`
--
ALTER TABLE `tb_ortu`
  ADD CONSTRAINT `tb_ortu_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tb_pengumuman`
--
ALTER TABLE `tb_pengumuman`
  ADD CONSTRAINT `tb_pengumuman_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`),
  ADD CONSTRAINT `tb_pengumuman_ibfk_2` FOREIGN KEY (`id_verifikasi`) REFERENCES `tb_verifikasi` (`id_verifikasi`);

--
-- Ketidakleluasaan untuk tabel `tb_prestasi`
--
ALTER TABLE `tb_prestasi`
  ADD CONSTRAINT `tb_prestasi_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tb_transkripsi_nilai`
--
ALTER TABLE `tb_transkripsi_nilai`
  ADD CONSTRAINT `fk_user_transkrip` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`);

--
-- Ketidakleluasaan untuk tabel `tb_verifikasi`
--
ALTER TABLE `tb_verifikasi`
  ADD CONSTRAINT `tb_verifikasi_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tb_zonasi`
--
ALTER TABLE `tb_zonasi`
  ADD CONSTRAINT `tb_zonasi_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `tb_peserta` (`id_user`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
