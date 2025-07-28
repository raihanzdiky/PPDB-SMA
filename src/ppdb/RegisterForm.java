package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JCheckBox showPassword;

    public RegisterForm() {
        setTitle("Register");
        setResizable(false);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Panel kiri (warna navy)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(20, 33, 61));
        leftPanel.setBounds(0, 0, 200, 400);
        leftPanel.setLayout(null);

        JLabel lblLeftTitle = new JLabel("Register");
        lblLeftTitle.setForeground(new Color(173, 216, 230));
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLeftTitle.setBounds(40, 30, 150, 30);
        leftPanel.add(lblLeftTitle);

        add(leftPanel);
        
        try {
    ImageIcon loginImage = new ImageIcon(getClass().getResource("/gambar/11.png"));
    JLabel lblIcon = new JLabel(loginImage);
    lblIcon.setBounds(25, 80, 150, 150); // Atur posisi & ukuran sesuai kebutuhan
    leftPanel.add(lblIcon);
} catch (Exception ex) {
    System.out.println("Icon tidak ditemukan: " + ex.getMessage());
}

        
        // Username
        JLabel lblUsername = new JLabel("Username");
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/gambar/imah.png"));
    lblUsername = new JLabel("Username", userIcon, JLabel.LEFT);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(250, 50, 100, 20);
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(250, 75, 400, 30);
        tfUsername.setBackground(new Color(230, 230, 230));
        tfUsername.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(tfUsername);

        // Password
        JLabel lblPassword = new JLabel("Password");
        ImageIcon pwIcon = new ImageIcon(getClass().getResource("/gambar/psw.png"));
        lblPassword = new JLabel("Username", pwIcon, JLabel.LEFT);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(250, 115, 100, 20);
        add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(250, 140, 400, 30);
        pfPassword.setBackground(new Color(230, 230, 230));
        pfPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(pfPassword);

        // Checkbox: Show Password
        showPassword = new JCheckBox("Lihat Password");
        showPassword.setBounds(250, 175, 200, 20);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setBackground(Color.WHITE);
        add(showPassword);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                pfPassword.setEchoChar((char) 0); // tampilkan
            } else {
                pfPassword.setEchoChar('•'); // sembunyikan
            }
        });

        // Tombol Register
        JButton btnRegister = new JButton("Konfirmasi");
        btnRegister.setBounds(250, 220, 400, 35);
        btnRegister.setBackground(new Color(144, 224, 239));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(btnRegister);

        btnRegister.addActionListener(e -> registerUser());
    }

    private void registerUser() {
        String username = tfUsername.getText().trim();
        String password = String.valueOf(pfPassword.getPassword()).trim();
        String role = "siswa"; // hanya siswa

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!");
            return;
        }

        try {
            Connection conn = config.configDB();

            // Cek apakah username sudah ada
            String cekSql = "SELECT * FROM tb_peserta WHERE username = ?";
            PreparedStatement cekStmt = conn.prepareStatement(cekSql);
            cekStmt.setString(1, username);
            ResultSet rs = cekStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
                return;
            }

            // Simpan ke database
            String sql = "INSERT INTO tb_peserta (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registrasi berhasil sebagai siswa!");
            dispose();
            new MainMenu().setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registrasi gagal: " + ex.getMessage());
        }
    }
}
