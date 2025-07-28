package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.sql.*;

public class PanelZonasi extends JPanel {
    private JTextField jarakField, alamatField, waktuTempuhField;
    private JButton btnSimpan, btnMaps;
    private int idUser;
    private boolean isUpdate = false;

    public PanelZonasi(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 33, 61)); // oranye gelap

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(20, 33, 61));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Data Zonasi (Jarak ke Sekolah)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblAlamat = new JLabel("Alamat Rumah:");
        lblAlamat.setForeground(Color.WHITE);
        formPanel.add(lblAlamat, gbc);

        gbc.gridx = 1;
        alamatField = new JTextField(20);
        formPanel.add(alamatField, gbc);

        gbc.gridx = 1; gbc.gridy++;
        btnMaps = new JButton("Lihat di Google Maps");
        btnMaps.setBackground(Color.WHITE);
        btnMaps.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(btnMaps, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblJarak = new JLabel("Jarak dari Rumah ke Sekolah (km):");
        lblJarak.setForeground(Color.WHITE);
        formPanel.add(lblJarak, gbc);
        gbc.gridx = 1;
        jarakField = new JTextField(20);
        formPanel.add(jarakField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblWaktu = new JLabel("Waktu Tempuh (menit):");
        lblWaktu.setForeground(Color.WHITE);
        formPanel.add(lblWaktu, gbc);
        gbc.gridx = 1;
        waktuTempuhField = new JTextField(20);
        formPanel.add(waktuTempuhField, gbc);

        gbc.gridx = 1; gbc.gridy++;
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(Color.WHITE);
        btnSimpan.setForeground(new Color(255, 143, 0));
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanAtauUpdate());
        btnMaps.addActionListener(e -> bukaGoogleMaps());

        loadData(); // Load data jika ada
    }

    private void bukaGoogleMaps() {
        String alamat = alamatField.getText().trim();
        if (!alamat.isEmpty()) {
            try {
                String url = "https://www.google.com/maps/search/" + java.net.URLEncoder.encode(alamat, "UTF-8");
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal membuka Google Maps: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan alamat terlebih dahulu!");
        }
    }

    private void loadData() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM tb_zonasi WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alamatField.setText(rs.getString("alamat_rumah"));
                jarakField.setText(String.valueOf(rs.getDouble("jarak_km")));
                waktuTempuhField.setText(String.valueOf(rs.getInt("waktu_tempuh")));
                isUpdate = true;
                btnSimpan.setText("Update");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void simpanAtauUpdate() {
        try {
            String alamat = alamatField.getText();
            double jarak = Double.parseDouble(jarakField.getText());
            String waktu = waktuTempuhField.getText();

            Connection conn = config.configDB();
            String sql;

            if (isUpdate) {
                sql = "UPDATE tb_zonasi SET alamat_rumah=?, jarak_km=?, waktu_tempuh=? WHERE id_user=?";
            } else {
                sql = "INSERT INTO tb_zonasi (alamat_rumah, jarak_km, waktu_tempuh, id_user) VALUES (?, ?, ?, ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, alamat);
            stmt.setDouble(2, jarak);
            stmt.setString(3, waktu);
            stmt.setInt(4, idUser);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data zonasi berhasil " + (isUpdate ? "diperbarui." : "disimpan."));
            isUpdate = true;
            btnSimpan.setText("Update");

            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data zonasi: " + ex.getMessage());
        }
    }
}
