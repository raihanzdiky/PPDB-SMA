package ppdb;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // Import LineBorder for potential use in general styling
import javax.swing.border.TitledBorder; // Import TitledBorder for potential use in general styling
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormKelolaDataSiswa extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnKembali;

    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color TABLE_HEADER_COLOR = new Color(44, 62, 80);
    private final Color TEXT_COLOR = new Color(52, 73, 94);

    public FormKelolaDataSiswa() {
        setTitle("Kelola Data Siswa");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel northPanel = new JPanel();
        northPanel.setBackground(PRIMARY_COLOR);
        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("DATA SISWA TERDAFTAR");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        northPanel.add(titleLabel);
        add(northPanel, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        model.addColumn("Username");
        model.addColumn("Nama Lengkap");
        model.addColumn("Nama Orang Tua");
        model.addColumn("Asal Sekolah");
        model.addColumn("Jurusan");
        model.addColumn("Jalur");
        model.addColumn("Dokumen");
        model.addColumn("ID User"); // Kolom tersembunyi untuk ID user

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(TEXT_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // Sembunyikan kolom ID User
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setWidth(0);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        southPanel.setBackground(BACKGROUND_COLOR);

        btnKembali = new JButton("Kembali ke Dashboard");
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnKembali.setBackground(SECONDARY_COLOR);
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        southPanel.add(btnKembali);
        add(southPanel, BorderLayout.SOUTH);

        tampilkanData();

        btnKembali.addActionListener(e -> {
            dispose();
            new DashboardAdmin().setVisible(true); // Sesuaikan dengan kelas DashboardAdmin Anda
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row >= 0 && col >= 0) {
                    String columnName = model.getColumnName(col);
                    String idUser = model.getValueAt(row, 7).toString();
                    String nilai = model.getValueAt(row, col).toString();

                    switch (columnName) {
                        case "Username":
                            tampilkanDetailDariTabel("tb_peserta", "id_user", idUser, columnName);
                            break;
                        case "Nama Lengkap":
                            tampilkanDetailDariTabel("tb_datacalonsiswa", "id_user", idUser, columnName);
                            break;
                        case "Nama Orang Tua":
                            tampilkanDetailDariTabel("tb_ortu", "id_user", idUser, columnName);
                            break;
                        case "Asal Sekolah":
                            tampilkanDetailDariTabel("tb_asalsekolah", "id_user", idUser, columnName);
                            break;
                        case "Jurusan":
                            // Menampilkan detail umum jurusan dari tb_jalur_jurusan
                            tampilkanDetailDariTabel("tb_jalur_jurusan", "id_user", idUser, columnName);
                            // Asumsi Anda ingin menampilkan semua data terkait jalur dari sini juga
                            tampilkanDetailJalurBerdasarkanTipe(idUser);
                            break;
                        case "Jalur":
                            // Memanggil metode baru untuk menampilkan detail jalur pendaftaran spesifik
                            tampilkanDetailJalurBerdasarkanTipe(idUser);
                            break;
                        case "Dokumen":
                            previewDokumen(idUser);
                            break;
                        default:
                            JOptionPane.showMessageDialog(FormKelolaDataSiswa.this, "Nilai: " + nilai, "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    private void tampilkanData() {
        model.setRowCount(0);
        try (Connection conn = config.configDB()) {
            String sql = "SELECT p.id_user, p.username, s.nama_lengkap, o.nama_lengkap AS ortu_nama, " +
                         "a.nama_sekolah, " +
                         "(SELECT GROUP_CONCAT(DISTINCT jj.jurusan SEPARATOR ', ') FROM tb_jalur_jurusan jj WHERE jj.id_user = p.id_user) AS jurusan_list, " +
                         "(SELECT GROUP_CONCAT(DISTINCT jj.jalur_pendaftaran SEPARATOR ', ') FROM tb_jalur_jurusan jj WHERE jj.id_user = p.id_user) AS jalur_list, " +
                         "d.foto_latarmerah " + // Kolom dokumen ini mungkin tidak selalu terisi
                         "FROM tb_peserta p " +
                         "LEFT JOIN tb_datacalonsiswa s ON p.id_user = s.id_user " +
                         "LEFT JOIN tb_ortu o ON p.id_user = o.id_user " +
                         "LEFT JOIN tb_asalsekolah a ON p.id_user = a.id_user " +
                         "LEFT JOIN tb_dokumen d ON p.id_user = d.id_user " +
                         "WHERE p.role = 'siswa'";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getString("nama_lengkap"),
                    rs.getString("ortu_nama"),
                    rs.getString("nama_sekolah"),
                    rs.getString("jurusan_list") != null ? rs.getString("jurusan_list") : "N/A",
                    rs.getString("jalur_list") != null ? rs.getString("jalur_list") : "N/A",
                    // Periksa apakah ada dokumen yang diunggah secara umum
                    (rs.getString("foto_latarmerah") != null || // Cukup satu kolom yang tidak null sudah cukup
                     rs.getString("akte_kelahiran") != null || // Asumsi tb_dokumen punya kolom ini juga, kalau tidak akan error
                     rs.getString("kartu_keluarga") != null ||
                     rs.getString("nilai_raport") != null ||
                     rs.getString("ijazah") != null) ? "Lihat Dokumen" : "Tidak Ada",
                    rs.getInt("id_user")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Metode baru untuk menampilkan detail jalur berdasarkan tipe pendaftaran
    private void tampilkanDetailJalurBerdasarkanTipe(String idUser) {
        try (Connection con = config.configDB()) {
            String sqlJalur = "SELECT jalur_pendaftaran FROM tb_jalur_jurusan WHERE id_user = ?";
            PreparedStatement psJalur = con.prepareStatement(sqlJalur);
            psJalur.setString(1, idUser);
            ResultSet rsJalur = psJalur.executeQuery();

            if (rsJalur.next()) {
                String jalurPendaftaran = rsJalur.getString("jalur_pendaftaran");
                switch (jalurPendaftaran) {
                    case "zonasi":
                        tampilkanDetailDariTabel("tb_zonasi", "id_user", idUser, "Detail Jalur Zonasi");
                        break;
                    case "transkripsi nilai":
                        tampilkanDetailDariTabel("tb_transkripsi_nilai", "id_user", idUser, "Detail Jalur Transkripsi Nilai");
                        break;
                    case "prestasi":
                        tampilkanDetailDariTabel("tb_prestasi", "id_user", idUser, "Detail Jalur Prestasi");
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Tipe jalur pendaftaran tidak dikenal: " + jalurPendaftaran, "Info", JOptionPane.INFORMATION_MESSAGE);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Data jalur pendaftaran tidak ditemukan untuk user ini.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mendapatkan detail jalur pendaftaran: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void tampilkanDetailDariTabel(String namaTabel, String kolomWhere, String idUser, String judul) {
        StringBuilder detail = new StringBuilder();
        try (Connection con = config.configDB()) {
            String sql = "SELECT * FROM " + namaTabel + " WHERE " + kolomWhere + " = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    detail.append(meta.getColumnName(i).replace("_", " ").toUpperCase()).append(": ")
                                .append(rs.getString(i)).append("\n");
                }
            } else {
                detail.append("Data tidak ditemukan di tabel ").append(namaTabel).append(".");
            }

            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Detail: " + judul, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan detail: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void previewDokumen(String idUser) {
        try (Connection conn = config.configDB()) {
            String sql = "SELECT * FROM tb_dokumen WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, String> dokumenMap = new LinkedHashMap<>();
                dokumenMap.put("Akte Kelahiran", rs.getString("akte_kelahiran"));
                dokumenMap.put("Kartu Keluarga", rs.getString("kartu_keluarga"));
                dokumenMap.put("Nilai Raport", rs.getString("nilai_raport"));
                dokumenMap.put("Ijazah", rs.getString("ijazah"));
                dokumenMap.put("Foto Latar Merah", rs.getString("foto_latarmerah"));

                // --- Bagian baru: Menampilkan semua data dokumen dalam satu pop-up ---
                StringBuilder dokumenSummary = new StringBuilder("Ringkasan Dokumen:\n\n");
                boolean hasDocuments = false;
                for (Map.Entry<String, String> entry : dokumenMap.entrySet()) {
                    String nama = entry.getKey();
                    String path = entry.getValue();
                    String status = (path != null && !path.isEmpty()) ? "Ada (Klik OK untuk preview)" : "Tidak Ada";
                    dokumenSummary.append(nama).append(": ").append(status).append("\n");
                    if (path != null && !path.isEmpty()) {
                        hasDocuments = true;
                    }
                }

                if (!hasDocuments) {
                    JOptionPane.showMessageDialog(this, "Tidak ada dokumen yang diunggah oleh siswa ini.", "Info Dokumen", JOptionPane.INFORMATION_MESSAGE);
                    return; // Keluar jika tidak ada dokumen sama sekali
                }

                JTextArea textArea = new JTextArea(dokumenSummary.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 200));

                JOptionPane.showMessageDialog(this, scrollPane, "Status Dokumen Siswa", JOptionPane.INFORMATION_MESSAGE);
                // --- Akhir bagian baru ---

                // Lanjutkan dengan loop untuk bertanya ingin melihat file satu per satu
                for (Map.Entry<String, String> entry : dokumenMap.entrySet()) {
                    String nama = entry.getKey();
                    String path = entry.getValue();
                    if (path != null && !path.isEmpty()) {
                        int jawab = JOptionPane.showConfirmDialog(this, "Apakah Anda ingin melihat file " + nama + "?", "Preview Dokumen", JOptionPane.YES_NO_OPTION);
                        if (jawab == JOptionPane.YES_OPTION) {
                            File file = new File(path);
                            if (file.exists() && Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(file);
                            } else {
                                JOptionPane.showMessageDialog(this, "File " + nama + " tidak ditemukan atau tidak dapat dibuka. Pastikan path file benar: " + path, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data dokumen ditemukan untuk ID User ini.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuka dokumen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}