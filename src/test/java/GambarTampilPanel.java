/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author acer
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GambarTampilPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tampil Panel Jika Ada Gambar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JButton btnPilih = new JButton("Pilih Gambar");
        JPanel panelGambar = new JPanel();
        JLabel labelGambar = new JLabel();

        panelGambar.add(labelGambar);
        panelGambar.setVisible(false); // panel awalnya disembunyikan

        btnPilih.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("File Gambar", "jpg", "png", "jpeg", "gif"));
                int hasil = fileChooser.showOpenDialog(null);
                if (hasil == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());

                    // Resize agar pas di JLabel
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    labelGambar.setIcon(new ImageIcon(img));

                    panelGambar.setVisible(true); // tampilkan panel setelah gambar dimasukkan
                }
            }
        });

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.add(btnPilih, BorderLayout.NORTH);
        panelUtama.add(panelGambar, BorderLayout.CENTER);

        frame.add(panelUtama);
        frame.setVisible(true);
    }
}
