package ppdb;
import javax.swing.*; 
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("PPDB SMA Tadika Mesra");
        setResizable(false);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(20, 33, 61)); // Warna navy gelap
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Selamat Datang di");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setBounds(240, 70, 300, 30);
        add(welcomeLabel);

        JLabel ppdbLabel = new JLabel("PPDB SMK Tadika Mesra");
        ppdbLabel.setForeground(Color.WHITE);
        ppdbLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        ppdbLabel.setBounds(180, 110, 400, 40);
        add(ppdbLabel);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(230, 200, 100, 40);
        registerBtn.setBackground(new Color(255, 153, 51)); // Oranye
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(registerBtn);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(360, 200, 100, 40);
        loginBtn.setBackground(new Color(0, 204, 102)); // Hijau
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(loginBtn);
 
        registerBtn.addActionListener(e -> { 
            new RegisterForm().setVisible(true); // ✅ ini membuka RegisterForm
        });

        loginBtn.addActionListener(e -> {
            new LoginForm().setVisible(true);
            
        });
    } 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
    