package ppdb;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormPengumumanAdmin extends JFrame {
    private JComboBox<String> comboNama;
    private JTextField txtSekolah, txtJalurJurusan, txtVerifikasi, txtTanggal;
    private JButton btnLulus, btnTidakLulus, btnKembali;
    private JTable tableVerifikasi;
    private DefaultTableModel modelVerifikasi;
    private int idUser = 0; // Inisialisasi dengan 0
    private int idVerifikasi = 0; // Inisialisasi dengan 0

    // Warna kustom
    private final Color PRIMARY_COLOR = new Color(71, 120, 197); // Biru tua
    private final Color SECONDARY_COLOR = new Color(106, 176, 76); // Hijau untuk Lulus
    private final Color DANGER_COLOR = new Color(231, 76, 60); // Merah untuk Tidak Lulus
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Abu-abu sangat muda
    private final Color PANEL_BACKGROUND_COLOR = Color.WHITE; // Putih untuk panel
    private final Color TEXT_COLOR = new Color(44, 62, 80); // Biru gelap
    private final Color TABLE_HEADER_COLOR = new Color(52, 73, 94); // Biru sangat gelap

    public FormPengumumanAdmin() {
        setTitle("Form Pengumuman Admin - PPDB");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Padding antar region
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Panel Judul
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(PRIMARY_COLOR);
        panelHeader.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblTitle = new JLabel("PENGUMUMAN SELEKSI SISWA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle);
        add(panelHeader, BorderLayout.NORTH);

        // Panel Utama (Form Input dan Tombol)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(PANEL_BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Helper untuk membuat JLabel
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = TEXT_COLOR;
        
        // Helper untuk membuat JTextField
        Font textFieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Komponen Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createLabel("Nama Lengkap:", labelFont, labelColor), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        comboNama = new JComboBox<>();
        comboNama.setFont(textFieldFont);
        mainPanel.add(comboNama, gbc);

        // Komponen Asal Sekolah
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        mainPanel.add(createLabel("Asal Sekolah:", labelFont, labelColor), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtSekolah = createTextField(textFieldFont);
        mainPanel.add(txtSekolah, gbc);

        // Komponen Jalur & Jurusan
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        mainPanel.add(createLabel("Jalur & Jurusan:", labelFont, labelColor), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtJalurJurusan = createTextField(textFieldFont);
        mainPanel.add(txtJalurJurusan, gbc);

        // Komponen Status Verifikasi
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        mainPanel.add(createLabel("Status Verifikasi:", labelFont, labelColor), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtVerifikasi = createTextField(textFieldFont);
        mainPanel.add(txtVerifikasi, gbc);

        // Komponen Tanggal Sekarang
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        mainPanel.add(createLabel("Tanggal Pengumuman:", labelFont, labelColor), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        txtTanggal = createTextField(textFieldFont);
        txtTanggal.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss"))); // Format lebih user-friendly
        mainPanel.add(txtTanggal, gbc);

        // Panel Tombol Aksi (Lulus, Tidak Lulus)
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Jarak antar tombol
        panelButtons.setBackground(PANEL_BACKGROUND_COLOR);
        
        btnLulus = createStyledButton("Nyatakan LULUS", SECONDARY_COLOR);
        btnTidakLulus = createStyledButton("Nyatakan TIDAK LULUS", DANGER_COLOR);

        panelButtons.add(btnLulus);
        panelButtons.add(btnTidakLulus);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; // Mengambil 2 kolom
        gbc.weighty = 0; // Tidak berkembang secara vertikal
        mainPanel.add(panelButtons, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panel bawah (Tabel Verifikasi Siswa)
        modelVerifikasi = new DefaultTableModel(new String[]{"Status Verifikasi", "Jadwal Wawancara", "Jadwal Tes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit
            }
        };
        tableVerifikasi = new JTable(modelVerifikasi);
        
        // Styling Tabel Verifikasi
        tableVerifikasi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableVerifikasi.setRowHeight(28);
        tableVerifikasi.setGridColor(new Color(220, 220, 220));
        tableVerifikasi.setSelectionBackground(new Color(200, 220, 240)); // Warna seleksi lebih lembut
        tableVerifikasi.setSelectionForeground(TEXT_COLOR);

        JTableHeader tableHeader = tableVerifikasi.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(TABLE_HEADER_COLOR);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setReorderingAllowed(false);

        // Renderer untuk tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableVerifikasi.getColumnCount(); i++) {
            tableVerifikasi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableVerifikasi);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1), "Detail Verifikasi Siswa Terpilih", 0, 0, new Font("Segoe UI", Font.BOLD, 16), TEXT_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        add(scrollPane, BorderLayout.SOUTH);

        // Tombol Kembali ke Dashboard Admin
        btnKembali = createStyledButton("Kembali ke Dashboard", PRIMARY_COLOR.darker());
        JPanel panelKembali = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Di kiri bawah
        panelKembali.setBackground(BACKGROUND_COLOR);
        panelKembali.add(btnKembali);
        add(panelKembali, BorderLayout.WEST); // Ditempatkan di WEST, bisa juga di SOUTH jika layoutnya pas

        // --- Aksi ---
        loadNamaPeserta();

        comboNama.addActionListener(e -> {
            loadDetailPeserta();
            loadDataVerifikasi();
        });

        btnLulus.addActionListener(e -> simpanPengumuman("DINYATAKAN_LULUS")); // Gunakan nilai ENUM yang benar
        btnTidakLulus.addActionListener(e -> simpanPengumuman("DINYATAKAN_TIDAK_LULUS")); // Gunakan nilai ENUM yang benar
        btnKembali.addActionListener(e -> {
            dispose();
            new DashboardAdmin().setVisible(true);
        });

        // Set txtTanggal to current date and time on load
        txtTanggal.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")));

        // Panggil loadDetailPeserta dan loadDataVerifikasi untuk item pertama saat form dimuat
        if (comboNama.getItemCount() > 0) {
            comboNama.setSelectedIndex(0); // Memilih item pertama secara otomatis
        }
    }

    // --- Helper Methods untuk Styling Komponen ---
    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setEditable(false);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            new EmptyBorder(5, 10, 5, 10) // Padding internal
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efek Hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    // --- Database Logic (unchanged from your original code, but with better error handling messages) ---
    private void loadNamaPeserta() {
        try (Connection conn = config.configDB();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_user, nama_lengkap FROM tb_datacalonsiswa")) {
            comboNama.removeAllItems();
            while (rs.next()) {
                comboNama.addItem(rs.getInt("id_user") + " - " + rs.getString("nama_lengkap"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error memuat daftar peserta: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat peserta: " + e.getMessage(), "Error Umum", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadDetailPeserta() {
        String selected = (String) comboNama.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) {
            // Reset fields if no selection or invalid format
            idUser = 0;
            idVerifikasi = 0;
            txtSekolah.setText("");
            txtJalurJurusan.setText("");
            txtVerifikasi.setText("");
            modelVerifikasi.setRowCount(0);
            return;
        }

        idUser = Integer.parseInt(selected.split(" - ")[0]);

        try (Connection conn = config.configDB()) {
            // Asal Sekolah
            PreparedStatement ps1 = conn.prepareStatement("SELECT nama_sekolah FROM tb_asalsekolah WHERE id_user = ?");
            ps1.setInt(1, idUser);
            ResultSet rs1 = ps1.executeQuery();
            txtSekolah.setText(rs1.next() ? rs1.getString("nama_sekolah") : "Data tidak tersedia");

            // Jalur & Jurusan (bisa lebih dari satu, jadi gabungkan)
            PreparedStatement ps2 = conn.prepareStatement("SELECT jalur_pendaftaran, jurusan FROM tb_jalur_jurusan WHERE id_user = ?");
            ps2.setInt(1, idUser);
            ResultSet rs2 = ps2.executeQuery();
            StringBuilder jalurJurusan = new StringBuilder();
            while (rs2.next()) {
                if (jalurJurusan.length() > 0) {
                    jalurJurusan.append(", ");
                }
                jalurJurusan.append(rs2.getString("jalur_pendaftaran")).append(" (").append(rs2.getString("jurusan")).append(")");
            }
            txtJalurJurusan.setText(jalurJurusan.length() > 0 ? jalurJurusan.toString() : "Data tidak tersedia");

            // Status Verifikasi
            PreparedStatement ps3 = conn.prepareStatement("SELECT id_verifikasi, status FROM tb_verifikasi WHERE id_user = ?");
            ps3.setInt(1, idUser);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                txtVerifikasi.setText(rs3.getString("status"));
                idVerifikasi = rs3.getInt("id_verifikasi");
            } else {
                txtVerifikasi.setText("BELUM DIVERIFIKASI");
                idVerifikasi = 0; // Reset idVerifikasi jika tidak ada data
            }

            txtTanggal.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error memuat detail peserta: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID User tidak valid: " + e.getMessage(), "Error Data", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat detail peserta: " + e.getMessage(), "Error Umum", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadDataVerifikasi() {
        modelVerifikasi.setRowCount(0);
        if (idUser == 0) return; // Jangan load jika idUser tidak valid

        try (Connection conn = config.configDB()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT status, jadwal_wawancara, jadwal_tes FROM tb_verifikasi WHERE id_user = ?");
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelVerifikasi.addRow(new Object[]{
                    rs.getString("status"),
                    rs.getString("jadwal_wawancara") != null ? rs.getTimestamp("jadwal_wawancara").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")) : "Belum ditentukan",
                    rs.getString("jadwal_tes") != null ? rs.getTimestamp("jadwal_tes").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")) : "Belum ditentukan"
                });
            }
            if (modelVerifikasi.getRowCount() == 0) {
                 modelVerifikasi.addRow(new Object[]{"Tidak Ada Data Verifikasi", "", ""});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data verifikasi: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data verifikasi: " + e.getMessage(), "Error Umum", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void simpanPengumuman(String status) {
        if (idUser == 0) {
            JOptionPane.showMessageDialog(this, "Mohon pilih peserta terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Cek apakah status verifikasi adalah "diterima" sebelum bisa dinyatakan lulus
        if (status.equals("DINYATAKAN_LULUS")) {
            String currentVerificationStatus = txtVerifikasi.getText();
            if (!currentVerificationStatus.equalsIgnoreCase("diterima")) {
                JOptionPane.showMessageDialog(this, "Peserta harus berstatus 'Diterima' pada Verifikasi untuk dapat dinyatakan LULUS.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // Tambahan: Jika idVerifikasi masih 0, dan pengumuman akan disimpan, ini bisa jadi masalah.
        // Pastikan idVerifikasi sudah didapatkan dari loadDetailPeserta() sebelum memanggil simpanPengumuman().
        // Ini sudah ditangani sebagian di loadDetailPeserta() dengan idVerifikasi = rs3.getInt("id_verifikasi");
        // Namun jika tidak ada data verifikasi sama sekali (idVerifikasi tetap 0), maka akan ada masalah di Foreign Key tb_pengumuman.id_verifikasi.
        // Berdasarkan skema DB Anda, id_verifikasi di tb_pengumuman adalah NOT NULL.
        // Jadi, jika idVerifikasi adalah 0 (tidak ada data di tb_verifikasi untuk id_user ini), maka tidak bisa INSERT.
        // Ini perlu diputuskan: apakah pengumuman hanya bisa dibuat jika sudah ada entri di tb_verifikasi?
        // Asumsi saya: Ya, pengumuman terkait erat dengan proses verifikasi.
        if (idVerifikasi == 0) {
             JOptionPane.showMessageDialog(this, "Data verifikasi untuk peserta ini belum tersedia. Tidak dapat membuat pengumuman.", "Peringatan", JOptionPane.WARNING_MESSAGE);
             return;
        }


        try (Connection conn = config.configDB()) {
            String sqlCheck = "SELECT COUNT(*) FROM tb_pengumuman WHERE id_user = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, idUser);
            ResultSet rsCheck = psCheck.executeQuery();
            rsCheck.next();
            boolean exists = rsCheck.getInt(1) > 0;

            String sql;
            PreparedStatement ps;

            if (exists) {
                // UPDATE query: UPDATE tb_pengumuman SET status_akhir=?, tanggal=?, id_verifikasi=? WHERE id_user=?
                sql = "UPDATE tb_pengumuman SET status_akhir=?, tanggal=?, id_verifikasi=? WHERE id_user=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, status);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(3, idVerifikasi); // Menggunakan idVerifikasi yang sudah didapatkan
                ps.setInt(4, idUser);      // Kondisi WHERE
            } else {
                // INSERT query: INSERT INTO tb_pengumuman (id_user, status_akhir, tanggal, id_verifikasi) VALUES (?, ?, ?, ?)
                sql = "INSERT INTO tb_pengumuman (id_user, status_akhir, tanggal, id_verifikasi) VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idUser); // Parameter 1 adalah id_user
                ps.setString(2, status);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(4, idVerifikasi); // Parameter 4 adalah id_verifikasi
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Pengumuman berhasil " + (exists ? "diperbarui." : "disimpan."), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan/memperbarui pengumuman.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error SQL saat menyimpan pengumuman: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan pengumuman: " + e.getMessage(), "Error Umum", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Main method untuk menjalankan aplikasi (opsional, tergantung struktur proyek Anda)
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormPengumumanAdmin().setVisible(true));
    }*/
}