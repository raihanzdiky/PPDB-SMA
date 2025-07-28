package ppdb;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormStatistikSiswa extends JFrame {

    private JLabel lblTotalPendaftar, lblLulus, lblTidakLulus, lblDalamProses, lblIPA, lblIPS;
    private JButton btnRefresh, btnKembali;
    private int idUser;

    // Warna Kustom
    private final Color PRIMARY_COLOR = new Color(34, 49, 63); // Dark Blue-Gray for header/accent
    private final Color SECONDARY_COLOR = new Color(52, 152, 219); // Bright Blue for buttons/highlights
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray for main background
    private final Color TEXT_COLOR_DARK = new Color(44, 62, 80); // Dark text color
    private final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green for success
    private final Color DANGER_COLOR = new Color(231, 76, 60); // Red for danger
    private final Color WARNING_COLOR = new Color(241, 196, 15); // Yellow for warning/in-progress

    public FormStatistikSiswa(int idUser) {
        this.idUser = idUser;
        setTitle("Statistik PPDB Siswa");
        setSize(700, 550); // Ukuran lebih besar untuk tampilan yang lebih lega
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Gunakan BorderLayout untuk struktur utama
        getContentPane().setBackground(BACKGROUND_COLOR); // Set background untuk frame

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Statistik Pendaftaran PPDB");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content Panel ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout()); // Gunakan GridBagLayout untuk fleksibilitas
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Melebar secara horizontal

        // Statistik Umum Pendaftar
        JPanel generalStatsPanel = createSectionPanel("Ringkasan Pendaftar");
        generalStatsPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Grid untuk statistik
        lblTotalPendaftar = createStatisticLabel("Total Pendaftar:", "/gambar/iconactor2.png");
        lblLulus = createStatisticLabel("Total Dinyatakan Lulus:", "/gambar/ceklis.png");
        lblTidakLulus = createStatisticLabel("Total Tidak Lulus:", "/gambar/iconsilang.png");
        lblDalamProses = createStatisticLabel("Total Dalam Proses:", "/gambar/dalamproses.png");

        generalStatsPanel.add(lblTotalPendaftar);
        generalStatsPanel.add(lblLulus);
        generalStatsPanel.add(lblTidakLulus);
        generalStatsPanel.add(lblDalamProses);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Mengambil dua kolom
        contentPanel.add(generalStatsPanel, gbc);

        // Statistik Jurusan
        JPanel majorStatsPanel = createSectionPanel("Statistik Jurusan Pilihan");
        majorStatsPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Grid untuk jurusan
        lblIPA = createStatisticLabel("Total Memilih IPA:", "/gambar/ipa.png");
        lblIPS = createStatisticLabel("Total Memilih IPS:", "/gambar/ips.png");

        majorStatsPanel.add(lblIPA);
        majorStatsPanel.add(lblIPS);

        gbc.gridy = 1;
        contentPanel.add(majorStatsPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // --- Footer Panel (Buttons) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15)); // Tombol di kanan bawah
        footerPanel.setBorder(new EmptyBorder(0, 20, 15, 20));

        btnRefresh = createStyledButton("Refresh Data", SECONDARY_COLOR);
        btnKembali = createStyledButton("Kembali ke Dashboard", new Color(108, 117, 125)); // Warna abu-abu

        footerPanel.add(btnRefresh);
        footerPanel.add(btnKembali);
        add(footerPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnRefresh.addActionListener(e -> tampilkanStatistik());
        btnKembali.addActionListener(e -> {
            dispose();
            // Asumsi kembali ke DashboardSiswa jika idUser adalah siswa,
            // atau DashboardAdmin jika form ini diakses dari admin.
            // Anda mungkin perlu logika lebih lanjut untuk ini,
            // atau pastikan form ini hanya dibuka dari satu dashboard.
            // Contoh: new DashboardSiswa().setVisible(true);
        });

        tampilkanStatistik(); // Muat data awal
    }

    // Helper method untuk membuat panel bagian
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TEXT_COLOR_DARK, 1), // Border luar
            new EmptyBorder(15, 15, 15, 15) // Padding dalam
        ));
        panel.setLayout(new BorderLayout()); // Gunakan BorderLayout untuk judul dan konten
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding bawah judul
        panel.add(sectionTitle, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        panel.add(content, BorderLayout.CENTER); // Konten akan ditambahkan ke panel ini
        return content; // Mengembalikan panel konten agar statistik bisa ditambahkan langsung
    }

    // Helper method untuk membuat label statistik dengan icon
    private JLabel createStatisticLabel(String prefix, String iconPath) {
        JLabel label = new JLabel(prefix);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(TEXT_COLOR_DARK);
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                // Skala icon agar tidak terlalu besar
                ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
                Image image = originalIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly
                label.setIcon(new ImageIcon(newimg));
                label.setIconTextGap(10); // Spasi antara icon dan teks
            } catch (Exception e) {
                System.err.println("Gagal memuat atau menskalakan ikon: " + iconPath + " - " + e.getMessage());
            }
        }
        return label;
    }

    // Overload untuk label tanpa icon
    private JLabel createStatisticLabel(String prefix) {
        return createStatisticLabel(prefix, null);
    }

    // Helper method untuk membuat tombol dengan styling
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding tombol
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Ubah kursor saat hover

        // Tambahkan efek hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void tampilkanStatistik() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ppdbsaya", "root", "")) {
            Statement stmt = conn.createStatement();

            // Total Pendaftar (Role Siswa)
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_peserta WHERE role = 'siswa'");
            if (rs.next()) lblTotalPendaftar.setText("Total Pendaftar: " + rs.getInt(1));

            // Total Dinyatakan Lulus
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_pengumuman WHERE status_akhir = 'DINYATAKAN_LULUS'");
            if (rs.next()) lblLulus.setText("Total Dinyatakan Lulus: " + rs.getInt(1));
            lblLulus.setForeground(SUCCESS_COLOR); // Warna hijau untuk lulus

            // Total Dinyatakan Tidak Lulus
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_pengumuman WHERE status_akhir = 'DINYATAKAN_TIDAK_LULUS'");
            if (rs.next()) lblTidakLulus.setText("Total Tidak Lulus: " + rs.getInt(1));
            lblTidakLulus.setForeground(DANGER_COLOR); // Warna merah untuk tidak lulus

            // Total Dalam Proses (belum diverifikasi dokumen ATAU status belum diseleksi)
            rs = stmt.executeQuery("SELECT COUNT(DISTINCT p.id_user) FROM tb_peserta p " +
                                  "LEFT JOIN tb_dokumen d ON p.id_user = d.id_user " +
                                  "LEFT JOIN tb_verifikasi v ON p.id_user = v.id_user " +
                                  "WHERE p.role = 'siswa' AND (d.status_verifikasi = 'BELUM DIVERIFIKASI' OR v.status = 'BELUM DISELEKSI' OR v.status IS NULL)");
            if (rs.next()) lblDalamProses.setText("Total Dalam Proses: " + rs.getInt(1));
            lblDalamProses.setForeground(WARNING_COLOR); // Warna kuning untuk dalam proses

            // Total yang memilih IPA
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_jalur_jurusan WHERE jurusan = 'IPA'");
            if (rs.next()) lblIPA.setText("Total Memilih IPA: " + rs.getInt(1));
            lblIPA.setForeground(TEXT_COLOR_DARK);

            // Total yang memilih IPS
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_jalur_jurusan WHERE jurusan = 'IPS'");
            if (rs.next()) lblIPS.setText("Total Memilih IPS: " + rs.getInt(1));
            lblIPS.setForeground(TEXT_COLOR_DARK);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error mengambil data statistik: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}