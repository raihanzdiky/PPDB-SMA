package ppdb;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class FormPengumumanSiswa extends JFrame {
    private JTextField tfNISN;
    private JLabel lblStatus, lblNama, lblNISN, lblSekolah, lblTanggal;
    private JPanel panelHasil;

    // Warna Kustom (konsisten dengan form lain jika ada)
    private final Color PRIMARY_COLOR = new Color(34, 49, 63); // Dark Blue-Gray for header/accent
    private final Color SECONDARY_COLOR = new Color(52, 152, 219); // Bright Blue for buttons/highlights
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray for main background
    private final Color TEXT_COLOR_DARK = new Color(44, 62, 80); // Dark text color
    private final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green for "Dinyatakan Lulus"
    private final Color DANGER_COLOR = new Color(231, 76, 60); // Red for "Tidak Lulus"

    public FormPengumumanSiswa() {
        setTitle("Cek Hasil PPDB");
        setSize(600, 650); // Ukuran lebih proporsional
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Gunakan BorderLayout untuk struktur utama
        getContentPane().setBackground(BACKGROUND_COLOR); // Set background untuk frame

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblJudul = new JLabel("Sistem Informasi PPDB");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblJudul.setForeground(Color.WHITE);
        headerPanel.add(lblJudul);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content Panel (using GridBagLayout for flexible arrangement) ---
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        mainContentPanel.setLayout(new GridBagLayout());
        mainContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding untuk konten utama

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Membentang 2 kolom

        // Sub-panel untuk input NISN dan tombol Cek
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR.brighter(), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(5, 5, 5, 5);
        inputGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblInstruksi = new JLabel("Masukkan NISN Anda untuk mengecek hasil seleksi:");
        lblInstruksi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInstruksi.setForeground(TEXT_COLOR_DARK);
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputGbc.gridwidth = 2;
        inputPanel.add(lblInstruksi, inputGbc);

        tfNISN = new JTextField(20);
        tfNISN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tfNISN.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SECONDARY_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        inputGbc.gridy = 1;
        inputGbc.gridwidth = 1;
        inputGbc.weightx = 0.7;
        inputPanel.add(tfNISN, inputGbc);

        JButton btnCek = createStyledButton("Cek Hasil", SECONDARY_COLOR);
        inputGbc.gridx = 1;
        inputGbc.gridy = 1;
        inputGbc.weightx = 0.3;
        inputPanel.add(btnCek, inputGbc);

        gbc.gridy = 0;
        mainContentPanel.add(inputPanel, gbc);

        // Panel Hasil Pengumuman
        panelHasil = new JPanel();
        panelHasil.setLayout(new GridBagLayout());
        panelHasil.setBackground(Color.WHITE);
        panelHasil.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(TEXT_COLOR_DARK.darker(), 1),
            "Hasil Pengumuman Seleksi",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            TEXT_COLOR_DARK
        ));
        panelHasil.setVisible(false); // Sembunyikan secara default

        GridBagConstraints hasilGbc = new GridBagConstraints();
        hasilGbc.insets = new Insets(8, 15, 8, 15);
        hasilGbc.fill = GridBagConstraints.HORIZONTAL;
        hasilGbc.anchor = GridBagConstraints.WEST;
        hasilGbc.weightx = 1.0;

        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Font besar untuk status
        hasilGbc.gridx = 0;
        hasilGbc.gridy = 0;
        hasilGbc.gridwidth = 2;
        hasilGbc.ipady = 10; // Padding vertikal
        panelHasil.add(lblStatus, hasilGbc);

        hasilGbc.gridwidth = 1; // Reset gridwidth
        hasilGbc.ipady = 0; // Reset ipady

        lblNama = new JLabel("");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNama.setForeground(TEXT_COLOR_DARK);
        hasilGbc.gridy = 1;
        panelHasil.add(lblNama, hasilGbc);

        lblNISN = new JLabel("");
        lblNISN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNISN.setForeground(TEXT_COLOR_DARK);
        hasilGbc.gridy = 2;
        panelHasil.add(lblNISN, hasilGbc);

        lblSekolah = new JLabel("");
        lblSekolah.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSekolah.setForeground(TEXT_COLOR_DARK);
        hasilGbc.gridy = 3;
        panelHasil.add(lblSekolah, hasilGbc);

        lblTanggal = new JLabel("");
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTanggal.setForeground(TEXT_COLOR_DARK);
        hasilGbc.gridy = 4;
        panelHasil.add(lblTanggal, hasilGbc);

        // Tombol Kembali dalam panelHasil (ditempatkan di tengah bawah)
        JButton btnKembali = createStyledButton("Kembali Ke Menu Utama", PRIMARY_COLOR);
        hasilGbc.gridy = 5;
        hasilGbc.gridx = 0;
        hasilGbc.gridwidth = 2;
        hasilGbc.insets = new Insets(20, 15, 5, 15); // Padding lebih besar di atas
        hasilGbc.anchor = GridBagConstraints.CENTER;
        panelHasil.add(btnKembali, hasilGbc);

        gbc.gridy = 1; // Posisikan panelHasil di bawah inputPanel
        gbc.weighty = 1.0; // Berikan ruang vertikal ekstra
        gbc.anchor = GridBagConstraints.NORTH; // Ratakan ke atas
        mainContentPanel.add(panelHasil, gbc);

        add(mainContentPanel, BorderLayout.CENTER);

        // --- Event Listeners ---
        btnCek.addActionListener(e -> cekHasilSeleksi());

        btnKembali.addActionListener(e -> {
            dispose();
            // Anda mungkin perlu menyesuaikan ini dengan kelas DashboardSiswa Anda
            // new DashboardSiswa().setVisible(true);
        });
    }

    // Helper method untuk membuat tombol dengan styling
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding tombol
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Ubah kursor saat hover
        button.setOpaque(true); // Pastikan warna background terlihat

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

    private void cekHasilSeleksi() {
        String nisn = tfNISN.getText().trim();

        if (nisn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NISN terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            panelHasil.setVisible(false);
            return;
        }

        try (Connection con = config.configDB()) {
            String sql = "SELECT ds.nama_lengkap, ds.nisn, sk.nama_sekolah, p.status_akhir, p.tanggal " +
                         "FROM tb_datacalonsiswa ds " +
                         "JOIN tb_pengumuman p ON ds.id_user = p.id_user " +
                         "JOIN tb_asalsekolah sk ON ds.id_user = sk.id_user " +
                         "WHERE ds.nisn = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nisn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nama = rs.getString("nama_lengkap");
                String sekolah = rs.getString("nama_sekolah");
                String status = rs.getString("status_akhir");
                Date tanggal = rs.getDate("tanggal");

                if ("DINYATAKAN_LULUS".equalsIgnoreCase(status)) {
                    lblStatus.setForeground(SUCCESS_COLOR);
                    lblStatus.setText("SELAMAT! ANDA DINYATAKAN LULUS!");
                } else {
                    lblStatus.setForeground(DANGER_COLOR);
                    lblStatus.setText("MAAF, ANDA DINYATAKAN TIDAK LULUS.");
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                lblNama.setText("Nama Lengkap: " + nama);
                lblNISN.setText("NISN: " + nisn);
                lblSekolah.setText("Asal Sekolah: " + sekolah);
                lblTanggal.setText("Tanggal Pengumuman: " + (tanggal != null ? dateFormat.format(tanggal) : "Belum Tersedia"));

                panelHasil.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Data NISN tidak ditemukan atau hasil belum diumumkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                panelHasil.setVisible(false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    }
