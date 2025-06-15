
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
import java.util.ArrayList;

public class SimulasiKeranjang extends JFrame {
    
    private JPanel panelKeranjang;

    public SimulasiKeranjang() {
        setTitle("Simulasi Keranjang Belanja");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelKeranjang = new JPanel();
        panelKeranjang.setLayout(new BoxLayout(panelKeranjang, BoxLayout.Y_AXIS));
        panelKeranjang.setBorder(BorderFactory.createTitledBorder("Keranjang Anda"));

        // Simulasi data produk
        ArrayList<String> daftarProduk = new ArrayList<>();
        daftarProduk.add("Ban Mobil Bridgestone");
        daftarProduk.add("Ban Motor FDR");
        daftarProduk.add("Oli Mesin Shell");
        daftarProduk.add("Velg Racing 17\""); // Produk ke-4, tidak akan ditampilkan

        tampilkanKeranjang(daftarProduk);

        add(new JScrollPane(panelKeranjang));
        setVisible(true);
    }

    private void tampilkanKeranjang(ArrayList<String> daftarProduk) {
        panelKeranjang.removeAll(); // Bersihkan isi panel

        if (daftarProduk.isEmpty()) {
            panelKeranjang.add(new JLabel("Keranjang kosong."));
        } else {
            int jumlahTampil = Math.min(3, daftarProduk.size());
            for (int i = 0; i < jumlahTampil; i++) {
                String namaProduk = daftarProduk.get(i);
                JLabel label = new JLabel((i + 1) + ". " + namaProduk);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                panelKeranjang.add(label);
            }

            if (daftarProduk.size() > 3) {
                panelKeranjang.add(new JLabel("...dan " + (daftarProduk.size() - 3) + " produk lainnya"));
            }
        }

        panelKeranjang.revalidate();
        panelKeranjang.repaint();
    }

    public static void main(String[] args) {
        new SimulasiKeranjang();
    }
}
