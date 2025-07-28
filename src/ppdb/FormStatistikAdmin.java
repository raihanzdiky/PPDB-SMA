package ppdb;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormStatistikAdmin extends JFrame {
    private JButton btnTotalPendaftar, btnTotalLulus, btnTotalTidakLulus, btnDalamProses, btnAturKuota, btnKembali;
    private JTable table;
    private DefaultTableModel model;
    private JPanel panelHeader, panelTombol, panelTable; // Renamed for clarity
    private JLabel lblJudul;

    // Define a color palette for a more appealing UI
    private final Color PRIMARY_DARK_BLUE = new Color(20, 33, 61); // For background/primary elements
    private final Color LIGHT_GREY_BACKGROUND = new Color(240, 240, 240); // General background
    private final Color ACCENT_BLUE = new Color(0, 123, 255); // For buttons/highlights
    private final Color SUCCESS_GREEN = new Color(40, 167, 69); // For 'Lulus' related buttons
    private final Color DANGER_RED = new Color(220, 53, 69); // For 'Tidak Lulus' related buttons
    private final Color WARNING_ORANGE = new Color(255, 193, 7); // For 'Dalam Proses'
    private final Color WHITE_TEXT = Color.WHITE; // For text on dark backgrounds
    private final Color BLACK_TEXT = Color.BLACK; // For text on light backgrounds


    private final String DB_URL = "jdbc:mysql://localhost:3306/ppdbsaya";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    public FormStatistikAdmin() {
        setTitle("Admin - Statistik Pendaftaran Siswa");
        setSize(1200, 700); // Increased size for better layout
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Increased gaps

        // Set overall background
        getContentPane().setBackground(LIGHT_GREY_BACKGROUND);

        // Header Panel
        panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setBackground(PRIMARY_DARK_BLUE); // Dark blue header
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Padding
        lblJudul = new JLabel("STATISTIK PENDAFTARAN SISWA");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger, bolder title
        lblJudul.setForeground(WHITE_TEXT); // White text on dark blue
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        // Button Panel
        panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); // Increased button spacing
        panelTombol.setBackground(LIGHT_GREY_BACKGROUND); // Match main background
        panelTombol.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        btnTotalPendaftar = createStyledButton("Total Pendaftar", ACCENT_BLUE);
        btnTotalLulus = createStyledButton("Total Lulus", SUCCESS_GREEN);
        btnTotalTidakLulus = createStyledButton("Total Tidak Lulus", DANGER_RED);
        btnDalamProses = createStyledButton("Total Dalam Proses", WARNING_ORANGE);
        btnAturKuota = createStyledButton("Atur Kuota", PRIMARY_DARK_BLUE); // Different color for settings
        btnKembali = createStyledButton("Kembali ke Dashboard", Color.GRAY); // Gray for neutral action

        panelTombol.add(btnTotalPendaftar);
        panelTombol.add(btnTotalLulus);
        panelTombol.add(btnTotalTidakLulus);
        panelTombol.add(btnDalamProses);
        panelTombol.add(btnAturKuota);
        panelTombol.add(btnKembali);

        add(panelTombol, BorderLayout.SOUTH);

        // Table Panel
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nama Lengkap", "NISN", "Asal Sekolah", "Jurusan", "Jalur Pendaftaran", "Status Akhir"}); // Updated column for 'Status'

        table = new JTable(model);
        table.setFillsViewportHeight(true); // Table fills the viewport height
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30); // Increased row height for better readability
        table.setBackground(Color.WHITE); // White background for table rows
        table.setForeground(BLACK_TEXT);
        table.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        table.setSelectionForeground(BLACK_TEXT);
        table.setGridColor(new Color(220, 220, 220)); // Light grid lines

        // Table Header Styling
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Bold header font
        tableHeader.setBackground(PRIMARY_DARK_BLUE); // Dark blue header background
        tableHeader.setForeground(WHITE_TEXT); // White text for header
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40)); // Increased header height

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Padding around table
        scrollPane.getViewport().setBackground(Color.WHITE); // Ensure scroll pane background matches table if needed

        panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(LIGHT_GREY_BACKGROUND);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        add(panelTable, BorderLayout.CENTER);

        // Listener tombol
        btnTotalPendaftar.addActionListener(e -> tampilkanData("SELECT dc.nama_lengkap, dc.nisn, sk.nama_sekolah, jj.jurusan, jj.jalur_pendaftaran, COALESCE(p.status_akhir, 'BELUM DIPROSES') AS status_display FROM tb_datacalonsiswa dc LEFT JOIN tb_asalsekolah sk ON dc.id_user = sk.id_user LEFT JOIN tb_jalur_jurusan jj ON dc.id_user = jj.id_user LEFT JOIN tb_pengumuman p ON dc.id_user = p.id_user", "Total Pendaftar"));

        btnTotalLulus.addActionListener(e -> tampilkanData("SELECT dc.nama_lengkap, dc.nisn, sk.nama_sekolah, jj.jurusan, jj.jalur_pendaftaran, p.status_akhir FROM tb_datacalonsiswa dc JOIN tb_asalsekolah sk ON dc.id_user = sk.id_user JOIN tb_jalur_jurusan jj ON dc.id_user = jj.id_user JOIN tb_pengumuman p ON dc.id_user = p.id_user WHERE p.status_akhir = 'DINYATAKAN_LULUS'", "Total Lulus"));

        btnTotalTidakLulus.addActionListener(e -> tampilkanData("SELECT dc.nama_lengkap, dc.nisn, sk.nama_sekolah, jj.jurusan, jj.jalur_pendaftaran, p.status_akhir FROM tb_datacalonsiswa dc JOIN tb_asalsekolah sk ON dc.id_user = sk.id_user JOIN tb_jalur_jurusan jj ON dc.id_user = jj.id_user JOIN tb_pengumuman p ON dc.id_user = p.id_user WHERE p.status_akhir = 'DINYATAKAN_TIDAK_LULUS'", "Total Tidak Lulus"));

        btnDalamProses.addActionListener(e -> tampilkanData(
                "SELECT dc.nama_lengkap, dc.nisn, sk.nama_sekolah, jj.jurusan, jj.jalur_pendaftaran, d.status_verifikasi " +
                "FROM tb_datacalonsiswa dc " +
                "LEFT JOIN tb_asalsekolah sk ON dc.id_user = sk.id_user " +
                "LEFT JOIN tb_jalur_jurusan jj ON dc.id_user = jj.id_user " +
                "LEFT JOIN tb_dokumen d ON dc.id_user = d.id_user " +
                "WHERE d.status_verifikasi = 'BELUM DIVERIFIKASI' OR d.status_verifikasi IS NULL", // Include null for robustness
                "Total Dalam Proses"
        ));

        btnAturKuota.addActionListener(e -> {
            // Check if FormAturKuota is already open to prevent multiple instances
            Window[] windows = Window.getWindows();
            boolean isOpen = false;
            for (Window window : windows) {
                if (window instanceof FormAturKuota && window.isShowing()) {
                    window.toFront(); // Bring existing window to front
                    isOpen = true;
                    break;
                }
            }
            if (!isOpen) {
                new FormAturKuota().setVisible(true); // Create and show new form
            }
        });
        btnKembali.addActionListener(e -> dispose());

        // Initial load
        tampilkanData("SELECT dc.nama_lengkap, dc.nisn, sk.nama_sekolah, jj.jurusan, jj.jalur_pendaftaran, COALESCE(p.status_akhir, 'BELUM DIPROSES') AS status_display FROM tb_datacalonsiswa dc LEFT JOIN tb_asalsekolah sk ON dc.id_user = sk.id_user LEFT JOIN tb_jalur_jurusan jj ON dc.id_user = jj.id_user LEFT JOIN tb_pengumuman p ON dc.id_user = p.id_user", "Total Pendaftar");

        setVisible(true);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(WHITE_TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for text
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
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

    private void tampilkanData(String query, String judul) {
        // Update the table header based on the query, specifically for "Dalam Proses" which uses `d.status_verifikasi`
        if (judul.equals("Total Dalam Proses")) {
            model.setColumnIdentifiers(new String[]{"Nama Lengkap", "NISN", "Asal Sekolah", "Jurusan", "Jalur Pendaftaran", "Status Verifikasi"});
        } else {
            model.setColumnIdentifiers(new String[]{"Nama Lengkap", "NISN", "Asal Sekolah", "Jurusan", "Jalur Pendaftaran", "Status Akhir"});
        }
        lblJudul.setText("STATISTIK PENDAFTARAN SISWA - " + judul.toUpperCase()); // Update header title

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            model.setRowCount(0); // Clear existing data
            int total = 0;
            while (rs.next()) {
                // Ensure correct column indices based on your query's SELECT order
                // The `COALESCE` in the "Total Pendaftar" query handles potential NULLs for status_akhir
                String status = rs.getString(6);
                if (status == null || status.isEmpty()) {
                    status = "BELUM DIVERIFIKASI"; // Default status if null, matching 'Dalam Proses' logic
                }
                model.addRow(new Object[]{
                    rs.getString(1), // nama_lengkap
                    rs.getString(2), // nisn
                    rs.getString(3), // nama_sekolah
                    rs.getString(4), // jurusan
                    rs.getString(5), // jalur_pendaftaran
                    status             // status_akhir or status_verifikasi
                });
                total++;
            }
            JOptionPane.showMessageDialog(this, judul + ": " + total + " peserta");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data dari database!\n" + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}