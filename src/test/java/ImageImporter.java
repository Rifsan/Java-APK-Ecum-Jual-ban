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
import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageImporter extends JFrame {
    private JLabel imageLabel;
    private File pathsFile;
    private ArrayList<String> imagePaths;

    public ImageImporter() {
        setTitle("Import Gambar");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imagePaths = new ArrayList<>();

        // Pastikan direktori data dan images tersedia
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdir();

        File imageDir = new File("images");
        if (!imageDir.exists()) imageDir.mkdir();

        pathsFile = new File("data/gambar_paths.txt");

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout());

        // Label gambar
        imageLabel = new JLabel("Gambar Belum Dipilih", JLabel.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Tombol impor
        JButton importButton = new JButton("Impor Gambar");
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Pilih Gambar");
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Gambar", "jpg", "jpeg", "png"));

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        String directory = "images/";
                        String relativePath = directory + selectedFile.getName();
                        File destFile = new File(relativePath);

                        // Salin gambar
                        try (InputStream in = new FileInputStream(selectedFile);
                             OutputStream out = new FileOutputStream(destFile)) {

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = in.read(buffer)) > 0) {
                                out.write(buffer, 0, length);
                            }
                        }

                        savePathToFile(relativePath);

                        // Tampilkan gambar
                        ImageIcon imageIcon = new ImageIcon(relativePath);
                        imageLabel.setIcon(imageIcon);
                        imageLabel.setText(""); // Hapus teks default

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Gagal menyimpan gambar.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(importButton, BorderLayout.SOUTH);
        add(panel);

        // Muat gambar terakhir jika ada
        loadImagesFromFile();
        if (!imagePaths.isEmpty()) {
            displayImage(0);
        }
    }

    private void savePathToFile(String relativePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathsFile, true))) {
            writer.write(relativePath);
            writer.newLine();
            imagePaths.add(relativePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImagesFromFile() {
        if (pathsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathsFile))) {
                String path;
                while ((path = reader.readLine()) != null) {
                    if (new File(path).exists()) {
                        imagePaths.add(path);
                    } else {
                        System.out.println("File tidak ditemukan: " + path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayImage(int index) {
        if (index >= 0 && index < imagePaths.size()) {
            String path = imagePaths.get(index);
            ImageIcon imageIcon = new ImageIcon(path);
            imageLabel.setIcon(imageIcon);
            imageLabel.setText(""); // Hapus teks default
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageImporter().setVisible(true));
    }
}
