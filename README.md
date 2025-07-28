# PPDB-SMA
Aplikasi PPDB sistem yang digunakan untuk mempermudah proses pendaftaran siswa baru secara digital. Melalui aplikasi ini, calon siswa dapat mendaftar, mengisi data, dan memantau status pendaftaran secara online. Pihak sekolah dapat mengelola data pendaftar, melakukan verifikasi, serta mengumumkan hasil seleksi secara lebih efisien dan transparan.

========================================
README - Pembagian Tugas Coding PPDB
========================================

 Nama Proyek : Aplikasi Penerimaan Peserta Didik Baru (PPDB)  
 Bahasa       : Java Netbeans IDE 8.2 (Swing), Xampp controller  
 Deskripsi    : Aplikasi ini digunakan untuk mempermudah proses pendaftaran siswa baru secara digital 
                  dengan fitur login, pendaftaran, verifikasi, pengumuman, dan dashboard admin/siswa.

========================================
👥 ANGGOTA TIM DAN KONTRIBUSI
========================================

1. **Diky Raihan Subagja (24552011194)**
   - **Peran     : Ketua Tim & Lead Fullstack Developer**
   - **Kontribusi Utama (Mayoritas Coding):**
     - Merancang keseluruhan arsitektur proyek (MVC, struktur package, dan class)
     - Mengembangkan hampir seluruh core aplikasi termasuk:
       - `MainMenu.java`, sistem navigasi, dan integrasi semua form  
       - membuat seluruh fitur pada form admin dan mengembangkan 2 fitur siswa (`FormSeleksiVerifikasi.java`, `FormpengumumanSiswa.java`) 

2. **Rio Nugraha Putra (24552011060)**
   - **Peran     : Backend Developer (Database & Logic)**
   - **Kontribusi Utama (Mayoritas Coding di Backend ):**
     - Mendesain struktur database dan membuat seluruh query utama termasuk coding aplikasi
     - Mengembangkan koneksi database (`config.java`) dan modul sesi login (`Session.java`)
     - Menulis kode validasi input, autentikasi login.
     - mengembangkan fitur data diri siswa fitur siswa (`PanelDataDiri.java`, `PanelAsalSekolah.java`, `PanelDokumen.java`, `PanelOrtu.java`, `FormStatistikSiswa.java`)

3. **Firman Satrio Aji (24552011022)**
   - Peran     : Developer UI & UX
   - Kontribusi:
     - Mendesain seluruh tampilan form  
     - Mengatur layout serta mempercantik panel 
     - Menambahkan ikon/gambar pada package (`gambar`) serta memperbaiki positioning elemen UI
     - membuat panel jalur jurusan (`PanelJalurJurusan.java`)

4. **Muhammad Fachri Athallah Sofyan (24552011117)**
   - Peran     : Developer Modul Jalur pendaftaran
   - Kontribusi:
     - Membuat panel zonasi (`FormZonasi.java`)
     - Membuat panel prestasi (`FormPrestasi.java`)
     - Membuat panel zonasi (`FormTranskrip.java`)

5. **Dhenia Putri Nuraini (24552011311)**
   - Peran     : Developer Modul dashboard
     - Kontribusi:
     - Membuat dashboard siswa (`DashboardSiswa.java`)
     - Membuat dashboard admin (`DashboardAdmin.java`)
     - mengintegrasikan ke form selanjutnya dan sebelumnya

6. **Nadia Salma Nurhidayah (24552011044)**
   - Peran     :Developer Modul Login dan Register
   - Kontribusi:
     - Membuat Form Login (`LoginForm.java`)
     - Mmebuat Form Register (`RegisterForm.java`)
     - mengintegrasikan ke form selanjutnya dan sebelumnya

========================================
CATATAN
========================================
- Seluruh anggota ikut berkontribusi dalam debugging dan pengujian aplikasi.
- Aplikasi ini dibuat untuk keperluan pembelajaran dan dapat dikembangkan lebih lanjut.
- Untuk menjalankan aplikasi, pastikan konfigurasi database pada config.java sudah sesuai.


===========================================
DIBUAT OLEH TIM Kelompok 4 TIFRP24B - 2025
===========================================
