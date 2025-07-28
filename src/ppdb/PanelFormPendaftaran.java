package ppdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelFormPendaftaran extends JPanel {
    private CardLayout layout;
    private JPanel stepContainer;
    private int currentStep = 0;

    private PanelDataDiri panelDataDiri;
    private PanelOrtu panelOrtu;
    private PanelAsalSekolah panelAsalSekolah;
    private PanelJalurJurusan panelJalurJurusan;
    private JPanel panelTambahanJalur; // Dinamis
    private PanelDokumen panelDokumen;

    private JButton btnPrev, btnNext;
    private int idUser;

    public PanelFormPendaftaran(int idUser) {
        this.idUser = idUser;
        layout = new CardLayout();
        stepContainer = new JPanel(layout);

        // Inisialisasi panel-panel
        panelDataDiri = new PanelDataDiri(idUser);
        panelOrtu = new PanelOrtu(idUser);
        panelAsalSekolah = new PanelAsalSekolah(idUser);
        panelJalurJurusan = new PanelJalurJurusan(idUser);
        panelDokumen = new PanelDokumen(idUser);

        // Tambahkan panel-panel ke stepContainer
        stepContainer.add(panelDataDiri, "step0");
        stepContainer.add(panelOrtu, "step1");
        stepContainer.add(panelAsalSekolah, "step2");
        stepContainer.add(panelJalurJurusan, "step3");
        stepContainer.add(new JPanel(), "step4"); // Placeholder untuk panel tambahan
        stepContainer.add(panelDokumen, "step5");

        // Tombol navigasi
        btnPrev = new JButton("❮ Sebelumnya");
        btnNext = new JButton("Selanjutnya ❯");

        // Aksi tombol "Sebelumnya"
        btnPrev.addActionListener(e -> {
            if (currentStep > 0) {
                currentStep--;
                layout.show(stepContainer, "step" + currentStep);
                if (currentStep < 5) {
                    btnNext.setText("Selanjutnya ❯");
                }
            }
        });

        // Aksi tombol "Selanjutnya" dan "Selesai"
        btnNext.addActionListener(e -> {
            if (currentStep == 3) {
                // Tambahkan panel tambahan sesuai jalur
                String jalur = panelJalurJurusan.getSelectedJalur();

                if (jalur == null || jalur.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Silakan pilih jalur terlebih dahulu.");
                    return;
                }

                if (jalur.equalsIgnoreCase("transkripsi nilai")) {
                    panelTambahanJalur = new PanelTranskrip(idUser);
                } else if (jalur.equalsIgnoreCase("prestasi")) {
                    panelTambahanJalur = new PanelPrestasi(idUser);
                } else if (jalur.equalsIgnoreCase("zonasi")) {
                    panelTambahanJalur = new PanelZonasi(idUser);
                } else {
                    panelTambahanJalur = new JPanel(); // fallback panel kosong
                }

                stepContainer.remove(4);
                stepContainer.add(panelTambahanJalur, "step4", 4);
            }

            if (currentStep < 5) {
                currentStep++;
                layout.show(stepContainer, "step" + currentStep);

                // Jika sampai langkah terakhir, ubah teks tombol
                if (currentStep == 5) {
                    btnNext.setText("Selesai");
                }
            } else {
                // Tekan tombol "Selesai"
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menyelesaikan pendaftaran?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    // Tutup jendela form dan kembali ke dashboard
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JFrame) {
                        window.dispose();

                        // Jika dashboard utama sebelumnya ditutup, bisa aktifkan ini:
                        // new DashboardUtama().setVisible(true);
                    }
                }
            }
        });

        // Panel navigasi bawah
        JPanel navPanel = new JPanel();
        navPanel.add(btnPrev);
        navPanel.add(btnNext);

        // Layout utama
        setLayout(new BorderLayout());
        add(stepContainer, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
    }
}

