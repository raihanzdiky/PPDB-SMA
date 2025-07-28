package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class DashboardAdmin extends JFrame {

    public DashboardAdmin() {
        int idUser = Session.getCurrentUserId();
        setTitle("Dashboard Admin PPDB");
        setSize(1200, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(20, 33, 61));
        header.setBounds(0, 0, 1200, 120);
        header.setLayout(null);
        add(header);
        
        String username = getUsernameFromDB(idUser);

// Label Selamat Datang di kiri bawah
ImageIcon Icon = new ImageIcon(getClass().getResource("/gambar/akun1.png"));
JLabel lblWelcome = new JLabel("Hi selamat datang, " + username, Icon, SwingConstants.CENTER);
lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
lblWelcome.setForeground(Color.DARK_GRAY);
lblWelcome.setBounds(20, 580, 400, 30);
 // posisikan di kiri bawah
add(lblWelcome);


        ImageIcon logo = new ImageIcon(getClass().getResource("/gambar/icon-dashboard-9.png"));
        JLabel lblTitle = new JLabel("PPDB SMK TADIKA MESRA", logo, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(300, 30, 600, 50);
        header.add(lblTitle);

        JButton btnLogout = new JButton("LOG OUT");
        btnLogout.setBounds(1050, 40, 100, 30);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.RED);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        header.add(btnLogout);

        btnLogout.addActionListener(e -> showLogoutDialog());

        // Panel Menu
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(2, 2, 30, 30));
        panelMenu.setBounds(100, 170, 1000, 400);
        panelMenu.setBackground(new Color(240, 240, 240));
        add(panelMenu);

        JButton btnDataDiri = createMenuButton("Kelola Data Siswa", new Color(255, 167, 38));
        JButton btnSeleksi = createMenuButton("Seleksi", new Color(144, 202, 249));
        JButton btnPengumuman = createMenuButton("Pengumuman", new Color(255, 235, 59));
        JButton btnStatistik = createMenuButton("Statistik", new Color(129, 212, 250));

        panelMenu.add(btnDataDiri);
        panelMenu.add(btnSeleksi);
        panelMenu.add(btnPengumuman);
        panelMenu.add(btnStatistik);

        btnDataDiri.addActionListener(e -> new FormKelolaDataSiswa().setVisible(true));

        btnSeleksi.addActionListener(e -> {
            new FormSeleksiVerifikasiAdmin().setVisible(true);
            dispose();
        });

        btnPengumuman.addActionListener(e -> {
            new FormPengumumanAdmin().setVisible(true);
            dispose();
        });

        btnStatistik.addActionListener(e -> new FormStatistikAdmin().setVisible(true));
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return button;
    }
    
     private String getUsernameFromDB(int idUser) {
    String username = "";
    try {
        Connection conn = config.configDB();
        String sql = "SELECT username FROM tb_peserta WHERE id_user=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUser);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            username = rs.getString("username");
        }
        rs.close();
        ps.close();
        conn.close();
    } catch (Exception ex) {
        System.out.println("Gagal mengambil username: " + ex.getMessage());
    }
    return username;
}

    private void showLogoutDialog() {
        JDialog dialog = new JDialog(this, "Konfirmasi Logout", true);
        dialog.setSize(400, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JLabel message = new JLabel("Anda ingin keluar?", SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dialog.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton btnYa = new JButton("YA");
        btnYa.setBackground(new Color(0, 200, 83));
        btnYa.setForeground(Color.WHITE);
        btnYa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnYa.setFocusPainted(false);
        btnYa.addActionListener(e -> {
            dialog.dispose();
            dispose();
            new MainMenu().setVisible(true);
        });

        JButton btnTidak = new JButton("TIDAK");
        btnTidak.setBackground(new Color(229, 57, 53));
        btnTidak.setForeground(Color.WHITE);
        btnTidak.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTidak.setFocusPainted(false);
        btnTidak.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnYa);
        buttonPanel.add(btnTidak);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
} 
