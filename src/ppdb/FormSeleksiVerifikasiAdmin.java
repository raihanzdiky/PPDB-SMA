package ppdb;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormSeleksiVerifikasiAdmin extends JFrame {
    private JTable table;
    private JTextField tfCari;
    private JButton btnCari, btnTerima, btnTolak, btnVerifDok, btnKembali, btnUpdate;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private DefaultTableModel model;

    // Define a consistent color palette
    private final Color PRIMARY_DARK_BLUE = new Color(20, 33, 61);
    private final Color LIGHT_GREY_BACKGROUND = new Color(240, 240, 240);
    private final Color ACCENT_BLUE = new Color(0, 123, 255); // For general actions
    private final Color SUCCESS_GREEN = new Color(40, 167, 69); // For 'Terima'
    private final Color DANGER_RED = new Color(220, 53, 69); // For 'Tolak'
    private final Color WARNING_ORANGE = new Color(255, 193, 7); // For 'Verifikasi Dokumen'
    private final Color WHITE_TEXT = Color.WHITE;
    private final Color BLACK_TEXT = Color.BLACK;
    private final Color TEXT_FIELD_BORDER = new Color(180, 180, 180);
    private final Color TABLE_HEADER_TEXT = Color.WHITE;

    public FormSeleksiVerifikasiAdmin() {
        setTitle("Admin - Seleksi & Verifikasi Pendaftar");
        setSize(1100, 750); // Increased size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Generous spacing
        getContentPane().setBackground(LIGHT_GREY_BACKGROUND); // Overall background

        // --- TOP PANEL: Search ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15)); // Left-aligned, spaced
        searchPanel.setBackground(LIGHT_GREY_BACKGROUND);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20)); // Padding

        JLabel lblCari = createStyledLabel("Cari Nama / NISN:");
        searchPanel.add(lblCari);

        tfCari = createStyledTextField(25); // Wider for search
        searchPanel.add(tfCari);

        btnCari = createStyledButton("Cari", ACCENT_BLUE);
        searchPanel.add(btnCari);
        add(searchPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Table ---
        model = new DefaultTableModel(new String[]{"ID User", "Nama Lengkap", "NISN", "Status Dokumen", "Status Seleksi"}, 0);
        table = new JTable(model);
        styleTable(table); // Apply consistent table styling

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Padding for table
        scroll.getViewport().setBackground(Color.WHITE); // Ensure viewport background is white
        add(scroll, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Actions and Schedule ---
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 20));
        bottomPanel.setBackground(LIGHT_GREY_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Padding

        // Left side of bottomPanel: Scheduling
        JPanel schedulePanel = new JPanel(new GridBagLayout());
        schedulePanel.setBackground(LIGHT_GREY_BACKGROUND);
        schedulePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_DARK_BLUE, 1), // Blue border
            "Pengaturan Jadwal",
            0, 0, // Title position (CENTER, TOP)
            new Font("Segoe UI", Font.BOLD, 14), PRIMARY_DARK_BLUE)); // Title font and color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        schedulePanel.add(createStyledLabel("Jadwal Wawancara:"), gbc);
        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(200, 35)); // Consistent height
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setBorder(BorderFactory.createLineBorder(TEXT_FIELD_BORDER, 1));
        schedulePanel.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        schedulePanel.add(createStyledLabel("Waktu Tes (HH:mm):"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel sm = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        timeSpinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(de);
        timeSpinner.setPreferredSize(new Dimension(150, 35)); // Consistent height
        timeSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeSpinner.setBorder(BorderFactory.createLineBorder(TEXT_FIELD_BORDER, 1));
        schedulePanel.add(timeSpinner, gbc);

        bottomPanel.add(schedulePanel, BorderLayout.WEST);

        // Right side of bottomPanel: Action Buttons
        JPanel actionButtonsPanel = new JPanel(new GridLayout(4, 1, 15, 15)); // Rows, cols, hgap, vgap
        actionButtonsPanel.setBackground(LIGHT_GREY_BACKGROUND);
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20)); // Padding

        btnTerima = createStyledButton("Terima", SUCCESS_GREEN);
        btnTolak = createStyledButton("Tolak", DANGER_RED);
        btnVerifDok = createStyledButton("Verifikasi Dokumen", WARNING_ORANGE);
        btnUpdate = createStyledButton("Update Data", ACCENT_BLUE); // Group with other actions

        actionButtonsPanel.add(btnTerima);
        actionButtonsPanel.add(btnTolak);
        actionButtonsPanel.add(btnVerifDok);
        actionButtonsPanel.add(btnUpdate);
        bottomPanel.add(actionButtonsPanel, BorderLayout.CENTER);

        // Bottom right: Kembali button (aligned to the right)
        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        returnButtonPanel.setBackground(LIGHT_GREY_BACKGROUND);
        btnKembali = createStyledButton("Kembali ke Dashboard", PRIMARY_DARK_BLUE);
        returnButtonPanel.add(btnKembali);
        bottomPanel.add(returnButtonPanel, BorderLayout.SOUTH); // Placed at the very bottom

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnCari.addActionListener(e -> cariData());
        btnTerima.addActionListener(e -> validasiData("DITERIMA"));
        btnTolak.addActionListener(e -> validasiData("DITOLAK"));
        btnVerifDok.addActionListener(e -> verifikasiDokumen());
        btnUpdate.addActionListener(e -> updateVerifikasi());
        btnKembali.addActionListener(e -> {
            dispose();
            // Assuming DashboardAdmin exists and is the main admin entry point
            new DashboardAdmin().setVisible(true);
        });

        // Add mouse listener to the table for detail view
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int idUser = (int) model.getValueAt(row, 0);
                    tampilkanDetailLengkap(idUser);
                }
            }
        });

        tampilkanSemua(); // Load initial data
    }

    // --- Helper Methods for Styling ---
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(BLACK_TEXT);
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10) // Internal padding
        ));
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35)); // Consistent height
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(WHITE_TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for text
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.putClientProperty("FlatLaf.style", "arc: 10"); // Attempt at rounded corners with FlatLaf
        // Add a subtle hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30); // Increased row height
        table.setBackground(Color.WHITE);
        table.setForeground(BLACK_TEXT);
        table.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        table.setSelectionForeground(BLACK_TEXT);
        table.setGridColor(new Color(220, 220, 220)); // Light grid lines

        // Header Styling
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(PRIMARY_DARK_BLUE);
        tableHeader.setForeground(TABLE_HEADER_TEXT);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40)); // Taller header
    }

    // --- Database Operations ---
    private void tampilkanSemua() {
        model.setRowCount(0); // Clear existing data
        try (Connection con = config.configDB();
             PreparedStatement ps = con.prepareStatement(
                "SELECT d.id_user, d.nama_lengkap, d.nisn, " +
                "COALESCE(k.status_verifikasi, 'BELUM DIVERIFIKASI') AS status_dokumen, " +
                "COALESCE(v.status, 'BELUM DISELEKSI') AS status_seleksi " +
                "FROM tb_datacalonsiswa d " +
                "JOIN tb_peserta p ON d.id_user = p.id_user " +
                "LEFT JOIN tb_dokumen k ON d.id_user = k.id_user " +
                "LEFT JOIN tb_verifikasi v ON d.id_user = v.id_user " +
                "WHERE p.role = 'siswa'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_user"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nisn"),
                    rs.getString("status_dokumen"),
                    rs.getString("status_seleksi")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cariData() {
        model.setRowCount(0); // Clear existing data
        try (Connection con = config.configDB()) {
            String keyword = tfCari.getText().trim();
            String sql = "SELECT d.id_user, d.nama_lengkap, d.nisn, " +
                         "COALESCE(k.status_verifikasi, 'BELUM DIVERIFIKASI') AS status_dokumen, " +
                         "COALESCE(v.status, 'BELUM DISELEKSI') AS status_seleksi " +
                         "FROM tb_datacalonsiswa d " +
                         "JOIN tb_peserta p ON d.id_user = p.id_user " +
                         "LEFT JOIN tb_dokumen k ON d.id_user = k.id_user " +
                         "LEFT JOIN tb_verifikasi v ON d.id_user = v.id_user " +
                         "WHERE (d.nama_lengkap LIKE ? OR d.nisn LIKE ?) AND p.role = 'siswa'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_user"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nisn"),
                    rs.getString("status_dokumen"),
                    rs.getString("status_seleksi")
                });
            }
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan untuk kata kunci: '" + keyword + "'", "Informasi Pencarian", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void validasiData(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris data yang akan diproses!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUser = (int) model.getValueAt(row, 0);

        try (Connection con = config.configDB()) {
            // Check if entry already exists in tb_verifikasi
            String checkSql = "SELECT COUNT(*) FROM tb_verifikasi WHERE id_user = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, idUser);
            ResultSet rsCheck = checkPs.executeQuery();
            rsCheck.next();
            boolean entryExists = rsCheck.getInt(1) > 0;
            checkPs.close();

            String sql;
            if (entryExists) {
                sql = "UPDATE tb_verifikasi SET status = ?, jadwal_wawancara = ?, jadwal_tes = ? WHERE id_user = ?";
            } else {
                sql = "INSERT INTO tb_verifikasi (status, jadwal_wawancara, jadwal_tes, id_user) VALUES (?, ?, ?, ?)";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);

            if (status.equals("DITERIMA")) {
                Date wawancaraDate = dateChooser.getDate();
                Date waktuTes = (Date) timeSpinner.getValue();

                if (wawancaraDate == null) {
                    JOptionPane.showMessageDialog(this, "Tanggal wawancara harus diisi untuk status DITERIMA.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

                String wawancara = sdfDate.format(wawancaraDate) + " 00:00:00"; // Assuming 00:00:00 for date only
                // Get today's date for the test date part, then combine with time from spinner
                String jadwalTes = sdfDate.format(new Date()) + " " + sdfTime.format(waktuTes);

                ps.setString(2, wawancara);
                ps.setString(3, jadwalTes);
            } else { // DITOLAK
                ps.setNull(2, Types.TIMESTAMP);
                ps.setNull(3, Types.TIMESTAMP);
            }

            if (entryExists) {
                ps.setInt(4, idUser);
            } else {
                ps.setInt(4, idUser); // id_user is the last parameter for INSERT
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Status seleksi berhasil diubah menjadi: " + status + "!");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada perubahan status seleksi.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
            tampilkanSemua(); // Refresh table
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status seleksi:\n" + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengatur jadwal:\n" + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void verifikasiDokumen() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang dokumennya ingin diverifikasi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUser = (int) model.getValueAt(row, 0);

        try (Connection con = config.configDB()) {
            String update = "UPDATE tb_dokumen SET status_verifikasi = 'SUDAH DIVERIFIKASI' WHERE id_user = ?";
            PreparedStatement ps = con.prepareStatement(update);
            ps.setInt(1, idUser);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "Status dokumen berhasil diverifikasi!");
                tampilkanSemua(); // Refresh table to reflect change
            } else {
                JOptionPane.showMessageDialog(this, "Dokumen tidak ditemukan atau sudah diverifikasi.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal verifikasi dokumen:\n" + ex.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateVerifikasi() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris data yang akan diupdate!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUser = (int) model.getValueAt(row, 0);

        try (Connection con = config.configDB()) {
            String checkSql = "SELECT status FROM tb_verifikasi WHERE id_user = ?";
            PreparedStatement psCheck = con.prepareStatement(checkSql);
            psCheck.setInt(1, idUser);
            ResultSet rsCheck = psCheck.executeQuery();

            if (!rsCheck.next()) {
                JOptionPane.showMessageDialog(this, "Data belum pernah diverifikasi. Gunakan tombol 'Terima' atau 'Tolak' terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String currentStatus = rsCheck.getString("status");

            // Option for status update
            String[] options = {"DITERIMA", "DITOLAK"};
            String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru untuk " + model.getValueAt(row, 1) + ":",
                "Update Status Seleksi",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentStatus // Default selection
            );

            if (newStatus == null) { // User cancelled
                return;
            }

            String sql = "UPDATE tb_verifikasi SET status = ?, jadwal_wawancara = ?, jadwal_tes = ? WHERE id_user = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newStatus);

            if (newStatus.equals("DITERIMA")) {
                Date wawancaraDate = dateChooser.getDate();
                Date waktuTes = (Date) timeSpinner.getValue();

                if (wawancaraDate == null) {
                    JOptionPane.showMessageDialog(this, "Tanggal wawancara harus diisi untuk status DITERIMA.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

                String wawancara = sdfDate.format(wawancaraDate) + " 00:00:00";
                String jadwalTes = sdfDate.format(new Date()) + " " + sdfTime.format(waktuTes);

                ps.setString(2, wawancara);
                ps.setString(3, jadwalTes);
            } else { // DITOLAK
                ps.setNull(2, Types.TIMESTAMP);
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setInt(4, idUser);
            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui menjadi " + newStatus + ".");
                tampilkanSemua();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error update database: " + ex.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void tampilkanDetailLengkap(int idUser) {
    JTextArea detailArea = new JTextArea(25, 60);
    detailArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    detailArea.setEditable(false);
    detailArea.setLineWrap(true);
    detailArea.setWrapStyleWord(true);

    StringBuilder detail = new StringBuilder();
    try (Connection con = config.configDB()) {
        String sql = "SELECT p.username, d.*, o.*, a.*, j.*, doc.*, v.status AS status_verifikasi_admin, v.jadwal_wawancara, v.jadwal_tes " +
                     "FROM tb_peserta p " +
                     "LEFT JOIN tb_datacalonsiswa d ON p.id_user = d.id_user " +
                     "LEFT JOIN tb_ortu o ON p.id_user = o.id_user " +
                     "LEFT JOIN tb_asalsekolah a ON p.id_user = a.id_user " +
                     "LEFT JOIN tb_jalur_jurusan j ON p.id_user = j.id_user " +
                     "LEFT JOIN tb_dokumen doc ON p.id_user = doc.id_user " +
                     "LEFT JOIN tb_verifikasi v ON p.id_user = v.id_user " +
                     "WHERE p.id_user = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idUser);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            detail.append("=== Data Pribadi ===\n");
            detail.append("Username: ").append(rs.getString("username")).append("\n");
            detail.append("Nama Lengkap: ").append(rs.getString("nama_lengkap")).append("\n");
            detail.append("NISN: ").append(rs.getString("nisn")).append("\n");
            detail.append("Tempat/Tgl Lahir: ").append(rs.getString("tempat_lahir")).append(", ").append(rs.getString("tanggal_lahir")).append("\n");
            detail.append("Jenis Kelamin: ").append(rs.getString("jenis_kelamin")).append("\n");
            detail.append("Alamat: ").append(rs.getString("alamat")).append("\n");
            detail.append("No Telepon: ").append(rs.getString("no_tlp")).append("\n");
            detail.append("Email: ").append(rs.getString("email")).append("\n\n");

            detail.append("=== Data Orang Tua ===\n");
            detail.append("Nama Ortu: ").append(rs.getString("o.nama_lengkap")).append("\n");
            detail.append("Pendidikan Terakhir: ").append(rs.getString("pendidikan_terakhir")).append("\n");
            detail.append("Pekerjaan: ").append(rs.getString("pekerjaan")).append("\n\n");

            detail.append("=== Asal Sekolah ===\n");
            detail.append("Nama Sekolah: ").append(rs.getString("nama_sekolah")).append("\n");
            detail.append("Alamat Sekolah: ").append(rs.getString("alamat_sekolah")).append("\n");
            detail.append("Tahun Lulus: ").append(rs.getString("tahun_lulus")).append("\n");
            detail.append("Nilai Rapor Asal: ").append(rs.getString("nilai_raport")).append("\n\n");

            detail.append("=== Jalur & Jurusan ===\n");
            detail.append("Jalur Pendaftaran: ").append(rs.getString("jalur_pendaftaran")).append("\n");
            detail.append("Jurusan Pilihan: ").append(rs.getString("jurusan")).append("\n\n");

            detail.append("=== Status Dokumen ===\n");
            detail.append("Akte Kelahiran: ").append(rs.getString("akte_kelahiran") != null ? "Ada" : "Tidak Ada").append("\n");
            detail.append("Kartu Keluarga: ").append(rs.getString("kartu_keluarga") != null ? "Ada" : "Tidak Ada").append("\n");
            detail.append("Transkrip Nilai/Nilai Rapor: ").append(rs.getString("nilai_raport") != null ? "Ada" : "Tidak Ada").append("\n");
            detail.append("Ijazah: ").append(rs.getString("ijazah") != null ? "Ada" : "Tidak Ada").append("\n");
            detail.append("Foto Latar Merah: ").append(rs.getString("foto_latarmerah") != null ? "Ada" : "Tidak Ada").append("\n");
            detail.append("Status Verifikasi Dokumen: ").append(rs.getString("status_verifikasi")).append("\n\n");

            detail.append("=== Status Seleksi & Jadwal ===\n");
            detail.append("Status Seleksi Admin: ").append(rs.getString("status_verifikasi_admin") == null ? "BELUM DISELEKSI" : rs.getString("status_verifikasi_admin")).append("\n");
            detail.append("Jadwal Wawancara: ").append(rs.getString("jadwal_wawancara") == null ? "-" : rs.getString("jadwal_wawancara")).append("\n");
            detail.append("Jadwal Tes: ").append(rs.getString("jadwal_tes") == null ? "-" : rs.getString("jadwal_tes")).append("\n\n");

            String jalur = rs.getString("jalur_pendaftaran");
            if ("zonasi".equalsIgnoreCase(jalur)) {
                PreparedStatement psZ = con.prepareStatement("SELECT * FROM tb_zonasi WHERE id_user = ?");
                psZ.setInt(1, idUser);
                ResultSet rsZ = psZ.executeQuery();
                if (rsZ.next()) {
                    detail.append("=== Data Zonasi ===\n");
                    detail.append("Alamat Rumah: ").append(rsZ.getString("alamat_rumah")).append("\n");
                    detail.append("Jarak: ").append(rsZ.getString("jarak_km")).append(" km\n");
                    detail.append("Waktu Tempuh: ").append(rsZ.getString("waktu_tempuh")).append("\n\n");
                }
            } else if ("prestasi".equalsIgnoreCase(jalur)) {
                PreparedStatement psP = con.prepareStatement("SELECT * FROM tb_prestasi WHERE id_user = ?");
                psP.setInt(1, idUser);
                ResultSet rsP = psP.executeQuery();
                if (rsP.next()) {
                    detail.append("=== Data Prestasi ===\n");
                    detail.append("Nama Prestasi: ").append(rsP.getString("nama_prestasi")).append("\n");
                    detail.append("Tingkat: ").append(rsP.getString("tingkat")).append("\n");
                    detail.append("Juara ke: ").append(rsP.getInt("juara_ke")).append("\n");
                    detail.append("Tahun: ").append(rsP.getInt("tahun")).append("\n\n");
                }
            } else if ("transkripsi nilai".equalsIgnoreCase(jalur)) {
                PreparedStatement psT = con.prepareStatement("SELECT * FROM tb_transkripsi_nilai WHERE id_user = ?");
                psT.setInt(1, idUser);
                ResultSet rsT = psT.executeQuery();
                if (rsT.next()) {
                    detail.append("=== Transkrip Nilai ===\n");
                    for (int i = 1; i <= 5; i++) {
                        detail.append("Semester ").append(i).append(": ").append(rsT.getDouble("semester" + i)).append("\n");
                    }
                    detail.append("Rata-rata: ").append(rsT.getDouble("rata_rata")).append("\n\n");
                }
            }
        }

        detailArea.setText(detail.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(detailArea), "Detail Data Siswa", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil detail data dari database: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
}