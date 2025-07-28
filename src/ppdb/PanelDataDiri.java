package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class PanelDataDiri extends JPanel {
    private JTextField namaField, nisnField, tempatLahirField, alamatField, noTelpField, emailField;
    private JDateChooser tanggalLahirChooser;
    private JComboBox<String> genderCombo;
    private JButton btnSimpan;
    private int idUser;
    private boolean isDataExist = false;
    private int idCalon = -1;

    // Custom Color Palette (as provided and slightly adjusted for harmony)
    private final Color PRIMARY_DARK = new Color(34, 49, 63);    // Dark Blue-Gray for main background
    private final Color ACCENT_BLUE = new Color(52, 152, 219);  // Bright Blue for accents/borders
    private final Color TEXT_LIGHT = new Color(236, 240, 241);  // Light Gray for text on dark background
    private final Color FIELD_BG_COLOR = Color.WHITE;           // White for input fields background
    private final Color FIELD_TEXT_COLOR = new Color(44, 62, 80); // Darker text for input fields
    private final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker blue for button hover
    private final Color SUBTLE_BORDER = new Color(189, 195, 199); // For field borders

    public PanelDataDiri(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout(0, 30)); // Increased vertical gap for more breathing room
        setBackground(PRIMARY_DARK);
        setBorder(new EmptyBorder(40, 60, 40, 60)); // Generous padding for the main panel

        // Title Label
        JLabel lblTitle = new JLabel("Formulir Data Diri Siswa");
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 36)); // Larger font
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(TEXT_LIGHT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // More bottom padding
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel (Central content area)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(40, 58, 77)); // Slightly lighter dark background for contrast with main panel
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_DARK.brighter(), 1), // A subtle border around the form area
            new EmptyBorder(30, 40, 30, 40) // Internal padding for the formPanel
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand horizontally

        // Row 0: Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(formPanel, "Nama Lengkap:", gbc);
        gbc.gridx = 1;
        namaField = createStyledTextField();
        formPanel.add(namaField, gbc);

        // Row 1: NISN
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "NISN:", gbc);
        gbc.gridx = 1;
        nisnField = createStyledTextField();
        formPanel.add(nisnField, gbc);

        // Row 2: Tempat Lahir
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Tempat Lahir:", gbc);
        gbc.gridx = 1;
        tempatLahirField = createStyledTextField();
        formPanel.add(tempatLahirField, gbc);

        // Row 3: Tanggal Lahir
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Tanggal Lahir:", gbc);
        gbc.gridx = 1;
        tanggalLahirChooser = createStyledDateChooser();
        formPanel.add(tanggalLahirChooser, gbc);

        // Row 4: Jenis Kelamin
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Jenis Kelamin:", gbc);
        gbc.gridx = 1;
        genderCombo = createStyledComboBox(new String[]{"Laki-laki", "Perempuan"});
        formPanel.add(genderCombo, gbc);

        // Row 5: Alamat
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Alamat:", gbc);
        gbc.gridx = 1;
        alamatField = createStyledTextField();
        formPanel.add(alamatField, gbc);

        // Row 6: No. Telepon
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "No. Telepon:", gbc);
        gbc.gridx = 1;
        noTelpField = createStyledTextField();
        formPanel.add(noTelpField, gbc);

        // Row 7: Email
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Email:", gbc);
        gbc.gridx = 1;
        emailField = createStyledTextField();
        formPanel.add(emailField, gbc);

        // Row 8: Simpan/Update Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center horizontally
        gbc.insets = new Insets(35, 15, 10, 15); // Increased top padding for the button

        btnSimpan = createStyledButton("Simpan Data");
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Event Listener for Save/Update Button
        btnSimpan.addActionListener(e -> {
            if (isDataExist) updateDataDiri();
            else simpanDataDiri();
        });

        loadData();
    }

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Slightly larger font for labels
        label.setForeground(TEXT_LIGHT);
        gbc.anchor = GridBagConstraints.WEST; // Align label to the left
        panel.add(label, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Consistent font size
        field.setBackground(FIELD_BG_COLOR);
        field.setForeground(FIELD_TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUBTLE_BORDER, 1, true),  // Rounded soft-gray border
            new EmptyBorder(10, 12, 10, 12) // Internal padding
        ));
        field.setCaretColor(FIELD_TEXT_COLOR);

        // Hover & focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT_BLUE, 2, true), // Thicker accent blue border on focus
                    new EmptyBorder(10, 12, 10, 12)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(SUBTLE_BORDER, 1, true), // Back to subtle border on lost focus
                    new EmptyBorder(10, 12, 10, 12)
                ));
            }
        });

        return field;
    }

    private JDateChooser createStyledDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd MMMM yyyy");
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Consistent font size
        dateChooser.setBackground(FIELD_BG_COLOR);
        dateChooser.setForeground(FIELD_TEXT_COLOR);
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUBTLE_BORDER, 1, true), // Consistent with text fields
            new EmptyBorder(8, 10, 8, 10) // Internal padding
        ));

        // Attempt to style the internal text field (JTextFieldDateEditor)
        if (dateChooser.getDateEditor() instanceof com.toedter.calendar.JTextFieldDateEditor) {
            com.toedter.calendar.JTextFieldDateEditor dateEditorField =
                (com.toedter.calendar.JTextFieldDateEditor) dateChooser.getDateEditor();

            dateEditorField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            dateEditorField.setBackground(FIELD_BG_COLOR);
            dateEditorField.setForeground(FIELD_TEXT_COLOR);
            dateEditorField.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove default border
            dateEditorField.setCaretColor(FIELD_TEXT_COLOR);
        }

        // Style the calendar button within JDateChooser
        for (Component comp : dateChooser.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(ACCENT_BLUE);
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8)); // Padding for button icon
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(BUTTON_HOVER_COLOR);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(ACCENT_BLUE);
                    }
                });
            }
        }
        return dateChooser;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Consistent font size
        comboBox.setBackground(FIELD_BG_COLOR);
        comboBox.setForeground(FIELD_TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUBTLE_BORDER, 1, true), // Consistent with text fields
            new EmptyBorder(8, 10, 8, 10) // Internal padding
        ));
        ((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT); // Align text left

        // Custom UI for JComboBox to handle dropdown button and hover
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                
                button.setBackground(FIELD_BG_COLOR);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBackground(new Color(240, 240, 240)); // Lighter hover
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBackground(FIELD_BG_COLOR);
                    }
                });
                return button;
            }

            private Icon createArrowIcon(Color color) {
                return new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(color);
                        int w = getIconWidth();
                        int h = getIconHeight();
                        int[] xPoints = {x, x + w / 2, x + w};
                        int[] yPoints = {y + h, y, y + h};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }

                    @Override
                    public int getIconWidth() { return 10; }
                    @Override
                    public int getIconHeight() { return 6; }
                };
            }
        });

        return comboBox;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Use the ACCENT_BLUE for the primary gradient color
                GradientPaint gp = new GradientPaint(
                    0, 0, ACCENT_BLUE.brighter(),
                    0, getHeight(), ACCENT_BLUE.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 17)); // Slightly larger, bolder font
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // Crucial for custom painting
        button.setBorderPainted(false);
        button.setOpaque(false); // Make sure it's not opaque so the custom painting shows
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(14, 35, 14, 35)); // More padding

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 255, 255, 240)); // Slightly more opaque white
                button.setBackground(BUTTON_HOVER_COLOR); // Change background on hover if contentAreaFilled was true
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBackground(ACCENT_BLUE); // Revert background
            }
        });

        return button;
    }

    private void loadData() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM tb_datacalonsiswa WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                isDataExist = true;
                idCalon = rs.getInt("id_calon");

                namaField.setText(rs.getString("nama_lengkap"));
                nisnField.setText(rs.getString("nisn"));
                tempatLahirField.setText(rs.getString("tempat_lahir"));

                String jenisKelamin = rs.getString("jenis_kelamin");
                if ("L".equalsIgnoreCase(jenisKelamin)) {
                    genderCombo.setSelectedItem("Laki-laki");
                } else if ("P".equalsIgnoreCase(jenisKelamin)) {
                    genderCombo.setSelectedItem("Perempuan");
                } else {
                    genderCombo.setSelectedIndex(-1);
                }

                alamatField.setText(rs.getString("alamat"));
                noTelpField.setText(rs.getString("no_tlp"));
                emailField.setText(rs.getString("email"));

                Date tanggalLahirSql = rs.getDate("tanggal_lahir");
                if (tanggalLahirSql != null) {
                    tanggalLahirChooser.setDate(new java.util.Date(tanggalLahirSql.getTime()));
                } else {
                    tanggalLahirChooser.setDate(null);
                }

                btnSimpan.setText("Update Data");
            } else {
                btnSimpan.setText("Simpan Data");
            }

            rs.close(); stmt.close(); conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanDataDiri() {
        if (namaField.getText().isEmpty() || nisnField.getText().isEmpty() ||
            tempatLahirField.getText().isEmpty() || tanggalLahirChooser.getDate() == null ||
            genderCombo.getSelectedItem() == null ||
            alamatField.getText().isEmpty() || noTelpField.getText().isEmpty() ||
            emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nama = namaField.getText();
            String nisn = nisnField.getText();
            String tempatLahir = tempatLahirField.getText();
            java.util.Date tanggalLahirDate = tanggalLahirChooser.getDate();
            String gender = (String) genderCombo.getSelectedItem();
            String genderCode = "";
            if ("Laki-laki".equals(gender)) {
                genderCode = "L";
            } else if ("Perempuan".equals(gender)) {
                genderCode = "P";
            }

            String alamat = alamatField.getText();
            String noTelp = noTelpField.getText();
            String email = emailField.getText();

            String tanggalLahir = new SimpleDateFormat("yyyy-MM-dd").format(tanggalLahirDate);

            Connection conn = config.configDB();
            String sql = "INSERT INTO tb_datacalonsiswa (id_user, nama_lengkap, nisn, tempat_lahir, tanggal_lahir, jenis_kelamin, alamat, no_tlp, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, idUser);
            stmt.setString(2, nama);
            stmt.setString(3, nisn);
            stmt.setString(4, tempatLahir);
            stmt.setString(5, tanggalLahir);
            stmt.setString(6, genderCode);
            stmt.setString(7, alamat);
            stmt.setString(8, noTelp);
            stmt.setString(9, email);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idCalon = generatedKeys.getInt(1);
                    }
                }
                JOptionPane.showMessageDialog(this, "Data diri berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                isDataExist = true;
                btnSimpan.setText("Update Data");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data diri.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            stmt.close(); conn.close();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "NISN sudah terdaftar. Mohon gunakan NISN lain.", "Error Duplikasi NISN", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDataDiri() {
        if (namaField.getText().isEmpty() || nisnField.getText().isEmpty() ||
            tempatLahirField.getText().isEmpty() || tanggalLahirChooser.getDate() == null ||
            genderCombo.getSelectedItem() == null ||
            alamatField.getText().isEmpty() || noTelpField.getText().isEmpty() ||
            emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nama = namaField.getText();
            String nisn = nisnField.getText();
            String tempatLahir = tempatLahirField.getText();
            java.util.Date tanggalLahirDate = tanggalLahirChooser.getDate();
            String gender = (String) genderCombo.getSelectedItem();
            String genderCode = "";
            if ("Laki-laki".equals(gender)) {
                genderCode = "L";
            } else if ("Perempuan".equals(gender)) {
                genderCode = "P";
            }
            String alamat = alamatField.getText();
            String noTelp = noTelpField.getText();
            String email = emailField.getText();

            String tanggalLahir = new SimpleDateFormat("yyyy-MM-dd").format(tanggalLahirDate);

            Connection conn = config.configDB();
            String sql = "UPDATE tb_datacalonsiswa SET nama_lengkap=?, nisn=?, tempat_lahir=?, tanggal_lahir=?, jenis_kelamin=?, alamat=?, no_tlp=?, email=? WHERE id_calon=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setString(2, nisn);
            stmt.setString(3, tempatLahir);
            stmt.setString(4, tanggalLahir);
            stmt.setString(5, genderCode);
            stmt.setString(6, alamat);
            stmt.setString(7, noTelp);
            stmt.setString(8, email);
            stmt.setInt(9, idCalon);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data diri berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            stmt.close(); conn.close();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "NISN sudah terdaftar untuk siswa lain. Mohon periksa kembali.", "Error Duplikasi NISN", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}