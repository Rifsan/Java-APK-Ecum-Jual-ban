/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package devlop_apk_ban;



import devlop_apk_ban.Barang;
import java.awt.BorderLayout;
import layout.WrapLayout;

import javax.sound.sampled.*;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
//\import java.awt.Imasge;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;

/**
 *
 * @author acer
 */


public class Aplikasi extends javax.swing.JFrame {

    
    

    /**
     * Creates new form Home
     */
private boolean modeHistori = false;
private JPanel jplist;

private void logout() {
    int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
    if (konfirmasi == JOptionPane.YES_OPTION) {
        this.dispose(); // Tutup form sekarang

        // Tampilkan form login lagi
        new Loginn().setVisible(true);
    }
}
private void playSound(String soundPath) {
    try {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(soundPath));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void simpanProgressGame(int level, int hp, int maxHP, int mHP, int mMaxHP) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = """
            INSERT INTO game_progress (idUser, level, playerHP, maxHP, monsterHP, monsterMaxHP)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                level = VALUES(level),
                playerHP = VALUES(playerHP),
                maxHP = VALUES(maxHP),
                monsterHP = VALUES(monsterHP),
                monsterMaxHP = VALUES(monsterMaxHP)
        """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);
        stmt.setInt(2, level);
        stmt.setInt(3, hp);
        stmt.setInt(4, maxHP);
        stmt.setInt(5, mHP);
        stmt.setInt(6, mMaxHP);
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private int[] loadProgressGame() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT level, playerHP, maxHP, monsterHP, monsterMaxHP FROM game_progress WHERE idUser = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new int[] {
                rs.getInt("level"),
                rs.getInt("playerHP"),
                rs.getInt("maxHP"),
                rs.getInt("monsterHP"),
                rs.getInt("monsterMaxHP")
            };
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    // Default jika belum pernah main
    return new int[] {1, 100, 100, 100, 100};
}

private void tampilkanStokBarang() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = """
            SELECT p.brand, d.size, d.stok
            FROM produk p
            JOIN detail_produk d ON p.idProduk = d.idProduk
            ORDER BY p.brand, d.size
        """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            String nama = rs.getString("brand");
            String ukuran = rs.getString("size");
            int stok = rs.getInt("stok");

            sb.append("Produk: ").append(nama)
              .append(", Ukuran: ").append(ukuran)
              .append(", Stok: ").append(stok)
              .append("\n");
        }

        //tsdetailbarang.setText(sb.toString()); // atau JTextArea lain
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menampilkan stok: " + ex.getMessage());
    }
}

public void hapusItemKeranjang(int idProduk, String ukuran) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "DELETE FROM keranjang WHERE idUser = ? AND idProduk = ? AND ukuran = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);
        stmt.setInt(2, idProduk);
        stmt.setString(3, ukuran);
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menghapus item dari keranjang: " + e.getMessage());
    }
}

private void cariBarang(String keyword, JPanel targetPanel) {
    targetPanel.removeAll();

    for (Barang b : BarangDB.getSemuaBarang()) {
        if (b.getNama().toLowerCase().contains(keyword.toLowerCase())) {
            JPanel panelItem = new JPanel();
            panelItem.setLayout(new BoxLayout(panelItem, BoxLayout.Y_AXIS));
            panelItem.setBackground(Color.DARK_GRAY);
            panelItem.setPreferredSize(new Dimension(150, 180));
            panelItem.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            panelItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Gambar
            String imgPath = "src/main/java/" + b.getImagePath();
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imgPath);
                Image scaled = originalIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(scaled));
                lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelItem.add(lblImg);
            } else {
                JLabel lblMissing = new JLabel("Gambar Tidak Ada");
                lblMissing.setForeground(Color.RED);
                lblMissing.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelItem.add(lblMissing);
            }

            // Nama
            JLabel lblNama = new JLabel(b.getNama(), JLabel.CENTER);
            lblNama.setForeground(Color.WHITE);
            lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelItem.add(lblNama);

            // Klik detail
            panelItem.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    tampilkanDetailBarang(b);
                }
            });

            targetPanel.add(panelItem);
        }
    }

    targetPanel.revalidate();
    targetPanel.repaint();
}


public void tampilkanNotifikasi() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT pesan, tanggal FROM notifikasi WHERE idUser = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);

        ResultSet rs = stmt.executeQuery();
        jpisinotif.removeAll();  // panel untuk notifikasi

        while (rs.next()) {
            String pesan = rs.getString("pesan");
            JLabel lbl = new JLabel(pesan);
            jpisinotif.add(lbl);
        }

        jpisinotif.revalidate();
        jpisinotif.repaint();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


private void tampilkanNotifikasiUser() {
    jpisinotif.removeAll();
    jpisinotif.setLayout(new BoxLayout(jpisinotif, BoxLayout.Y_AXIS));

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM notifikasi_user WHERE idUser = ? ORDER BY waktu DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String pesan = rs.getString("pesan");
            String waktu = rs.getString("waktu");

            JLabel lbl = new JLabel("<html><b>" + waktu + ":</b> " + pesan + "</html>");
            lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            jpisinotif.add(lbl);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memuat notifikasi: " + e.getMessage());
    }

    jpisinotif.revalidate();
    jpisinotif.repaint();
}


public static int currentUserId = -1;
public static String currentUsername = "";
public static String userRole = "";
private File buktiTransferFile; // untuk menyimpan file upload



 private int hitungTotal() {
    int total = 0;
    for (ItemKeranjang item : daftarKeranjang) {
        total += item.getTotalHarga(); // jumlah * harga
    }
    return total;
}

private JPanel duplikatPanelResume() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    JLabel lblResume = new JLabel("Resume Belanja");
    lblResume.setForeground(Color.WHITE);

    JLabel lblTotal = new JLabel("Total:");
    lblTotal.setForeground(Color.WHITE);

    JLabel lblTotalHarga = new JLabel(jltotal.getText());
    lblTotalHarga.setForeground(Color.WHITE);

    JButton btnBeli = new JButton("Beli");
    btnBeli.addActionListener(e -> btnbeli1.doClick());

    panel.add(lblResume, BorderLayout.NORTH);

    JPanel isi = new JPanel();
    isi.setOpaque(false);
    isi.add(lblTotal);
    isi.add(lblTotalHarga);
    isi.add(btnBeli);

    panel.add(isi, BorderLayout.CENTER);

    return panel;
}


private void tampilkanPanelKeranjang(JPanel isiKiri) {
    Component[] komponen = jpkeranjang.getComponents();
    for (Component comp : komponen) {
        if (comp.getName() != null && comp.getName().equals("dinamis")) {
            jpkeranjang.remove(comp);
        }
    }

    JPanel gabungan = new JPanel(new BorderLayout());
    gabungan.setName("dinamis");
    gabungan.add(isiKiri, BorderLayout.CENTER);
    gabungan.add(jpresume, BorderLayout.EAST);

    jpkeranjang.add(gabungan, BorderLayout.CENTER);
    jpkeranjang.revalidate();
    jpkeranjang.repaint();
}




    
private void simpanHistoriTransaksi() {
    try (Connection conn = DatabaseConnection.getConnection()) {
        // Simpan transaksi ke histori_transaksi
        String sqlTransaksi = "INSERT INTO histori_transaksi (tanggal, total, idUser) VALUES (?, ?, ?)";
        PreparedStatement stmtTransaksi = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
        stmtTransaksi.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));

        int total = daftarKeranjang.stream().mapToInt(ItemKeranjang::getTotalHarga).sum();
        stmtTransaksi.setInt(2, total);

        if (Aplikasi.currentUserId <= 0) {
            JOptionPane.showMessageDialog(null, "User belum login, tidak bisa menyimpan histori.");
            return;
        }

        stmtTransaksi.setInt(3, Aplikasi.currentUserId);
        stmtTransaksi.executeUpdate();

        ResultSet rs = stmtTransaksi.getGeneratedKeys();
        int idTransaksi = -1;
        if (rs.next()) {
            idTransaksi = rs.getInt(1);
        }

        // Kembalikan ke versi sebelumnya dengan idProduk
        String sqlDetail = "INSERT INTO histori_detail (id_transaksi, idProduk, nama_barang, ukuran, jumlah, harga) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);

        for (ItemKeranjang item : daftarKeranjang) {
            stmtDetail.setInt(1, idTransaksi);
 // pastikan ItemKeranjang punya idProduk
            stmtDetail.setString(3, item.getNama());
            stmtDetail.setString(4, item.getUkuran());
            stmtDetail.setInt(5, item.getJumlah());
            stmtDetail.setInt(6, item.getHarga());
            stmtDetail.executeUpdate();
        }

        JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan ke histori.");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan histori transaksi: " + e.getMessage());
    }
}

private void tampilkanBarangKePanel(JPanel targetPanel) {
    targetPanel.removeAll();

    for (Barang b : BarangDB.getSemuaBarang()) {
        JPanel panelItem = new JPanel();
        panelItem.setLayout(new BoxLayout(panelItem, BoxLayout.Y_AXIS));
        panelItem.setBackground(Color.DARK_GRAY);
        panelItem.setPreferredSize(new Dimension(150, 180)); // Ukuran kotak
        panelItem.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        panelItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Gambar
        String imgPath = "src/main/java/" + b.getImagePath();
        File imgFile = new File(imgPath);
        if (imgFile.exists()) {
            ImageIcon originalIcon = new ImageIcon(imgPath);
            Image scaled = originalIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            JLabel lblImg = new JLabel(new ImageIcon(scaled));
            lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelItem.add(lblImg);
        } else {
            JLabel lblMissing = new JLabel("Gambar Tidak Ada");
            lblMissing.setForeground(Color.RED);
            lblMissing.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelItem.add(lblMissing);
        }

        // Nama
        JLabel lblNama = new JLabel(b.getNama(), JLabel.CENTER);
        lblNama.setForeground(Color.WHITE);
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelItem.add(lblNama);

        // Klik ke detail
        panelItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tampilkanDetailBarang(b);
            }
        });

        targetPanel.add(panelItem);
    }

    targetPanel.revalidate();
    targetPanel.repaint();
}


public static void tambahKeKeranjang(int userId, ItemKeranjang item) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "INSERT INTO keranjang (idUser, idProduk, ukuran, jumlah) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE jumlah = jumlah + ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.setInt(2, item.getIdProduk());
        stmt.setString(3, item.getUkuran());
        stmt.setInt(4, item.getJumlah());
        stmt.setInt(5, item.getJumlah());
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menambahkan ke keranjang: " + e.getMessage());
    }
}

public static List<ItemKeranjang> ambilKeranjang(int userId) {
    List<ItemKeranjang> daftar = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT k.idProduk, p.brand, k.ukuran, k.jumlah, p.harga " +
                     "FROM keranjang k JOIN produk p ON k.idProduk = p.idProduk WHERE k.idUser = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ItemKeranjang item = new ItemKeranjang();
            item.setIdProduk(rs.getInt("idProduk"));
            item.setNama(rs.getString("brand"));
            item.setUkuran(rs.getString("ukuran"));
            item.setJumlah(rs.getInt("jumlah"));
            item.setHarga(rs.getInt("harga"));
            daftar.add(item);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mengambil data keranjang: " + e.getMessage());
    }
    return daftar;
}




private List<JCheckBox> daftarCheckbox = new ArrayList<>();
private List<ItemKeranjang> itemCheckboxTerkait = new ArrayList<>();

public void updateTampilanKeranjang() {
    jpisibarang.removeAll();
    daftarCheckbox.clear();

    jpisibarang.setLayout(new BoxLayout(jpisibarang, BoxLayout.Y_AXIS));

    int total = 0;

    for (ItemKeranjang item : daftarKeranjang) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(600, 80));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.LIGHT_GRAY);

        // ✅ Checkbox + Label Deskripsi
        JCheckBox checkBox = new JCheckBox();
        daftarCheckbox.add(checkBox); // simpan agar bisa diakses untuk "Pilih Semua"

        JLabel labelInfo = new JLabel(item.getNama() + " | Ukuran: " + item.getUkuran() + " | Jumlah: " + item.getJumlah());
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kiri.setOpaque(false);
        kiri.add(checkBox);
        kiri.add(labelInfo);

        // ✅ Label Harga + Tombol Hapus
        JLabel lblHarga = new JLabel("Harga: Rp " + item.getTotalHarga());
        JButton btnHapus = new JButton("Hapus");

     btnHapus.addActionListener(e -> {
    daftarKeranjang.remove(item); // hapus dari list
    int idProduk = BarangDB.cariIdProdukByNama(item.getNama()); // pastikan fungsi ini ada
    hapusItemKeranjang(idProduk, item.getUkuran()); // hapus dari database
    updateTampilanKeranjang(); // refresh tampilan
});


        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        kanan.setOpaque(false);
        kanan.add(lblHarga);
        kanan.add(btnHapus);

        // Gabungkan ke panel utama
        panel.add(kiri, BorderLayout.CENTER);
        panel.add(kanan, BorderLayout.EAST);

        jpisibarang.add(Box.createVerticalStrut(5));
        jpisibarang.add(panel);

        total += item.getTotalHarga();
    }

    jltotal.setText("Rp " + total);
    jpisibarang.revalidate();
    jpisibarang.repaint();
}



public void tampilkanKeranjang(int idUser) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT k.idKeranjang, p.brand, k.ukuran, k.jumlah, p.harga " +
                     "FROM keranjang k JOIN produk p ON k.idProduk = p.idProduk " +
                     "WHERE k.idUser = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idUser);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // Tampilkan data keranjang (bisa ke JTable, JPanel, atau konsol)
            System.out.println("Produk: " + rs.getString("brand") +
                               ", Ukuran: " + rs.getString("ukuran") +
                               ", Jumlah: " + rs.getInt("jumlah") +
                               ", Harga: Rp " + rs.getDouble("harga"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void hapusDariKeranjang(int idKeranjang) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "DELETE FROM keranjang WHERE idKeranjang = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idKeranjang);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



private void tampilkanDetailBarang(Barang barang) {
    lbimg.setIcon(null);

    // Tampilkan gambar
    String imgPath = "src/main/java/" + barang.getImagePath();
    File file = new File(imgPath);
    if (file.exists()) {
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
        lbimg.setIcon(new ImageIcon(img));
    }

    txtnama.setText(barang.getNama());
    desk.setText(barang.getDeskripsi());
    lbhargabarang.setText("Rp. " + barang.getHarga());

    // Ambil ukuran dan stok
    List<String> ukuran = Arrays.asList(barang.getUkuran());
    JCheckBox[] allCB = { cbk1, cbk2, cbk3, cbk4 };
    JLabel[] stokLabels = { lbstok1, lbstok2, lbstok3, lbstok4 };

    try (Connection conn = DatabaseConnection.getConnection()) {
        int idProduk = BarangDB.cariIdProdukByNama(barang.getNama());

        for (int i = 0; i < allCB.length; i++) {
            if (i < ukuran.size()) {
                String size = ukuran.get(i);
                allCB[i].setText(size);
                allCB[i].setVisible(true);

                String sql = "SELECT stok FROM detail_produk WHERE idProduk = ? AND size = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idProduk);
                stmt.setString(2, size);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int stok = rs.getInt("stok");
                    stokLabels[i].setText(String.valueOf(stok));
                    stokLabels[i].setVisible(true);
                } else {
                    stokLabels[i].setVisible(false);
                }
            } else {
                allCB[i].setVisible(false);
                stokLabels[i].setVisible(false);
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mengambil stok: " + ex.getMessage());
    }

    // Tampilkan panel kategori
    CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "kategoribox");
}



private List<ItemKeranjang> daftarKeranjang = new ArrayList<>();
    public Aplikasi() {
    
    initComponents();
    
    
    
if (currentUserId == -1 || userRole == null || userRole.isEmpty()) {
    btnlogin.setVisible(true);     // belum login → tampilkan login
    btnlogout.setVisible(false);   // sembunyikan logout
} else {
    btnlogin.setVisible(false);    // sudah login → sembunyikan login
    btnlogout.setVisible(true);    // tampilkan logout
}

    txtpencarian.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang(txtpencarian.getText(), jplist);
    }
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang(txtpencarian.getText(), jplist);
    }
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang(txtpencarian.getText(), jplist);
    }
});


    btnback = new javax.swing.JButton("Back");
    btnback.setVisible(false);
    btnback.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnbackActionPerformed(evt);
        }
    });
    add(btnback);    
    if (!"admin".equals(userRole)) {
    btnaccount.setVisible(false); // sembunyikan tombol untuk user biasa
}

    jpisi.setLayout(new CardLayout());
    jpisi.add(jphome, "home");
    jpisi.add(jpkategori, "kategoribox");
    jpisi.add(jpcekout, "cekout");
    jpisi.add(jpkeranjang, "keranjang");
     jpisi.add(jpnotif, "notif");
    CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "home"); // TAMPILAN DEFAULT
    JPanel jplist = new JPanel();
    jplist.setLayout(new WrapLayout(FlowLayout.LEFT, 10, 10));
    jplist.setBackground(Color.BLACK);

JScrollPane scrollPane = new JScrollPane(jplist);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
jphome.add(scrollPane, BorderLayout.CENTER);

System.out.println("Jumlah komponen: " + jplist.getComponentCount());
System.out.println("Layout: " + jplist.getLayout().getClass().getSimpleName());


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcprofile = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        btncs = new javax.swing.JButton();
        btnaccount = new javax.swing.JButton();
        txtpencarian = new javax.swing.JTextField();
        jspisi = new javax.swing.JScrollPane();
        jpisi = new javax.swing.JPanel();
        jpkategori = new javax.swing.JPanel();
        jpbox1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        cbk1 = new javax.swing.JCheckBox();
        cbk2 = new javax.swing.JCheckBox();
        cbk3 = new javax.swing.JCheckBox();
        cbk4 = new javax.swing.JCheckBox();
        txtjumlah = new javax.swing.JTextField();
        btnbeli = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lbimg = new javax.swing.JLabel();
        txtnama = new javax.swing.JLabel();
        btntambah = new javax.swing.JButton();
        btnkurang = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        desk = new javax.swing.JTextArea();
        btnkeranjang = new javax.swing.JButton();
        lbhargabarang = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbstok1 = new javax.swing.JLabel();
        lbstok2 = new javax.swing.JLabel();
        lbstok3 = new javax.swing.JLabel();
        lbstok4 = new javax.swing.JLabel();
        jpcekout = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        transfer = new javax.swing.JLabel();
        norek = new javax.swing.JLabel();
        btnbayar = new javax.swing.JButton();
        btnuploadbukti = new javax.swing.JButton();
        logobank = new javax.swing.JLabel();
        jpcekotbarang = new javax.swing.JPanel();
        totalbarang = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        hargabarang = new javax.swing.JLabel();
        lbharga = new javax.swing.JLabel();
        namabarang = new javax.swing.JLabel();
        ukuran = new javax.swing.JLabel();
        lbtotal = new javax.swing.JLabel();
        lbnama = new javax.swing.JLabel();
        lbsize = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtfalamat = new javax.swing.JTextArea();
        jpkeranjang = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jppilihsemua = new javax.swing.JPanel();
        cbpilihsemua = new javax.swing.JCheckBox();
        jpresume = new javax.swing.JPanel();
        resumebelanja = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        btnbeli1 = new javax.swing.JButton();
        jltotal = new javax.swing.JLabel();
        jpisibarang = new javax.swing.JPanel();
        jphome = new javax.swing.JPanel();
        banner = new javax.swing.JLabel();
        jpnotif = new javax.swing.JPanel();
        lbnotif = new javax.swing.JLabel();
        jpisinotif = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnnotif = new javax.swing.JButton();
        btnback = new javax.swing.JButton();
        btnlogout = new javax.swing.JButton();
        btnchart = new javax.swing.JButton();
        btnlogin = new javax.swing.JButton();
        btngame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        btncs.setBackground(new java.awt.Color(0, 0, 0));
        btncs.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Downloads\\jawa\\JavaAsset\\CS.png")); // NOI18N
        btncs.setText("CS");
        btncs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncsActionPerformed(evt);
            }
        });

        btnaccount.setBackground(new java.awt.Color(0, 0, 0));
        btnaccount.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Documents\\NetBeansProjects\\com.mycompany_APK_Jual_ban_test\\src\\main\\java\\JavaAsset\\Profile.png")); // NOI18N
        btnaccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaccountActionPerformed(evt);
            }
        });

        txtpencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpencarianActionPerformed(evt);
            }
        });

        jspisi.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspisi.setToolTipText("");
        jspisi.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jpisi.setLayout(new java.awt.CardLayout());

        jpkategori.setBackground(new java.awt.Color(0, 0, 0));
        jpkategori.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jpkategoriAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jpbox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel25.setText("Kategori Pilihan");

        cbk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbk1ActionPerformed(evt);
            }
        });

        cbk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbk2ActionPerformed(evt);
            }
        });

        txtjumlah.setText("0");
        txtjumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahActionPerformed(evt);
            }
        });

        btnbeli.setText("Beli");
        btnbeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbeliActionPerformed(evt);
            }
        });

        jLabel6.setText("Jumlah");

        lbimg.setText("Gambar barang");
        lbimg.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lbimgAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtnama.setText("Nama barang");

        btntambah.setText("+");
        btntambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntambahActionPerformed(evt);
            }
        });

        btnkurang.setText("-");
        btnkurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkurangActionPerformed(evt);
            }
        });

        jLabel2.setText("Deskripsi");

        desk.setColumns(20);
        desk.setRows(5);
        jScrollPane1.setViewportView(desk);

        btnkeranjang.setText("Keranjang");
        btnkeranjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkeranjangActionPerformed(evt);
            }
        });

        lbhargabarang.setText("Rp.");

        jLabel3.setText("Stok Barang");

        lbstok1.setText("0");

        lbstok2.setText("0");

        lbstok3.setText("0");

        lbstok4.setText("0");

        javax.swing.GroupLayout jpbox1Layout = new javax.swing.GroupLayout(jpbox1);
        jpbox1.setLayout(jpbox1Layout);
        jpbox1Layout.setHorizontalGroup(
            jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpbox1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpbox1Layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpbox1Layout.createSequentialGroup()
                        .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpbox1Layout.createSequentialGroup()
                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbimg, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpbox1Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(lbhargabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jpbox1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(txtnama, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpbox1Layout.createSequentialGroup()
                                        .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpbox1Layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cbk1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cbk2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cbk3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cbk4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(55, 55, 55)
                                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lbstok4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lbstok3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lbstok2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lbstok1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnkurang, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jpbox1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(btntambah, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpbox1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnkeranjang)
                                .addGap(18, 18, 18)
                                .addComponent(btnbeli, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22))))
        );
        jpbox1Layout.setVerticalGroup(
            jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpbox1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel25)
                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpbox1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpbox1Layout.createSequentialGroup()
                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbstok1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbstok2)
                                .addGap(12, 12, 12)
                                .addComponent(lbstok3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbstok4))
                            .addComponent(lbimg, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpbox1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpbox1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnbeli)
                                    .addComponent(btnkeranjang)))
                            .addGroup(jpbox1Layout.createSequentialGroup()
                                .addComponent(cbk1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbk2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbk3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbk4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addGroup(jpbox1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtnama)
                                    .addComponent(btntambah)
                                    .addComponent(btnkurang))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbhargabarang)
                                .addGap(0, 23, Short.MAX_VALUE)))))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpkategoriLayout = new javax.swing.GroupLayout(jpkategori);
        jpkategori.setLayout(jpkategoriLayout);
        jpkategoriLayout.setHorizontalGroup(
            jpkategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpkategoriLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jpbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(489, Short.MAX_VALUE))
        );
        jpkategoriLayout.setVerticalGroup(
            jpkategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpkategoriLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1571, Short.MAX_VALUE))
        );

        jpisi.add(jpkategori, "card5");

        jpcekout.setBackground(new java.awt.Color(0, 0, 0));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Check Out");

        transfer.setText("Transfer");

        norek.setText("No rek ************************");

        btnbayar.setText("Konfimasi Pembayaran");
        btnbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbayarActionPerformed(evt);
            }
        });

        btnuploadbukti.setText("Upload Bukti pembayaran");
        btnuploadbukti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnuploadbuktiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnuploadbukti)
                            .addComponent(norek)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logobank, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(transfer)
                .addGap(21, 21, 21)
                .addComponent(logobank, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(norek)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnuploadbukti, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnbayar)
                .addGap(71, 71, 71))
        );

        totalbarang.setText("total barang");

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        hargabarang.setText("Harga barang");

        lbharga.setText("Rp.");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hargabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbharga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hargabarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbharga)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        namabarang.setText("Nama barang");

        ukuran.setText("Ukuran");

        lbtotal.setText("0");

        lbnama.setText("Nama");

        lbsize.setText("Size");

        javax.swing.GroupLayout jpcekotbarangLayout = new javax.swing.GroupLayout(jpcekotbarang);
        jpcekotbarang.setLayout(jpcekotbarangLayout);
        jpcekotbarangLayout.setHorizontalGroup(
            jpcekotbarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpcekotbarangLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jpcekotbarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbsize, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbnama, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ukuran, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jpcekotbarangLayout.setVerticalGroup(
            jpcekotbarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpcekotbarangLayout.createSequentialGroup()
                .addContainerGap(110, Short.MAX_VALUE)
                .addComponent(totalbarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbtotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbnama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ukuran)
                .addGap(4, 4, 4)
                .addComponent(lbsize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        txtfalamat.setColumns(20);
        txtfalamat.setRows(5);
        txtfalamat.setText("Nama :\nNo.HP :\nAlamat :\nId Transfer :\n");
        txtfalamat.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                txtfalamatAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane2.setViewportView(txtfalamat);

        javax.swing.GroupLayout jpcekoutLayout = new javax.swing.GroupLayout(jpcekout);
        jpcekout.setLayout(jpcekoutLayout);
        jpcekoutLayout.setHorizontalGroup(
            jpcekoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpcekoutLayout.createSequentialGroup()
                .addGroup(jpcekoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpcekoutLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpcekoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpcekotbarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpcekoutLayout.createSequentialGroup()
                        .addGap(395, 395, 395)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(311, Short.MAX_VALUE))
        );
        jpcekoutLayout.setVerticalGroup(
            jpcekoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpcekoutLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpcekoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpcekoutLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jpcekotbarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1526, Short.MAX_VALUE))
        );

        jpisi.add(jpcekout, "card7");

        jpkeranjang.setBackground(new java.awt.Color(255, 255, 255));
        jpkeranjang.setMaximumSize(new java.awt.Dimension(1161, 2002));
        jpkeranjang.setMinimumSize(new java.awt.Dimension(1161, 2002));
        jpkeranjang.setName(""); // NOI18N
        jpkeranjang.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jpkeranjangAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel7.setFont(new java.awt.Font("Humnst777 Blk BT", 0, 24)); // NOI18N
        jLabel7.setText("Keranjang");

        jppilihsemua.setBackground(new java.awt.Color(0, 0, 0));

        cbpilihsemua.setForeground(new java.awt.Color(255, 255, 255));
        cbpilihsemua.setText("Pilih Semua");
        cbpilihsemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbpilihsemuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jppilihsemuaLayout = new javax.swing.GroupLayout(jppilihsemua);
        jppilihsemua.setLayout(jppilihsemuaLayout);
        jppilihsemuaLayout.setHorizontalGroup(
            jppilihsemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jppilihsemuaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbpilihsemua, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jppilihsemuaLayout.setVerticalGroup(
            jppilihsemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jppilihsemuaLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(cbpilihsemua)
                .addContainerGap())
        );

        jpresume.setBackground(new java.awt.Color(0, 0, 0));

        resumebelanja.setForeground(new java.awt.Color(255, 255, 255));
        resumebelanja.setText("Resume Belanja");

        total.setForeground(new java.awt.Color(255, 255, 255));
        total.setText("Total");

        btnbeli1.setText("Beli");
        btnbeli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbeli1ActionPerformed(evt);
            }
        });

        jltotal.setForeground(new java.awt.Color(255, 255, 255));
        jltotal.setText("0");

        javax.swing.GroupLayout jpresumeLayout = new javax.swing.GroupLayout(jpresume);
        jpresume.setLayout(jpresumeLayout);
        jpresumeLayout.setHorizontalGroup(
            jpresumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpresumeLayout.createSequentialGroup()
                .addGroup(jpresumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpresumeLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpresumeLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(jpresumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnbeli1)
                            .addComponent(resumebelanja))))
                .addGap(23, 72, Short.MAX_VALUE))
        );
        jpresumeLayout.setVerticalGroup(
            jpresumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpresumeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(resumebelanja)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpresumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total)
                    .addComponent(jltotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(btnbeli1)
                .addGap(17, 17, 17))
        );

        jpisibarang.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jpisibarang.setMaximumSize(null);
        jpisibarang.setMinimumSize(new java.awt.Dimension(600, 630));
        jpisibarang.setPreferredSize(new java.awt.Dimension(600, 630));

        javax.swing.GroupLayout jpisibarangLayout = new javax.swing.GroupLayout(jpisibarang);
        jpisibarang.setLayout(jpisibarangLayout);
        jpisibarangLayout.setHorizontalGroup(
            jpisibarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jpisibarangLayout.setVerticalGroup(
            jpisibarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jpkeranjangLayout = new javax.swing.GroupLayout(jpkeranjang);
        jpkeranjang.setLayout(jpkeranjangLayout);
        jpkeranjangLayout.setHorizontalGroup(
            jpkeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpkeranjangLayout.createSequentialGroup()
                .addGroup(jpkeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpkeranjangLayout.createSequentialGroup()
                        .addGap(440, 440, 440)
                        .addComponent(jLabel7))
                    .addGroup(jpkeranjangLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jpkeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jppilihsemua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpisibarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpresume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(390, Short.MAX_VALUE))
        );
        jpkeranjangLayout.setVerticalGroup(
            jpkeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpkeranjangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(35, 35, 35)
                .addGroup(jpkeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpkeranjangLayout.createSequentialGroup()
                        .addComponent(jppilihsemua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpisibarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpresume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1270, Short.MAX_VALUE))
        );

        jpisi.add(jpkeranjang, "card3");

        jphome.setBackground(new java.awt.Color(0, 0, 0));
        jphome.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jphomeAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        banner.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Documents\\NetBeansProjects\\com.mycompany_APK_Jual_ban_test\\src\\main\\java\\devlop_apk_ban\\banner.png")); // NOI18N
        banner.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                bannerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jphomeLayout = new javax.swing.GroupLayout(jphome);
        jphome.setLayout(jphomeLayout);
        jphomeLayout.setHorizontalGroup(
            jphomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jphomeLayout.createSequentialGroup()
                .addComponent(banner, javax.swing.GroupLayout.PREFERRED_SIZE, 943, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 344, Short.MAX_VALUE))
        );
        jphomeLayout.setVerticalGroup(
            jphomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jphomeLayout.createSequentialGroup()
                .addComponent(banner)
                .addContainerGap(1777, Short.MAX_VALUE))
        );

        jpisi.add(jphome, "card2");

        jpnotif.setBackground(new java.awt.Color(255, 255, 255));

        lbnotif.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbnotif.setText("Notifikasi");

        javax.swing.GroupLayout jpisinotifLayout = new javax.swing.GroupLayout(jpisinotif);
        jpisinotif.setLayout(jpisinotifLayout);
        jpisinotifLayout.setHorizontalGroup(
            jpisinotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpisinotifLayout.setVerticalGroup(
            jpisinotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );

        jLabel1.setText("Notifikasih bahwa barang telah di setujui");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(517, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpnotifLayout = new javax.swing.GroupLayout(jpnotif);
        jpnotif.setLayout(jpnotifLayout);
        jpnotifLayout.setHorizontalGroup(
            jpnotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnotifLayout.createSequentialGroup()
                .addGroup(jpnotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnotifLayout.createSequentialGroup()
                        .addGap(415, 415, 415)
                        .addComponent(lbnotif))
                    .addGroup(jpnotifLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jpnotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpisinotif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(526, Short.MAX_VALUE))
        );
        jpnotifLayout.setVerticalGroup(
            jpnotifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnotifLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbnotif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpisinotif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1573, Short.MAX_VALUE))
        );

        jpisi.add(jpnotif, "card6");

        jspisi.setViewportView(jpisi);

        btnnotif.setBackground(new java.awt.Color(0, 0, 0));
        btnnotif.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Documents\\NetBeansProjects\\com.mycompany_APK_Jual_ban_test\\src\\main\\java\\JavaAsset\\notif_1.png")); // NOI18N
        btnnotif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnotifActionPerformed(evt);
            }
        });

        btnback.setBackground(new java.awt.Color(0, 0, 0));
        btnback.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Documents\\NetBeansProjects\\com.mycompany_APK_Jual_ban_test\\src\\main\\java\\devlop_apk_ban\\back.png")); // NOI18N
        btnback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbackActionPerformed(evt);
            }
        });

        btnlogout.setText("Log Out");
        btnlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogoutActionPerformed(evt);
            }
        });

        btnchart.setBackground(new java.awt.Color(0, 0, 0));
        btnchart.setIcon(new javax.swing.ImageIcon("C:\\Users\\acer\\Documents\\NetBeansProjects\\com.mycompany_APK_Jual_ban_test\\src\\main\\java\\devlop_apk_ban\\chart.png")); // NOI18N
        btnchart.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnchartAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnchart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnchartActionPerformed(evt);
            }
        });

        btnlogin.setText("Log In");
        btnlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloginActionPerformed(evt);
            }
        });

        btngame.setBackground(new java.awt.Color(0, 0, 0));
        btngame.setBorder(null);
        btngame.setBorderPainted(false);
        btngame.setContentAreaFilled(false);
        btngame.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btngame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jspisi, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btngame, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnback, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btncs, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnnotif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnchart, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnlogout)
                            .addComponent(btnlogin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnaccount, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnaccount)
                            .addComponent(btnnotif)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnback)
                                .addComponent(btncs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(txtpencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnlogout)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnlogin))
                            .addComponent(btnchart, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspisi, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(455, 455, 455)
                        .addComponent(btngame, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jspisi.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void bukaKategoriBerdasarkanIndex(int index) {
    CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "kategoribox"); // pindah panel
        JLabel lbimgkategori = null;

    GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimgkategori, index);
}


    private void txtpencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpencarianActionPerformed
       String keyword = txtpencarian.getText().trim().toLowerCase();

    // 🥚 Easter Egg Rahasia
    if (keyword.equals("21+21")) {
        JOptionPane.showMessageDialog(this, "Helloword 👋 You've discovered an easter egg!");
        return;
    }        // TODO add your handling code here:
    }//GEN-LAST:event_txtpencarianActionPerformed
    
    private void btncsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncsActionPerformed
     CS csForm = new CS();
    csForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Hanya menutup CS, bukan seluruh app
    csForm.setVisible(true);
    csForm.setLocationRelativeTo(null); // Tampil di tengah layar
   // TODO add your handling code here:
    }//GEN-LAST:event_btncsActionPerformed

    private void btnaccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaccountActionPerformed
     // Cek role sebelum membuka admintest
    if ("admin".equals(userRole)) {
        try {
            admintest at = new admintest();
            at.setVisible(true);
            at.pack();
            at.setLocationRelativeTo(null);
            at.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dispose(); // Tutup frame saat ini
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuka form admin: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Akses ditolak: hanya admin yang dapat mengakses halaman ini.");
    }
// TODO add your handling code here:
    }//GEN-LAST:event_btnaccountActionPerformed

    private void btnbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbayarActionPerformed
            if (buktiTransferFile == null) {
            JOptionPane.showMessageDialog(null, "Silakan upload bukti transfer terlebih dahulu!");
            return;
        }

        // Simpan file ke direktori project
        try {
            File target = new File("src/main/java/bukti_transfer/" + buktiTransferFile.getName());
            Files.copy(buktiTransferFile.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Simpan histori_transaksi + detail
            int idUser = Aplikasi.currentUserId;
            int totalHarga = hitungTotal(); // hitung dari list item

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "");
            conn.setAutoCommit(false);

            // Simpan histori_transaksi
            String sqlTrans = "INSERT INTO histori_transaksi (tanggal, total, idUser) VALUES (NOW(), ?, ?)";
            PreparedStatement psTrans = conn.prepareStatement(sqlTrans, Statement.RETURN_GENERATED_KEYS);
            psTrans.setInt(1, totalHarga);
            psTrans.setInt(2, idUser);
            psTrans.executeUpdate();

            ResultSet rs = psTrans.getGeneratedKeys();
            if (rs.next()) {
                int idTransaksi = rs.getInt(1);

                // Simpan histori_detail
                for (ItemKeranjang item : daftarKeranjang) {
                    int idProduk = BarangDB.cariIdProdukByNama(item.getNama());
                    String sqlDetail = "INSERT INTO histori_detail (id_transaksi, nama_barang, ukuran, jumlah, harga, idProduk) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                    psDetail.setInt(1, idTransaksi);
                    psDetail.setString(2, item.getNama());
                    psDetail.setString(3, item.getUkuran());
                    psDetail.setInt(4, item.getJumlah());
                    psDetail.setInt(5, item.getHarga());
                    psDetail.setInt(6, idProduk);
                    psDetail.executeUpdate();
                }

                // Simpan bukti_transfer (ke tabel baru atau ke histori_transaksi, jika ditambahkan kolom path_bukti)
                String pathBukti = "bukti_transfer/" + buktiTransferFile.getName();
                PreparedStatement psBukti = conn.prepareStatement("UPDATE histori_transaksi SET path_bukti = ? WHERE id_transaksi = ?");
                psBukti.setString(1, pathBukti);
                psBukti.setInt(2, idTransaksi);
                psBukti.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Transaksi berhasil dan sedang menunggu validasi admin.");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menyimpan transaksi: " + ex.getMessage());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnbayarActionPerformed
    private boolean filterApplied = false; 
    private void txtfalamatAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtfalamatAncestorAdded

    }//GEN-LAST:event_txtfalamatAncestorAdded

    private void btnnotifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnotifActionPerformed

CardLayout cl = (CardLayout) jpisi.getLayout();
cl.show(jpisi, "notif");
    if (currentUserId == -1 || userRole == null || userRole.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan login terlebih dahulu!");
        this.dispose(); // Tutup form jika belum login
        new Loginn().setVisible(true); // Arahkan ke login
        return;
    }
jpisinotif.removeAll(); // Bersihkan isi panel
jpisinotif.setLayout(new BoxLayout(jpisinotif, BoxLayout.Y_AXIS)); // Atur layout

try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
    String sql = "SELECT pesan, tanggal FROM notifikasi WHERE idUser = ? ORDER BY tanggal DESC";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, Aplikasi.currentUserId);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        String pesan = rs.getString("pesan");
        String tanggal = rs.getString("tanggal");

        JPanel panelNotif = new JPanel(new BorderLayout());
        panelNotif.setBackground(new Color(230, 230, 230));
        panelNotif.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblPesan = new JLabel("📢 " + pesan + " (" + tanggal + ")");
        panelNotif.add(lblPesan, BorderLayout.CENTER);

        jpisinotif.add(panelNotif);
        jpisinotif.add(Box.createVerticalStrut(5)); // Spasi antar notifikasi
    }

    jpisinotif.revalidate();
    jpisinotif.repaint();

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "Gagal memuat notifikasi: " + e.getMessage());
}

            // TODO add your handling code here:
    }//GEN-LAST:event_btnnotifActionPerformed

    private void txtjumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjumlahActionPerformed

    private void btnkurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkurangActionPerformed
            int jumlah = Integer.parseInt(txtjumlah.getText());
        if (jumlah > 0) {
            jumlah--;
            txtjumlah.setText(String.valueOf(jumlah)); }     // TODO add your handling code here:
    }//GEN-LAST:event_btnkurangActionPerformed

    private void cbk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbk2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbk2ActionPerformed

    private void cbk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbk1ActionPerformed
      // TODO add your handling code here:
    }//GEN-LAST:event_cbk1ActionPerformed

    private void lbimgAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lbimgAncestorAdded
    lbimg.setPreferredSize(new Dimension(150, 150));
        // TODO add your handling code here:
    }//GEN-LAST:event_lbimgAncestorAdded

    private void jphomeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jphomeAncestorAdded
/*    jphome.removeAll(); // Bersihkan isi panel
    
    // Atur layout utama untuk jphome
    jphome.setLayout(new BorderLayout());

    // Tambahkan banner di atas
    jphome.add(banner, BorderLayout.NORTH);

    // Buat panel isi produk dengan WrapLayout
    JPanel jplist = new JPanel();
    jplist.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20)); // 20px jarak antar item
    jplist.setBackground(Color.BLACK);

    // Tambahkan scroll pane untuk jplist
    JScrollPane scrollPane = new JScrollPane(jplist);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null); // Opsional agar tidak ada border putih

    // Tambahkan scroll pane ke panel utama
    jphome.add(scrollPane, BorderLayout.CENTER);

    // Tampilkan barang ke jplist
    tampilkanBarangKePanel(jplist);

    //Refresh tampilan
    jphome.revalidate();
    jphome.repaint(); */

        jphome.removeAll();
    jphome.setLayout(new BorderLayout());
    jphome.add(banner, BorderLayout.NORTH);

    jplist = new JPanel();
    jplist.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20));
    jplist.setBackground(Color.BLACK);

    JScrollPane scrollPane = new JScrollPane(jplist);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);

    jphome.add(scrollPane, BorderLayout.CENTER);
    tampilkanBarangKePanel(jplist);
    jphome.revalidate();
    jphome.repaint();
      // TODO add your handling code here:
    }//GEN-LAST:event_jphomeAncestorAdded

    private void bannerAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_bannerAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_bannerAncestorAdded

    private void jpkategoriAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jpkategoriAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jpkategoriAncestorAdded

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
         int jumlah = Integer.parseInt(txtjumlah.getText());
        jumlah++;
        txtjumlah.setText(String.valueOf(jumlah));            // TODO add your handling code here:
    }//GEN-LAST:event_btntambahActionPerformed

    private void btnbeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbeliActionPerformed
        if (Aplikasi.userRole == null || Aplikasi.userRole.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan login terlebih dahulu untuk membeli produk.");
        new Loginn().setVisible(true);
        return;
    }

    String namaProduk = txtnama.getText().trim();
    String ukuranDipilih = null;
    if (cbk1.isSelected()) ukuranDipilih = cbk1.getText();
    else if (cbk2.isSelected()) ukuranDipilih = cbk2.getText();
    else if (cbk3.isSelected()) ukuranDipilih = cbk3.getText();
    else if (cbk4.isSelected()) ukuranDipilih = cbk4.getText();

    if (ukuranDipilih == null) {
        JOptionPane.showMessageDialog(null, "Pilih salah satu ukuran terlebih dahulu.");
        return;
    }

    int jumlah;
    try {
        jumlah = Integer.parseInt(txtjumlah.getText());
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Jumlah tidak valid.");
        return;
    }

    int harga;
    try {
        harga = Integer.parseInt(lbhargabarang.getText().replaceAll("[^\\d]", ""));
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Harga produk tidak valid.");
        return;
    }

    int total = jumlah * harga;

    try (Connection conn = DatabaseConnection.getConnection()) {
        int idProduk = BarangDB.cariIdProdukByNama(namaProduk);
        if (idProduk == -1) {
            JOptionPane.showMessageDialog(null, "Produk tidak ditemukan di database.");
            return;
        }

        // Simpan ke histori_transaksi
        String sqlHistori = "INSERT INTO histori_transaksi (tanggal, total, idUser) VALUES (?, ?, ?)";
        PreparedStatement stmtHistori = conn.prepareStatement(sqlHistori, Statement.RETURN_GENERATED_KEYS);
        stmtHistori.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        stmtHistori.setInt(2, total);
        stmtHistori.setInt(3, Aplikasi.currentUserId);
        stmtHistori.executeUpdate();

        int idTransaksi = -1;
        ResultSet rsHistori = stmtHistori.getGeneratedKeys();
        if (rsHistori.next()) {
            idTransaksi = rsHistori.getInt(1);
        }

        // Simpan ke histori_detail
        String sqlDetail = "INSERT INTO histori_detail (id_transaksi, idProduk, nama_barang, ukuran, jumlah, harga) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);
        stmtDetail.setInt(1, idTransaksi);
        stmtDetail.setInt(2, idProduk);
        stmtDetail.setString(3, namaProduk);
        stmtDetail.setString(4, ukuranDipilih);
        stmtDetail.setInt(5, jumlah);
        stmtDetail.setInt(6, harga);
        stmtDetail.executeUpdate();

        // Simpan ke tabel pembelian
        String kodeUnik = "PB-" + System.currentTimeMillis();
        String sqlPembelian = "INSERT INTO pembelian (idProduk, kodePembelian, jumlah, subTotal, totalHarga, idAlamat) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmtPembelian = conn.prepareStatement(sqlPembelian);
        stmtPembelian.setInt(1, idProduk);
        stmtPembelian.setString(2, kodeUnik);
        stmtPembelian.setInt(3, jumlah);
        stmtPembelian.setDouble(4, harga);
        stmtPembelian.setDouble(5, total);
        stmtPembelian.setInt(6, 0); // idAlamat = 0 (sementara)
        stmtPembelian.executeUpdate();

        JOptionPane.showMessageDialog(null, "Produk berhasil ditambahkan ke checkout.");

        // Tampilkan ke panel checkout
        lbnama.setText(namaProduk);
        lbsize.setText(ukuranDipilih);
        lbtotal.setText(String.valueOf(jumlah));
        lbharga.setText("Rp " + harga);

        CardLayout cl = (CardLayout) jpisi.getLayout();
        cl.show(jpisi, "cekout");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal melakukan pembelian: " + e.getMessage());
    }// TODO add your handling code here:
    }//GEN-LAST:event_btnbeliActionPerformed

    private void cbpilihsemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbpilihsemuaActionPerformed
    boolean pilihSemua = cbpilihsemua.isSelected();
    for (JCheckBox cb : daftarCheckbox) {
        cb.setSelected(pilihSemua);
    }       // TODO add your handling code here:
    }//GEN-LAST:event_cbpilihsemuaActionPerformed

    private void btnbeli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbeli1ActionPerformed

    if (Aplikasi.userRole == null || Aplikasi.userRole.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan login terlebih dahulu untuk melakukan pembelian.");
        Loginn loginFrame = new Loginn();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return;
    }

    if (daftarKeranjang.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Keranjang masih kosong!");
        return;
    }

    updateTampilanKeranjang();

    try (Connection conn = DatabaseConnection.getConnection()) {
        String alamatLengkap = txtfalamat.getText();
        int idAlamat = -1;

        String sqlAlamat = "INSERT INTO alamat_user (idUser, alamat) VALUES (?, ?)";
        PreparedStatement stmtAlamat = conn.prepareStatement(sqlAlamat, Statement.RETURN_GENERATED_KEYS);
        stmtAlamat.setInt(1, Aplikasi.currentUserId);
        stmtAlamat.setString(2, alamatLengkap);
        stmtAlamat.executeUpdate();

        ResultSet rsAlamat = stmtAlamat.getGeneratedKeys();
        if (rsAlamat.next()) {
            idAlamat = rsAlamat.getInt(1);
        }

        String sqlKeranjang = "SELECT k.idProduk, p.brand, k.ukuran, k.jumlah, d.harga " +
                              "FROM keranjang k JOIN produk p ON k.idProduk = p.idProduk " +
                              "JOIN detail_produk d ON d.idProduk = k.idProduk AND d.size = k.ukuran " +
                              "WHERE k.idUser = ?";
        PreparedStatement stmtKeranjang = conn.prepareStatement(sqlKeranjang);
        stmtKeranjang.setInt(1, Aplikasi.currentUserId);
        ResultSet rs = stmtKeranjang.executeQuery();

        while (rs.next()) {
            int idProduk = rs.getInt("idProduk");
            String nama = rs.getString("brand");
            String ukuran = rs.getString("ukuran");
            int jumlah = rs.getInt("jumlah");
            int harga = rs.getInt("harga");
            int total = jumlah * harga;

            String kodeUnik = "PB-" + System.currentTimeMillis();

            String sqlPembelian = "INSERT INTO pembelian (idProduk, kodePembelian, jumlah, subTotal, totalHarga, idAlamat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtPembelian = conn.prepareStatement(sqlPembelian, Statement.RETURN_GENERATED_KEYS);
            stmtPembelian.setInt(1, idProduk);
            stmtPembelian.setString(2, kodeUnik);
            stmtPembelian.setInt(3, jumlah);
            stmtPembelian.setDouble(4, harga);
            stmtPembelian.setDouble(5, total);
            stmtPembelian.setInt(6, idAlamat);
            stmtPembelian.executeUpdate();

            ResultSet rsPembelian = stmtPembelian.getGeneratedKeys();
            if (rsPembelian.next()) {
                int idPembelian = rsPembelian.getInt(1);
                String sqlDetailPembelian = "INSERT INTO detail_pembelian (idPembelian, idAlamat, jumlah, subTotal, totalHarga) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmtDetail = conn.prepareStatement(sqlDetailPembelian);
                stmtDetail.setInt(1, idPembelian);
                stmtDetail.setInt(2, idAlamat);
                stmtDetail.setInt(3, jumlah);
                stmtDetail.setDouble(4, harga);
                stmtDetail.setDouble(5, total);
                stmtDetail.executeUpdate();
            }
        }

        String sqlClear = "DELETE FROM keranjang WHERE idUser = ?";
        PreparedStatement stmtClear = conn.prepareStatement(sqlClear);
        stmtClear.setInt(1, Aplikasi.currentUserId);
        stmtClear.executeUpdate();

        daftarKeranjang.clear();
        updateTampilanKeranjang();

        JOptionPane.showMessageDialog(null, "Checkout berhasil disimpan ke database!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan checkout: " + e.getMessage());
    }

    CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "cekout");
       // TODO add your handling code here:
    }//GEN-LAST:event_btnbeli1ActionPerformed

    private void btnkeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkeranjangActionPerformed
         if (currentUserId == -1 || userRole == null || userRole.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan login terlebih dahulu.");
        return;
    }

    String namaProduk = txtnama.getText().trim();
    String ukuranDipilih = null;
    if (cbk1.isSelected()) ukuranDipilih = cbk1.getText();
    else if (cbk2.isSelected()) ukuranDipilih = cbk2.getText();
    else if (cbk3.isSelected()) ukuranDipilih = cbk3.getText();
    else if (cbk4.isSelected()) ukuranDipilih = cbk4.getText();

    if (ukuranDipilih == null) {
        JOptionPane.showMessageDialog(null, "Pilih salah satu ukuran terlebih dahulu.");
        return;
    }

    int jumlah = 0;
    try {
        jumlah = Integer.parseInt(txtjumlah.getText());
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Jumlah tidak valid.");
        return;
    }

    try (Connection conn = DatabaseConnection.getConnection()) {
        int idProduk = BarangDB.cariIdProdukByNama(namaProduk);
        if (idProduk == -1) {
            JOptionPane.showMessageDialog(null, "Produk tidak ditemukan.");
            return;
        }

        // Cek apakah item sudah ada di keranjang
        String sqlCheck = "SELECT * FROM keranjang WHERE idUser = ? AND idProduk = ? AND ukuran = ?";
        PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
        stmtCheck.setInt(1, currentUserId);
        stmtCheck.setInt(2, idProduk);
        stmtCheck.setString(3, ukuranDipilih);
        ResultSet rs = stmtCheck.executeQuery();

        if (rs.next()) {
            // Update jumlah jika sudah ada
            String sqlUpdate = "UPDATE keranjang SET jumlah = jumlah + ? WHERE idUser = ? AND idProduk = ? AND ukuran = ?";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtUpdate.setInt(1, jumlah);
            stmtUpdate.setInt(2, currentUserId);
            stmtUpdate.setInt(3, idProduk);
            stmtUpdate.setString(4, ukuranDipilih);
            stmtUpdate.executeUpdate();
        } else {
            // Insert baru jika belum ada
            String sqlInsert = "INSERT INTO keranjang (idUser, idProduk, ukuran, jumlah) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
            stmtInsert.setInt(1, currentUserId);
            stmtInsert.setInt(2, idProduk);
            stmtInsert.setString(3, ukuranDipilih);
            stmtInsert.setInt(4, jumlah);
            stmtInsert.executeUpdate();
        }

        JOptionPane.showMessageDialog(null, "Barang berhasil ditambahkan ke keranjang.");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menambahkan ke keranjang: " + e.getMessage());
    }          // isi jpisibarang

  
// TODO add your handling code here:
    }//GEN-LAST:event_btnkeranjangActionPerformed

    private void jpkeranjangAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jpkeranjangAncestorAdded
      // TODO add your handling code here:
    }//GEN-LAST:event_jpkeranjangAncestorAdded

    private void btnchartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnchartActionPerformed
       if (currentUserId == -1 || userRole == null || userRole.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan login terlebih dahulu!");
        this.dispose();
        new Loginn().setVisible(true);
        return;
    }

    modeHistori = false; // mode histori dimatikan karena ini keranjang aktif
    jpisibarang.removeAll();
    jpisibarang.setLayout(new BoxLayout(jpisibarang, BoxLayout.Y_AXIS));
    jpisibarang.setBackground(Color.WHITE);

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = """
    SELECT k.*, p.brand, p.harga
    FROM keranjang k
    JOIN produk p ON k.idProduk = p.idProduk
    WHERE k.idUser = ?
    ORDER BY k.tanggal_ditambahkan DESC
""";


        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Aplikasi.currentUserId);

        ResultSet rs = stmt.executeQuery();

        daftarKeranjang.clear();

        while (rs.next()) {
            int idProduk = rs.getInt("idProduk");
            String nama = rs.getString("brand");
            String ukuran = rs.getString("ukuran");
            int jumlah = rs.getInt("jumlah");
            int harga = rs.getInt("harga");

            daftarKeranjang.add(new ItemKeranjang(idProduk, nama, ukuran, jumlah, harga));
        }

        updateTampilanKeranjang();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memuat keranjang: " + e.getMessage());
        return;
    }

    jltotal.setText("Rp 0");

    CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "keranjang");
// TODO add your handling code here:
    }//GEN-LAST:event_btnchartActionPerformed

    private void btnchartAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnchartAncestorAdded

// TODO add your handling code here:
    }//GEN-LAST:event_btnchartAncestorAdded

    private void btnuploadbuktiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnuploadbuktiActionPerformed
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            buktiTransferFile = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, "Bukti transfer dipilih: " + buktiTransferFile.getName());
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnuploadbuktiActionPerformed

    private void btnbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackActionPerformed
        CardLayout cl = (CardLayout) jpisi.getLayout();
    cl.show(jpisi, "home");
    btnback.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_btnbackActionPerformed

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
       logout();        // TODO add your handling code here:
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloginActionPerformed
        Loginn loginForm = new Loginn();
    loginForm.setVisible(true);
    this.dispose(); // tutup halaman utama agar tidak tumpang tindih        // TODO add your handling code here:
    }//GEN-LAST:event_btnloginActionPerformed

    private void btngameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngameActionPerformed
    
    JFrame gameFrame = new JFrame("⚔️ Game Turn-Based: Hero vs Boss");
    gameFrame.setSize(420, 350);
    gameFrame.setLocationRelativeTo(null);
    gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    JLabel lblLevel = new JLabel("Boss Lv. 1");
    JLabel lblPlayerHP = new JLabel("❤️ Player HP: 100");
    JLabel lblMonsterHP = new JLabel("👹 Boss HP: 100");
    JLabel lblLog = new JLabel("Ayo mulai pertempuran!");
    lblLog.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton btnAttack = new JButton("🗡️ Serang");
    JButton btnHeal = new JButton("💊 Heal");
    JButton btnRestart = new JButton("🔄 Ulangi");
    JButton btnNextBoss = new JButton("➡️ Lanjut Boss");
    btnRestart.setVisible(false);
    btnNextBoss.setVisible(false);

    final int[] level = {1};
    final int[] maxPlayerHP = {100};
    final int[] playerHP = {100};
    final int[] monsterHP = {100};
    final int[] monsterMaxHP = {100};
    final Random rand = new Random();

    // Update label
    ActionListener updateStatus = e -> {
        lblPlayerHP.setText("❤️ Player HP: " + playerHP[0]);
        lblMonsterHP.setText("👹 Boss HP: " + monsterHP[0]);
        lblLevel.setText("Boss Lv. " + level[0]);

        if (playerHP[0] <= 0) {
            lblLog.setText("💀 Kamu kalah!");
            btnAttack.setEnabled(false);
            btnHeal.setEnabled(false);
            btnNextBoss.setVisible(false);
            btnRestart.setVisible(true);
        } else if (monsterHP[0] <= 0) {
            lblLog.setText("🎉 Kamu kalahkan Boss Lv. " + level[0]);
            btnAttack.setEnabled(false);
            btnHeal.setEnabled(false);
            btnNextBoss.setVisible(true);
            btnRestart.setVisible(true);
        }
    };

    btnAttack.addActionListener(e -> {
        
        int damage = rand.nextInt(20) + 10;
        monsterHP[0] -= damage;
        lblLog.setText("Kamu menyerang boss - " + damage + " HP!");

        if (monsterHP[0] > 0) {
            int monsterDamage = rand.nextInt(15 + level[0] * 2) + 5;
            playerHP[0] -= monsterDamage;
            lblLog.setText(lblLog.getText() + " Boss balas -" + monsterDamage + " HP!");
        }

        updateStatus.actionPerformed(null);
    });

    btnHeal.addActionListener(e -> {
        int heal = rand.nextInt(10 + level[0]) + 10;
        playerHP[0] = Math.min(maxPlayerHP[0], playerHP[0] + heal);
        lblLog.setText("Kamu menyembuhkan +" + heal + " HP.");

        int monsterDamage = rand.nextInt(10 + level[0] * 2) + 5;
        playerHP[0] -= monsterDamage;
        lblLog.setText(lblLog.getText() + " Boss menyerang -" + monsterDamage + " HP!");

        updateStatus.actionPerformed(null);
    });

    btnRestart.addActionListener(e -> {
        level[0] = 1;
        maxPlayerHP[0] = 100;
        playerHP[0] = maxPlayerHP[0];
        monsterHP[0] = 100;
        monsterMaxHP[0] = 100;
        btnAttack.setEnabled(true);
        btnHeal.setEnabled(true);
        btnRestart.setVisible(false);
        btnNextBoss.setVisible(false);
        lblLog.setText("Ayo mulai lagi dari Boss Lv. 1!");
        updateStatus.actionPerformed(null);
    });

btnNextBoss.addActionListener(e -> {
    level[0]++;
    
    // Buff player lebih besar tiap 5 level
    if (level[0] % 5 == 0) {
        maxPlayerHP[0] += 25;
        lblLog.setText("💪 Kamu merasa lebih kuat! (+25 HP)");
    } else {
        maxPlayerHP[0] += 10;
        lblLog.setText("Naik ke Lv. " + level[0] + "! (+10 HP)");
    }

    playerHP[0] = maxPlayerHP[0];

    // Boss makin kuat
    monsterMaxHP[0] = 100 + (level[0] * 40);
    monsterHP[0] = monsterMaxHP[0];

    btnAttack.setEnabled(true);
    btnHeal.setEnabled(true);
    btnNextBoss.setVisible(false);
    btnRestart.setVisible(false);

    lblLog.setText(lblLog.getText() + " 👹 Boss Lv. " + level[0] + " datang!");
    updateStatus.actionPerformed(null);
});


    panel.add(lblLevel);
    panel.add(lblPlayerHP);
    panel.add(lblMonsterHP);
    panel.add(Box.createVerticalStrut(10));
    panel.add(lblLog);
    panel.add(Box.createVerticalStrut(10));
    panel.add(btnAttack);
    panel.add(btnHeal);
    panel.add(btnNextBoss);
    panel.add(btnRestart);

    gameFrame.add(panel);
    gameFrame.setVisible(true);
   // TODO add your handling code here:
    }//GEN-LAST:event_btngameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Aplikasi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel banner;
    private javax.swing.JButton btnaccount;
    private javax.swing.JButton btnback;
    private javax.swing.JButton btnbayar;
    private javax.swing.JButton btnbeli;
    private javax.swing.JButton btnbeli1;
    private javax.swing.JButton btnchart;
    private javax.swing.JButton btncs;
    private javax.swing.JButton btngame;
    private javax.swing.JButton btnkeranjang;
    private javax.swing.JButton btnkurang;
    private javax.swing.JButton btnlogin;
    private javax.swing.JButton btnlogout;
    private javax.swing.JButton btnnotif;
    private javax.swing.JButton btntambah;
    private javax.swing.JButton btnuploadbukti;
    private javax.swing.JCheckBox cbk1;
    private javax.swing.JCheckBox cbk2;
    private javax.swing.JCheckBox cbk3;
    private javax.swing.JCheckBox cbk4;
    private javax.swing.JCheckBox cbpilihsemua;
    private javax.swing.JTextArea desk;
    private javax.swing.JFileChooser fcprofile;
    private javax.swing.JLabel hargabarang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jltotal;
    private javax.swing.JPanel jpbox1;
    private javax.swing.JPanel jpcekotbarang;
    private javax.swing.JPanel jpcekout;
    private javax.swing.JPanel jphome;
    private javax.swing.JPanel jpisi;
    private javax.swing.JPanel jpisibarang;
    private javax.swing.JPanel jpisinotif;
    private javax.swing.JPanel jpkategori;
    private javax.swing.JPanel jpkeranjang;
    private javax.swing.JPanel jpnotif;
    private javax.swing.JPanel jppilihsemua;
    private javax.swing.JPanel jpresume;
    private javax.swing.JScrollPane jspisi;
    private javax.swing.JLabel lbharga;
    private javax.swing.JLabel lbhargabarang;
    private javax.swing.JLabel lbimg;
    private javax.swing.JLabel lbnama;
    private javax.swing.JLabel lbnotif;
    private javax.swing.JLabel lbsize;
    private javax.swing.JLabel lbstok1;
    private javax.swing.JLabel lbstok2;
    private javax.swing.JLabel lbstok3;
    private javax.swing.JLabel lbstok4;
    private javax.swing.JLabel lbtotal;
    private javax.swing.JLabel logobank;
    private javax.swing.JLabel namabarang;
    private javax.swing.JLabel norek;
    private javax.swing.JLabel resumebelanja;
    private javax.swing.JLabel total;
    private javax.swing.JLabel totalbarang;
    private javax.swing.JLabel transfer;
    private javax.swing.JTextArea txtfalamat;
    private javax.swing.JTextField txtjumlah;
    private javax.swing.JLabel txtnama;
    private javax.swing.JTextField txtpencarian;
    private javax.swing.JLabel ukuran;
    // End of variables declaration//GEN-END:variables

}
