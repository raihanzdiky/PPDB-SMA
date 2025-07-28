package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormAturKuota extends JFrame {
    private JComboBox<String> cmbJurusan, cmbJalur;
    private JTextField txtKuota;
    private JButton btnSimpan, btnKembali;

    // Konfigurasi koneksi database
    private final String DB_URL = "jdbc:mysql://localhost:3306/ppdbsaya";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    public FormAturKuota() {
        setTitle("Atur Kuota Pendaftaran");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Jurusan:"));
        cmbJurusan = new JComboBox<>(new String[]{"IPA", "IPS"});
        add(cmbJurusan);

        add(new JLabel("Jalur Pendaftaran:"));
        cmbJalur = new JComboBox<>(new String[]{"zonasi", "prestasi", "transkripsi nilai"});
        add(cmbJalur);

        add(new JLabel("Kuota:"));
        txtKuota = new JTextField();
        add(txtKuota);

        btnSimpan = new JButton("Simpan");
        btnKembali = new JButton("Kembali");
        add(btnSimpan);
        add(btnKembali);

        cmbJurusan.addActionListener(e -> cekDataAda());
        cmbJalur.addActionListener(e -> cekDataAda());

        btnSimpan.addActionListener(e -> simpanAtauUpdate());
        btnKembali.addActionListener(e -> dispose());

        cekDataAda();
        setVisible(true);
    }

    private void cekDataAda() {
        String jurusan = (String) cmbJurusan.getSelectedItem();
        String jalur = (String) cmbJalur.getSelectedItem();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pst = conn.prepareStatement("SELECT kuota FROM tb_kuota WHERE jurusan=? AND jalur_pendaftaran=?")) {

            pst.setString(1, jurusan);
            pst.setString(2, jalur);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtKuota.setText(String.valueOf(rs.getInt("kuota")));
                btnSimpan.setText("Update");
            } else {
                txtKuota.setText("");
                btnSimpan.setText("Simpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memeriksa kuota!\n" + e.getMessage());
        }
    }

    private void simpanAtauUpdate() {
        String jurusan = (String) cmbJurusan.getSelectedItem();
        String jalur = (String) cmbJalur.getSelectedItem();
        int kuota;

        try {
            kuota = Integer.parseInt(txtKuota.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kuota harus berupa angka!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement pst;
            if (btnSimpan.getText().equals("Update")) {
                pst = conn.prepareStatement("UPDATE tb_kuota SET kuota=? WHERE jurusan=? AND jalur_pendaftaran=?");
                pst.setInt(1, kuota);
                pst.setString(2, jurusan);
                pst.setString(3, jalur);
            } else {
                pst = conn.prepareStatement("INSERT INTO tb_kuota (jurusan, jalur_pendaftaran, kuota) VALUES (?, ?, ?)");
                pst.setString(1, jurusan);
                pst.setString(2, jalur);
                pst.setInt(3, kuota);
            }
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kuota berhasil disimpan.");
            cekDataAda();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan kuota!\n" + e.getMessage());
        }
    }
} 
