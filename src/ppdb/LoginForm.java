package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {

    JTextField tfUsername;
    JPasswordField pfPassword;
    JCheckBox cbShowPassword;

    public LoginForm() {
        setTitle("Login");
        setResizable(false);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Panel kiri
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(20, 33, 61));
        leftPanel.setBounds(0, 0, 200, 400);
        leftPanel.setLayout(null);

        JLabel lblLeftTitle = new JLabel("Login");
        lblLeftTitle.setForeground(new Color(173, 216, 230));
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLeftTitle.setBounds(50, 30, 100, 30);
        leftPanel.add(lblLeftTitle);

        add(leftPanel);
        
        // Tambahkan setelah lblLeftTitle ditambahkan ke panel
try {
    ImageIcon loginImage = new ImageIcon(getClass().getResource("/gambar/1.png"));
    JLabel lblIcon = new JLabel(loginImage);
    lblIcon.setBounds(25, 80, 150, 150); // Atur posisi & ukuran sesuai kebutuhan
    leftPanel.add(lblIcon);
} catch (Exception ex) {
    System.out.println("Icon tidak ditemukan: " + ex.getMessage());
}


        // Username
        JLabel lblUsername;
        try {
            ImageIcon loginIcon = new ImageIcon(getClass().getResource("imah.png"));
            lblUsername = new JLabel("Username", loginIcon, JLabel.LEFT);
        } catch (Exception ex) {
            lblUsername = new JLabel("Username");
        }
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(250, 70, 150, 20);
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(250, 95, 400, 30);
        tfUsername.setBackground(new Color(230, 230, 230));
        tfUsername.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(tfUsername);

        // Password
       
        
        JLabel lblPassword;
        try {
            ImageIcon PasswordIcon = new ImageIcon(getClass().getResource("/gambar/psw.png"));
            lblPassword = new JLabel("Password", PasswordIcon, JLabel.LEFT);
        } catch (Exception ex) {
            lblPassword = new JLabel("Password");
        }
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(250, 145, 100, 20);
        add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(250, 170, 400, 30);
        pfPassword.setBackground(new Color(230, 230, 230));
        pfPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(pfPassword);

        // Checkbox "Lihat Password"
        cbShowPassword = new JCheckBox("Lihat Password");
        cbShowPassword.setBounds(250, 205, 150, 20);
        cbShowPassword.setBackground(Color.WHITE);
        cbShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(cbShowPassword);

        cbShowPassword.addActionListener(e -> {
            if (cbShowPassword.isSelected()) {
                pfPassword.setEchoChar((char) 0);
            } else {
                pfPassword.setEchoChar('•');
            }
        });

        // Lupa Password
        JLabel lupaPassword = new JLabel("Lupa password?");
        lupaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lupaPassword.setForeground(Color.BLACK);
        lupaPassword.setBounds(540, 205, 120, 20);
        lupaPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lupaPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                recoverPassword();
            }
        });
        add(lupaPassword);

        // Tombol Login
        JButton btnLogin = new JButton("Masuk");
        btnLogin.setBounds(250, 240, 400, 35);
        btnLogin.setBackground(new Color(144, 224, 239));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(btnLogin);

        btnLogin.addActionListener(e -> loginUser());
    }

   private void loginUser() {
    String username = tfUsername.getText().trim();
    String password = String.valueOf(pfPassword.getPassword()).trim();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Silakan isi semua kolom!");
        return;
    }

    try {
        Connection conn = config.configDB();
        String sql = "SELECT * FROM tb_peserta WHERE BINARY username=? AND BINARY password=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int idUser = rs.getInt("id_user");
            String role = rs.getString("role");

            Session.setCurrentUserId(idUser);
            JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + role + "!");

            dispose();
            if (role.equalsIgnoreCase("admin")) {
                new DashboardAdmin().setVisible(true);
            } else {
                new DashboardSiswa().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah.");
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Login gagal: " + ex.getMessage());
    }
}


    private void recoverPassword() {
        String inputUsername = JOptionPane.showInputDialog(this, "Masukkan username Anda:");
        if (inputUsername != null && !inputUsername.trim().isEmpty()) {
            try {
                Connection conn = config.configDB();
                String sqlCheck = "SELECT * FROM tb_peserta WHERE username=?";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setString(1, inputUsername);
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    String newPassword = JOptionPane.showInputDialog(this, "Masukkan password baru:");
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        String sqlUpdate = "UPDATE tb_peserta SET password=? WHERE username=?";
                        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                        psUpdate.setString(1, newPassword);
                        psUpdate.setString(2, inputUsername);
                        psUpdate.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Password berhasil diperbarui!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Password baru tidak boleh kosong.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Username tidak ditemukan.");
                }

                rs.close();
                psCheck.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username tidak boleh kosong.");
        }
    }
}


