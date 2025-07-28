package ppdb;

import com.toedter.calendar.JYearChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PanelAsalSekolah extends JPanel {
    private JTextField namaSekolahField, alamatSekolahField, nilaiRaportField;
    private JYearChooser tahunChooser;
    private JButton btnSimpan, btnMaps;
    private int idUser;
    private boolean isUpdate = false;

    // Custom Color Palette for a more attractive UI
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245); // Light Gray Background
    private final Color PANEL_BACKGROUND = Color.WHITE; // White for content panels
    private final Color PRIMARY_ACCENT = new Color(52, 152, 219); // Bright Blue for accents/buttons
    private final Color TEXT_COLOR_DARK = new Color(44, 62, 80); // Dark Blue-Gray for text
    private final Color TEXT_COLOR_LIGHT = new Color(108, 122, 137); // Lighter Gray for labels
    private final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker Blue for button hover
    private final Color MAPS_BUTTON_COLOR = new Color(230, 126, 34); // Orange for Maps button
    private final Color MAPS_BUTTON_HOVER = new Color(208, 100, 7); // Darker Orange for Maps hover

    public PanelAsalSekolah(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout(0, 25)); // Add vertical gap between title and form
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(40, 50, 40, 50)); // Overall padding for the panel

        // Title Label
        JLabel lblTitle = new JLabel("Formulir Data Asal Sekolah");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(TEXT_COLOR_DARK);
        add(lblTitle, BorderLayout.NORTH);

        // Main content panel (holds the form)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(PANEL_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1), // Subtle border around the form panel
            new EmptyBorder(30, 40, 30, 40) // Internal padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand horizontally

        // Nama Sekolah
        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(contentPanel, "Nama Sekolah:", gbc);
        gbc.gridx = 1;
        namaSekolahField = createStyledTextField();
        contentPanel.add(namaSekolahField, gbc);

        // Alamat Sekolah
        gbc.gridx = 0; gbc.gridy++;
        addLabel(contentPanel, "Alamat Sekolah:", gbc);
        gbc.gridx = 1;
        alamatSekolahField = createStyledTextField();
        contentPanel.add(alamatSekolahField, gbc);

        // Maps Button (on a new row, centered or aligned left under address field)
        gbc.gridx = 1; gbc.gridy++;
        gbc.insets = new Insets(0, 15, 20, 15); // Less top padding, more bottom padding for button
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        btnMaps = createStyledButton("Lihat di Google Maps", MAPS_BUTTON_COLOR, MAPS_BUTTON_HOVER);
        contentPanel.add(btnMaps, gbc);

        // Tahun Lulus
        gbc.gridx = 0; gbc.gridy++;
        gbc.insets = new Insets(12, 15, 12, 15); // Reset insets
        addLabel(contentPanel, "Tahun Lulus:", gbc);
        gbc.gridx = 1;
        tahunChooser = createStyledYearChooser();
        contentPanel.add(tahunChooser, gbc);

        // Rata-rata Nilai Rapor
        gbc.gridx = 0; gbc.gridy++;
        addLabel(contentPanel, "Rata-rata Nilai Rapor:", gbc);
        gbc.gridx = 1;
        nilaiRaportField = createStyledTextField();
        contentPanel.add(nilaiRaportField, gbc);

        // Simpan/Update Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center horizontally
        gbc.insets = new Insets(30, 15, 0, 15); // Top padding for button
        btnSimpan = createStyledButton("Simpan Data", PRIMARY_ACCENT, BUTTON_HOVER_COLOR);
        contentPanel.add(btnSimpan, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Action Listeners
        btnSimpan.addActionListener(e -> simpanAtauUpdate());
        btnMaps.addActionListener(e -> bukaGoogleMaps());

        loadDataSekolah(); // Load existing data
    }

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(TEXT_COLOR_DARK); // Darker text for labels
        gbc.anchor = GridBagConstraints.WEST; // Align label to the left
        panel.add(label, gbc);
    }

    // Helper method for styled JTextField
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25); // Increased preferred width
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBackground(new Color(248, 249, 250)); // Very light gray for field background
        field.setForeground(TEXT_COLOR_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1), // Subtle gray border
            new EmptyBorder(10, 12, 10, 12) // Internal padding
        ));
        field.setCaretColor(TEXT_COLOR_DARK); // Set caret color
        return field;
    }

    // Helper method for styled JYearChooser
    private JYearChooser createStyledYearChooser() {
        JYearChooser yearChooser = new JYearChooser();
        // You might need to access internal components for full styling control
        // This is a basic styling for the JYearChooser itself
        yearChooser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        yearChooser.setBackground(new Color(248, 249, 250));
        yearChooser.setForeground(TEXT_COLOR_DARK);
        yearChooser.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(6, 8, 6, 8) // Internal padding
        ));
        // Attempt to style the internal components if possible (might require custom UI delegate)
        for (Component comp : yearChooser.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(PRIMARY_ACCENT.brighter());
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
            }
        }
        return yearChooser;
    }


    // Helper method for styled JButton
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30)); // Generous padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.setOpaque(true); // Ensure background is painted

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void bukaGoogleMaps() {
        String alamat = alamatSekolahField.getText().trim();
        if (!alamat.isEmpty()) {
            try {
                // Use a proper Google Maps URL
                Desktop.getDesktop().browse(new java.net.URI("https://www.google.com/maps/search/" + java.net.URLEncoder.encode(alamat, "UTF-8")));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membuka Google Maps: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan alamat terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadDataSekolah() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM tb_asalsekolah WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                namaSekolahField.setText(rs.getString("nama_sekolah"));
                alamatSekolahField.setText(rs.getString("alamat_sekolah"));
                tahunChooser.setYear(rs.getInt("tahun_lulus"));
                nilaiRaportField.setText(rs.getString("nilai_raport"));
                isUpdate = true;
                btnSimpan.setText("Update Data");
            } else {
                btnSimpan.setText("Simpan Data"); // Ensure button text is correct if no data exists
            }

            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanAtauUpdate() {
        // Input validation
        if (namaSekolahField.getText().isEmpty() || alamatSekolahField.getText().isEmpty() ||
            nilaiRaportField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate nilaiRaportField to be a valid number (decimal)
        try {
            Double.parseDouble(nilaiRaportField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nilai Rapor harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nama = namaSekolahField.getText();
            String alamat = alamatSekolahField.getText();
            int tahun = tahunChooser.getYear();
            String nilai = nilaiRaportField.getText(); // Keep as String to match DB type, or parse to Double for `setDouble`

            Connection conn = config.configDB();
            PreparedStatement stmt;

            if (isUpdate) {
                String sql = "UPDATE tb_asalsekolah SET nama_sekolah=?, alamat_sekolah=?, tahun_lulus=?, nilai_raport=? WHERE id_user=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nama);
                stmt.setString(2, alamat);
                stmt.setInt(3, tahun);
                stmt.setString(4, nilai); // Use setString if DB type is VARCHAR for nilai_raport
                stmt.setInt(5, idUser);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data asal sekolah berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String sql = "INSERT INTO tb_asalsekolah (id_user, nama_sekolah, alamat_sekolah, tahun_lulus, nilai_raport) VALUES (?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idUser);
                stmt.setString(2, nama);
                stmt.setString(3, alamat);
                stmt.setInt(4, tahun);
                stmt.setString(5, nilai); // Use setString if DB type is VARCHAR for nilai_raport
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data asal sekolah berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                isUpdate = true;
                btnSimpan.setText("Update Data");
            }

            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}