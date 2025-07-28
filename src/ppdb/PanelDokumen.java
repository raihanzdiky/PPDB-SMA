package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter; // For file type filtering

public class PanelDokumen extends JPanel {
    private JButton btnAkte, btnKK, btnRaport, btnIjazah, btnFoto, btnSimpan;
    private JButton previewAkte, previewKK, previewRaport, previewIjazah, previewFoto;
    private File fileAkte, fileKK, fileRaport, fileIjazah, fileFoto;
    private int idUser;
    private boolean isUpdate = false;

    // Custom Color Palette (consistent with other panels)
    private final Color PRIMARY_DARK = new Color(34, 49, 63);    // Dark Blue-Gray for main background
    private final Color ACCENT_BLUE = new Color(52, 152, 219);  // Bright Blue for accents/borders
    private final Color TEXT_LIGHT = new Color(236, 240, 241);  // Light Gray for text on dark background
    private final Color FIELD_BG_COLOR = Color.WHITE;           // White for input fields background
    private final Color FIELD_TEXT_COLOR = new Color(44, 62, 80); // Darker text for input fields
    private final Color BUTTON_HOVER_COLOR = new Color(41, 128, 185); // Darker blue for button hover
    private final Color SUBTLE_BORDER = new Color(189, 195, 199); // For field borders
    private final Color FORM_PANEL_BG = new Color(40, 58, 77); // Slightly lighter dark for inner panel
    private final Color FILE_BUTTON_BG = new Color(70, 96, 118); // A lighter dark blue for file buttons
    private final Color FILE_BUTTON_HOVER = new Color(85, 115, 140); // Hover for file buttons

    public PanelDokumen(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout(0, 30)); // Increased vertical gap
        setBackground(PRIMARY_DARK);
        setBorder(new EmptyBorder(40, 60, 40, 60)); // Generous padding for the main panel

        // Title Label
        JLabel lblTitle = new JLabel("Upload Dokumen Pendaftaran");
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
        gbc.insets = new Insets(8, 15, 8, 15); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Allow components to expand horizontally

        int row = 0;
        // Akte Kelahiran
        gbc.gridx = 0; gbc.gridy = row;
        addLabel(formPanel, "Akte Kelahiran (.pdf, .jpg, .png):", gbc);
        gbc.gridx = 1;
        btnAkte = createStyledFileButton("Pilih File"); formPanel.add(btnAkte, gbc);
        gbc.gridx = 2;
        previewAkte = createStyledPreviewButton("Preview"); formPanel.add(previewAkte, gbc);

        row++;
        // Kartu Keluarga
        gbc.gridx = 0; gbc.gridy = row;
        addLabel(formPanel, "Kartu Keluarga (.pdf, .jpg, .png):", gbc);
        gbc.gridx = 1;
        btnKK = createStyledFileButton("Pilih File"); formPanel.add(btnKK, gbc);
        gbc.gridx = 2;
        previewKK = createStyledPreviewButton("Preview"); formPanel.add(previewKK, gbc);

        row++;
        // Nilai Rapor
        gbc.gridx = 0; gbc.gridy = row;
        addLabel(formPanel, "Nilai Rapor (.pdf, .jpg, .png):", gbc);
        gbc.gridx = 1;
        btnRaport = createStyledFileButton("Pilih File"); formPanel.add(btnRaport, gbc);
        gbc.gridx = 2;
        previewRaport = createStyledPreviewButton("Preview"); formPanel.add(previewRaport, gbc);

        row++;
        // Ijazah
        gbc.gridx = 0; gbc.gridy = row;
        addLabel(formPanel, "Ijazah (.pdf, .jpg, .png):", gbc);
        gbc.gridx = 1;
        btnIjazah = createStyledFileButton("Pilih File"); formPanel.add(btnIjazah, gbc);
        gbc.gridx = 2;
        previewIjazah = createStyledPreviewButton("Preview"); formPanel.add(previewIjazah, gbc);

        row++;
        // Foto Latar Merah
        gbc.gridx = 0; gbc.gridy = row;
        addLabel(formPanel, "Foto Latar Merah (.jpg, .png):", gbc);
        gbc.gridx = 1;
        btnFoto = createStyledFileButton("Pilih File"); formPanel.add(btnFoto, gbc);
        gbc.gridx = 2;
        previewFoto = createStyledPreviewButton("Preview"); formPanel.add(previewFoto, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; // Span all columns for the save button
        gbc.insets = new Insets(30, 15, 10, 15); // Increased top padding for the button
        btnSimpan = createStyledButton("Simpan Dokumen");
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        btnAkte.addActionListener(e -> fileAkte = pilihFile(btnAkte, "Akte Kelahiran", new String[]{"pdf", "jpg", "png"}));
        btnKK.addActionListener(e -> fileKK = pilihFile(btnKK, "Kartu Keluarga", new String[]{"pdf", "jpg", "png"}));
        btnRaport.addActionListener(e -> fileRaport = pilihFile(btnRaport, "Nilai Raport", new String[]{"pdf", "jpg", "png"}));
        btnIjazah.addActionListener(e -> fileIjazah = pilihFile(btnIjazah, "Ijazah", new String[]{"pdf", "jpg", "png"}));
        btnFoto.addActionListener(e -> fileFoto = pilihFile(btnFoto, "Foto Latar Merah", new String[]{"jpg", "png"}));

        previewAkte.addActionListener(e -> tampilkanPreview(fileAkte));
        previewKK.addActionListener(e -> tampilkanPreview(fileKK));
        previewRaport.addActionListener(e -> tampilkanPreview(fileRaport));
        previewIjazah.addActionListener(e -> tampilkanPreview(fileIjazah));
        previewFoto.addActionListener(e -> tampilkanPreview(fileFoto));

        btnSimpan.addActionListener(e -> simpanAtauUpdate());

        loadDokumen();
        updatePreviewButtonStates(); // Set initial state of preview buttons
    }

    // --- Helper Methods for Styled Components ---

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(TEXT_LIGHT);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
    }

    private JButton createStyledFileButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_LIGHT);
        button.setBackground(FILE_BUTTON_BG); // Darker blue for file selection
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(FILE_BUTTON_BG.darker(), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(FILE_BUTTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(FILE_BUTTON_BG);
            }
        });
        return button;
    }

    private JButton createStyledPreviewButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_BLUE.darker()); // A slightly darker accent blue
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_BLUE.darker().darker(), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setEnabled(false); // Initially disabled until a file is loaded/selected

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_BLUE);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_BLUE.darker());
                }
            }
        });
        return button;
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

    // --- File Handling Logic ---

    private File pilihFile(JButton button, String description, String[] extensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih " + description);
        chooser.setFileFilter(new FileNameExtensionFilter(description + " Files (" + String.join(", ", extensions) + ")", extensions));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            button.setText(selected.getName());
            // Enable the corresponding preview button
            if (button == btnAkte) previewAkte.setEnabled(true);
            else if (button == btnKK) previewKK.setEnabled(true);
            else if (button == btnRaport) previewRaport.setEnabled(true);
            else if (button == btnIjazah) previewIjazah.setEnabled(true);
            else if (button == btnFoto) previewFoto.setEnabled(true);
            return selected;
        }
        return null;
    }

    private void tampilkanPreview(File file) {
        if (file == null || !file.exists()) {
            JOptionPane.showMessageDialog(this, "File tidak ditemukan atau belum dipilih.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, "Fitur preview tidak didukung di sistem ini.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Desktop.getDesktop().open(file); // Membuka file dengan aplikasi default
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuka file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Database Interaction Methods ---

    private void loadDokumen() {
        try (Connection conn = config.configDB()) {
            String sql = "SELECT * FROM tb_dokumen WHERE id_user=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fileAkte = getFileFromPath(rs.getString("akte_kelahiran"));
                fileKK = getFileFromPath(rs.getString("kartu_keluarga"));
                fileRaport = getFileFromPath(rs.getString("nilai_raport"));
                fileIjazah = getFileFromPath(rs.getString("ijazah"));
                fileFoto = getFileFromPath(rs.getString("foto_latarmerah"));

                updateButtonText(btnAkte, fileAkte);
                updateButtonText(btnKK, fileKK);
                updateButtonText(btnRaport, fileRaport);
                updateButtonText(btnIjazah, fileIjazah);
                updateButtonText(btnFoto, fileFoto);
                
                isUpdate = true;
                btnSimpan.setText("Update Dokumen");
            } else {
                btnSimpan.setText("Simpan Dokumen");
            }

            rs.close(); ps.close();
            updatePreviewButtonStates(); // Set preview button states based on loaded files
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat dokumen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateButtonText(JButton button, File file) {
        if (file != null && file.exists()) {
            button.setText(file.getName());
        } else {
            button.setText("Pilih File");
        }
    }

    private void updatePreviewButtonStates() {
        previewAkte.setEnabled(fileAkte != null && fileAkte.exists());
        previewKK.setEnabled(fileKK != null && fileKK.exists());
        previewRaport.setEnabled(fileRaport != null && fileRaport.exists());
        previewIjazah.setEnabled(fileIjazah != null && fileIjazah.exists());
        previewFoto.setEnabled(fileFoto != null && fileFoto.exists());
    }

    private File getFileFromPath(String path) {
        if (path == null || path.isEmpty()) return null;
        File file = new File(path);
        return file.exists() ? file : null; // Only return file if it actually exists
    }

    private void simpanAtauUpdate() {
        if (fileAkte == null || fileKK == null || fileRaport == null || fileIjazah == null || fileFoto == null) {
            JOptionPane.showMessageDialog(this, "Lengkapi semua file dokumen terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = config.configDB()) {
            String sql;
            PreparedStatement stmt;

            if (isUpdate) {
                sql = "UPDATE tb_dokumen SET akte_kelahiran=?, kartu_keluarga=?, nilai_raport=?, ijazah=?, foto_latarmerah=? WHERE id_user=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fileAkte.getAbsolutePath());
                stmt.setString(2, fileKK.getAbsolutePath());
                stmt.setString(3, fileRaport.getAbsolutePath());
                stmt.setString(4, fileIjazah.getAbsolutePath());
                stmt.setString(5, fileFoto.getAbsolutePath());
                stmt.setInt(6, idUser);
            } else {
                sql = "INSERT INTO tb_dokumen (id_user, akte_kelahiran, kartu_keluarga, nilai_raport, ijazah, foto_latarmerah, status_verifikasi) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idUser);
                stmt.setString(2, fileAkte.getAbsolutePath());
                stmt.setString(3, fileKK.getAbsolutePath());
                stmt.setString(4, fileRaport.getAbsolutePath());
                stmt.setString(5, fileIjazah.getAbsolutePath());
                stmt.setString(6, fileFoto.getAbsolutePath());
                stmt.setString(7, "BELUM DIVERIFIKASI"); // Default status upon first upload
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Dokumen berhasil " + (isUpdate ? "diperbarui!" : "disimpan!"), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            isUpdate = true;
            btnSimpan.setText("Update Dokumen");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan dokumen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}