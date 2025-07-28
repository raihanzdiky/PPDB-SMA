package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PanelTranskrip extends JPanel {
    private JTextField[] nilaiFields;
    private JButton btnSimpan;
    private int idUser;
    private boolean isUpdate = false;

    public PanelTranskrip(int idUser) {
        this.idUser = idUser;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 33, 61)); // background utama

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(20, 33, 61));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Rata-rata Nilai Raport 5 Semester");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        nilaiFields = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel lbl = new JLabel("Semester " + (i + 1) + ":");
            lbl.setForeground(Color.WHITE);
            formPanel.add(lbl, gbc);

            gbc.gridx = 1;
            nilaiFields[i] = new JTextField(10);
            formPanel.add(nilaiFields[i], gbc);
        }

        gbc.gridx = 1;
        gbc.gridy++;
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(Color.WHITE);
        btnSimpan.setForeground(new Color(0, 137, 123));
        btnSimpan.setFocusPainted(false);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(btnSimpan, gbc);

        add(formPanel, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanAtauUpdate());
        loadData();
    }

    private void loadData() {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT semester1, semester2, semester3, semester4, semester5 FROM tb_transkripsi_nilai WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < 5; i++) {
                    nilaiFields[i].setText(String.valueOf(rs.getDouble("semester" + (i + 1))));
                }
                isUpdate = true;
                btnSimpan.setText("Update");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simpanAtauUpdate() {
        try {
            double[] nilai = new double[5];
            for (int i = 0; i < 5; i++) {
                nilai[i] = Double.parseDouble(nilaiFields[i].getText());
            }

            Connection conn = config.configDB();
            String sql;

            if (isUpdate) {
                sql = "UPDATE tb_transkripsi_nilai SET semester1 = ?, semester2 = ?, semester3 = ?, semester4 = ?, semester5 = ? WHERE id_user = ?";
            } else {
                sql = "INSERT INTO tb_transkripsi_nilai (id_user, semester1, semester2, semester3, semester4, semester5) VALUES (?, ?, ?, ?, ?, ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            if (isUpdate) {
                for (int i = 0; i < 5; i++) {
                    stmt.setDouble(i + 1, nilai[i]);
                }
                stmt.setInt(6, idUser);
            } else {
                stmt.setInt(1, idUser);
                for (int i = 0; i < 5; i++) {
                    stmt.setDouble(i + 2, nilai[i]);
                }
            }

            stmt.executeUpdate();
            stmt.close();

            // Ambil nilai rata-rata yang dihasilkan MySQL
            String rataSql = "SELECT rata_rata FROM tb_transkripsi_nilai WHERE id_user = ?";
            PreparedStatement rataStmt = conn.prepareStatement(rataSql);
            rataStmt.setInt(1, idUser);
            ResultSet rs = rataStmt.executeQuery();
            double rataRata = 0;

            if (rs.next()) {
                rataRata = rs.getDouble("rata_rata");
            }

            rs.close();
            rataStmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Data berhasil " + (isUpdate ? "diperbarui" : "disimpan") + 
                "\nRata-rata dihitung: " + rataRata);
            isUpdate = true;
            btnSimpan.setText("Update");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harap masukkan semua nilai dengan format angka yang benar.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan nilai: " + ex.getMessage());
        }
    }
}
