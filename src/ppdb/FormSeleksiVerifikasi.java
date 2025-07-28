package ppdb;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormSeleksiVerifikasi extends JFrame {
    private JLabel lblStatus, lblWawancara, lblTes;
    private int idUser;

    // Warna Kustom
    private final Color PRIMARY_COLOR = new Color(34, 49, 63);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color TEXT_COLOR_DARK = new Color(44, 62, 80);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color INFO_COLOR = new Color(241, 196, 15);

    public FormSeleksiVerifikasi(int idUser) {
        this.idUser = idUser;

        setTitle("Hasil Seleksi & Jadwal");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblJudul = new JLabel("Status Seleksi dan Jadwal Penting");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJudul.setForeground(Color.WHITE);
        headerPanel.add(lblJudul);
        add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TEXT_COLOR_DARK, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel statusTitle = new JLabel("Status Pendaftaran:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusTitle.setForeground(TEXT_COLOR_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(statusTitle, gbc);

        lblStatus = new JLabel("-");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblStatus.setForeground(INFO_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(lblStatus, gbc);

        JLabel wawancaraTitle = new JLabel("Jadwal Wawancara:");
        wawancaraTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        wawancaraTitle.setForeground(TEXT_COLOR_DARK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(wawancaraTitle, gbc);

        lblWawancara = new JLabel("-");
        lblWawancara.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblWawancara.setForeground(TEXT_COLOR_DARK);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(lblWawancara, gbc);

        JLabel tesTitle = new JLabel("Jadwal Tes:");
        tesTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tesTitle.setForeground(TEXT_COLOR_DARK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(tesTitle, gbc);

        lblTes = new JLabel("-");
        lblTes.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblTes.setForeground(TEXT_COLOR_DARK);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(lblTes, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        footerPanel.setBorder(new EmptyBorder(0, 20, 15, 20));

        JButton btnKembali = createStyledButton("Kembali ke Dashboard", SECONDARY_COLOR);
        footerPanel.add(btnKembali);
        add(footerPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            dispose();
            // new DashboardSiswa().setVisible(true);
        });

        loadData();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void loadData() {
        try (Connection con = config.configDB()) {
            String sql = "SELECT status, jadwal_wawancara, jadwal_tes FROM tb_verifikasi WHERE id_user = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                String wawancara = rs.getString("jadwal_wawancara");
                String tes = rs.getString("jadwal_tes");

                if ("diterima".equalsIgnoreCase(status)) {
                    lblStatus.setText("Status: DITERIMA");
                    lblStatus.setForeground(SUCCESS_COLOR);
                } else if ("ditolak".equalsIgnoreCase(status)) {
                    lblStatus.setText("Status: DITOLAK");
                    lblStatus.setForeground(DANGER_COLOR);
                } else {
                    lblStatus.setText("Status: BELUM DIVERIFIKASI");
                    lblStatus.setForeground(INFO_COLOR);
                }

                lblWawancara.setText("Jadwal Wawancara: " + (wawancara != null ? wawancara : "Belum Diatur"));
                lblTes.setText("Jadwal Tes: " + (tes != null ? tes : "Belum Diatur"));

            } else {
                lblStatus.setText("Status: BELUM DIVERIFIKASI");
                lblStatus.setForeground(INFO_COLOR);
                lblWawancara.setText("Jadwal Wawancara: Belum Diatur");
                lblTes.setText("Jadwal Tes: Belum Diatur");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}