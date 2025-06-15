/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GambarManager {
    private static final String DATA_PATH = "data/gambar_paths.txt";

    // Simpan path gambar (append agar tidak tertimpa)
    public static void simpanPathGambar(String pathGambar) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PATH, true))) {
            writer.write(pathGambar);  // Menambahkan path gambar baru ke file
            writer.newLine();           // Pisahkan dengan baris baru
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ambil semua path gambar yang tersimpan
    public static String[] ambilSemuaPathGambar() {
        File pathFile = new File(DATA_PATH);  // File yang menyimpan path gambar
        if (pathFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
                List<String> paths = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    paths.add(line);  // Tambahkan path gambar ke list
                }
                return paths.toArray(new String[0]);  // Mengembalikan array dari list
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String[0];  // Mengembalikan array kosong jika file tidak ada
    }

    // Menampilkan gambar ke label
    public static void tampilkanGambarKeLabel(JLabel label) {
        String[] paths = ambilSemuaPathGambar();  // Ambil semua path gambar
        if (paths.length > 0) {
            String path = paths[paths.length - 1]; // Ambil gambar terakhir yang ditambahkan
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
                label.setText("");  // Kosongkan teks jika gambar ada
            } else {
                label.setIcon(null);
                label.setText("Gambar tidak ditemukan");
            }
        }
    }
    // Hapus semua path dan file gambar
public static void hapusSemuaDataGambar() {
    // Hapus file data path
    File dataFile = new File(DATA_PATH);
    if (dataFile.exists()) {
        dataFile.delete();
    }

    // Hapus semua gambar dari folder 'images'
    File imageDir = new File("images");
    if (imageDir.exists() && imageDir.isDirectory()) {
        File[] files = imageDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
public static void tampilkanGambarKeLabelBerdasarkanIndex(JLabel label, int index) {
    String[] paths = ambilSemuaPathGambar();
    if (index >= 0 && index < paths.length) {
        String path = paths[index];
        if (new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            label.setText("");  // Kosongkan teks jika gambar ada
        } else {
            label.setIcon(null);
            label.setText("Gambar tidak ditemukan");
        }
    } else {
        label.setIcon(null);
        label.setText("Tidak ada gambar pada index ini");
    }
}

    // Menampilkan semua gambar ke label
    public static void tampilkanSemuaGambarKeLabel(JLabel[] labels) {
        String[] paths = ambilSemuaPathGambar();  // Ambil semua path gambar
        for (int i = 0; i < labels.length && i < paths.length; i++) {
            String path = paths[i];
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(labels[i].getWidth(), labels[i].getHeight(), Image.SCALE_SMOOTH);
                labels[i].setIcon(new ImageIcon(img));
                labels[i].setText("");  // Kosongkan teks jika gambar ada
            } else {
                labels[i].setIcon(null);
                labels[i].setText("Gambar tidak ditemukan");
            }
        }
    }
}


