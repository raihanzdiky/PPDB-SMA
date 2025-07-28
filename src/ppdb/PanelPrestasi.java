package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;

public class PanelPrestasi extends JPanel {
    private JTextField namaPrestasiField, juaraField;
    private JComboBox<String> tingkatCombo;
    private JSpinner tahunSpinner;
    private JButton btnSimpan;
    private int idUser;
    private boolean isUpdate = false;

    public PanelPrestasi(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 33, 61)); // Coklat kemerahan

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(20, 33, 61));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Data Prestasi Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        gbc.gridx = 0; gbc.gridy = 0;
        addLabel(formPanel, "Nama Prestasi:", gbc);
        gbc.gridx = 1;
        namaPrestasiField = new JTextField(20);
        formPanel.add(namaPrestasiField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Juara ke-:", gbc);
        gbc.gridx = 1;
        juaraField = new JTextField(20);
        formPanel.add(juaraField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Tingkat Prestasi:", gbc);
        gbc.gridx = 1;
        tingkatCombo = new JComboBox<>(new String[]{"Sekolah", "Kabupaten", "Provinsi", "Nasional", "Internasional"});
        formPanel.add(tingkatCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        addLabel(formPanel, "Tahun Prestasi:", gbc);
        gbc.gridx = 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        tahunSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2000, currentYear, 1));
        formPanel.add(tahunSpinner, gbc);

        gbc.gridx = 1; gbc.gridy++;
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(Color.WHITE);
        btnSimpan.setForeground(new Color(121, 85, 72));
        btnSimpan.setFocusPainted(false);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanAtauUpdate());

        loadData();
    }

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);
    }

    private void loadData() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM tb_prestasi WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                namaPrestasiField.setText(rs.getString("nama_prestasi"));
                juaraField.setText(String.valueOf(rs.getInt("juara_ke")));
                tingkatCombo.setSelectedItem(rs.getString("tingkat"));
                tahunSpinner.setValue(rs.getInt("tahun"));

                isUpdate = true;
                btnSimpan.setText("Update");
            }

            rs.close(); stmt.close(); conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void simpanAtauUpdate() {
        try {
            String nama = namaPrestasiField.getText();
            int juara = Integer.parseInt(juaraField.getText());
            String tingkat = tingkatCombo.getSelectedItem().toString();
            int tahun = (int) tahunSpinner.getValue();

            Connection conn = config.configDB();
            String sql;

            if (isUpdate) {
                sql = "UPDATE tb_prestasi SET nama_prestasi=?, tingkat=?, juara_ke=?, tahun=? WHERE id_user=?";
            } else {
                sql = "INSERT INTO tb_prestasi (id_user, nama_prestasi, tingkat, juara_ke, tahun) VALUES (?, ?, ?, ?, ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            if (isUpdate) {
                stmt.setString(1, nama);
                stmt.setString(2, tingkat);
                stmt.setInt(3, juara);
                stmt.setInt(4, tahun);
                stmt.setInt(5, idUser);
            } else {
                stmt.setInt(1, idUser);
                stmt.setString(2, nama);
                stmt.setString(3, tingkat);
                stmt.setInt(4, juara);
                stmt.setInt(5, tahun);
            }

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data prestasi berhasil " + (isUpdate ? "diperbarui" : "disimpan."));
            isUpdate = true;
            btnSimpan.setText("Update");

            stmt.close(); conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data prestasi: " + ex.getMessage());
        }
    }
}
