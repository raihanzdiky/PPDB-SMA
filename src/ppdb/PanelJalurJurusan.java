package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI; // For custom JComboBox arrow

public class PanelJalurJurusan extends JPanel {
    private JComboBox<String> jalurCombo, jurusanCombo;
    private JButton btnSimpan;
    private int idUser;
    private boolean isUpdate = false;

    // Custom Color Palette (consistent with PanelDataDiri and PanelOrtu)
    private final Color PRIMARY_DARK = new Color(34, 49, 63);    // Dark Blue-Gray for main background
    private final Color ACCENT_BLUE = new Color(52, 152, 219);  // Bright Blue for accents/borders
    private final Color TEXT_LIGHT = new Color(236, 240, 241);  // Light Gray for text on dark background
    private final Color FIELD_BG_COLOR = Color.WHITE;           // White for input fields background
    private final Color FIELD_TEXT_COLOR = new Color(44, 62, 80); // Darker text for input fields
    private final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker blue for button hover
    private final Color SUBTLE_BORDER = new Color(189, 195, 199); // For field borders
    private final Color FORM_PANEL_BG = new Color(40, 58, 77); // Slightly lighter dark for inner panel

    public PanelJalurJurusan(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout(0, 30)); // Increased vertical gap
        setBackground(PRIMARY_DARK);
        setBorder(new EmptyBorder(40, 60, 40, 60)); // Generous padding for the main panel

        // Title Label
        JLabel lblTitle = new JLabel("Pilih Jalur dan Jurusan");
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 36));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(TEXT_LIGHT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel (Central content area)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(FORM_PANEL_BG); // Slightly lighter dark background
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_DARK.brighter(), 1), // Subtle border
            new EmptyBorder(30, 40, 30, 40) // Internal padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand horizontally

        // Row 0: Jalur Pendaftaran
        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(formPanel, "Jalur Pendaftaran:", gbc);
        gbc.gridx = 1;
        jalurCombo = createStyledComboBox(new String[]{"transkripsi nilai", "prestasi", "zonasi"});
        formPanel.add(jalurCombo, gbc);

        // Row 1: Jurusan Pilihan
        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Jurusan Pilihan:", gbc);
        gbc.gridx = 1;
        jurusanCombo = createStyledComboBox(new String[]{"ipa", "ips"});
        formPanel.add(jurusanCombo, gbc);

        // Row 2: Simpan/Update Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center horizontally
        gbc.insets = new Insets(35, 15, 10, 15); // Increased top padding for the button

        btnSimpan = createStyledButton("Simpan Data");
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanAtauUpdate());

        loadData();
    }

    // --- Helper Methods for Styled Components (Reused from PanelOrtu) ---

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(TEXT_LIGHT);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setBackground(FIELD_BG_COLOR);
        comboBox.setForeground(FIELD_TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUBTLE_BORDER, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        ((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

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
                        button.setBackground(new Color(240, 240, 240));
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

                GradientPaint gp = new GradientPaint(
                    0, 0, ACCENT_BLUE.brighter(),
                    0, getHeight(), ACCENT_BLUE.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(14, 35, 14, 35));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 255, 255, 240));
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.repaint();
            }
        });

        return button;
    }

    // --- Database Interaction Methods (unchanged functionality) ---

    private void loadData() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM tb_jalur_jurusan WHERE id_user=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jalurCombo.setSelectedItem(rs.getString("jalur_pendaftaran"));
                jurusanCombo.setSelectedItem(rs.getString("jurusan"));
                isUpdate = true;
                btnSimpan.setText("Update Data"); // Change button text
            } else {
                btnSimpan.setText("Simpan Data"); // Ensure initial text is "Simpan Data"
            }

            rs.close(); ps.close(); conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanAtauUpdate() {
        if (jalurCombo.getSelectedItem() == null || jurusanCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String jalur = getSelectedJalur();
            String jurusan = getSelectedJurusan();

            Connection conn = config.configDB();
            String sql;
            PreparedStatement stmt = null;

            if (isUpdate) {
                sql = "UPDATE tb_jalur_jurusan SET jalur_pendaftaran=?, jurusan=? WHERE id_user=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, jalur);
                stmt.setString(2, jurusan);
                stmt.setInt(3, idUser);
            } else {
                sql = "INSERT INTO tb_jalur_jurusan (id_user, jalur_pendaftaran, jurusan) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idUser);
                stmt.setString(2, jalur);
                stmt.setString(3, jurusan);
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data jalur & jurusan berhasil " + (isUpdate ? "diperbarui!" : "disimpan!"), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            isUpdate = true;
            btnSimpan.setText("Update Data"); // Change button text after successful insert

            if (stmt != null) stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getSelectedJalur() {
        return jalurCombo.getSelectedItem().toString();
    }

    public String getSelectedJurusan() {
        return jurusanCombo.getSelectedItem().toString();
    }
}