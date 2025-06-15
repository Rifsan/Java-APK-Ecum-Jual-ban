/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package devlop_apk_ban;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author acer
 */
public class admintest extends javax.swing.JFrame {
    
    private String imagePath = "";  // Track the image path
    
private void updateCbdata() {
    cbdata.removeAllItems();
    for (String nama : BarangDB.getSemuaNamaProduk()) {
        cbdata.addItem(nama);
    }
}

private void isiUkuranUntukComboBox(String namaProduk) {
    cbukuranstok.removeAllItems();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT d.size FROM produk p JOIN detail_produk d ON p.idProduk = d.idProduk WHERE p.brand = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, namaProduk);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            cbukuranstok.addItem(rs.getString("size"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memuat ukuran ke comboBox: " + ex.getMessage());
    }
}


   
    
private void isiComboTransaksiMenunggu() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT id_transaksi FROM histori_transaksi WHERE status = 'Menunggu'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        jComboBox1.removeAllItems();
        while (rs.next()) {
            jComboBox1.addItem(String.valueOf(rs.getInt("id_transaksi")));
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mengambil transaksi: " + ex.getMessage());
    }
}

private void tampilkanDetailTransaksi(int idTransaksi) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT * FROM histori_detail WHERE id_transaksi = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idTransaksi);
        ResultSet rs = stmt.executeQuery();

        StringBuilder detail = new StringBuilder();
        while (rs.next()) {
            detail.append("Nama: ").append(rs.getString("nama_barang"))
                  .append(", Ukuran: ").append(rs.getString("ukuran"))
                  .append(", Jumlah: ").append(rs.getInt("jumlah"))
                  .append(", Harga: Rp ").append(rs.getInt("harga")).append("\n");
        }
        jTextArea1.setText(detail.toString());

        sql = "SELECT path_bukti FROM histori_transaksi WHERE id_transaksi = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idTransaksi);
        rs = stmt.executeQuery();
        if (rs.next()) {
            String path = rs.getString("path_bukti");
            File imgFile = new File("src/main/java/" + path);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                lbimg2.setIcon(new ImageIcon(img));
            } else {
                lbimg2.setIcon(null);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memuat detail transaksi: " + e.getMessage());
    }
}

private void konfirmasiPesanan() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String selected = (String) jComboBox1.getSelectedItem();
        if (selected == null) return;

        int idTransaksi = Integer.parseInt(selected);

        String sql = "UPDATE histori_transaksi SET status = 'Disetujui' WHERE id_transaksi = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idTransaksi);
        stmt.executeUpdate();

        int idUser = -1;
        sql = "SELECT idUser FROM histori_transaksi WHERE id_transaksi = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idTransaksi);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            idUser = rs.getInt("idUser");
        }

        if (idUser != -1) {
            sql = "INSERT INTO notifikasi (idUser, pesan) VALUES (?, ?)";
            PreparedStatement notifStmt = conn.prepareStatement(sql);
            notifStmt.setInt(1, idUser);
            notifStmt.setString(2, "Transaksi dengan ID " + idTransaksi + " telah disetujui oleh admin.");
            notifStmt.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Pesanan telah disetujui dan notifikasi dikirim.");
        isiComboTransaksiMenunggu();
        jTextArea1.setText("");
        lbimg2.setIcon(null);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal konfirmasi: " + ex.getMessage());
    }
}


private void tambahStokUkuran(String namaBarang, String ukuran, int jumlah) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "UPDATE detail_produk SET stok = stok + ? WHERE idProduk = (SELECT idProduk FROM produk WHERE brand = ?) AND size = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, jumlah);
        stmt.setString(2, namaBarang);
        stmt.setString(3, ukuran);
        int rows = stmt.executeUpdate();

        if (rows > 0) {
            System.out.println("Stok ditambahkan untuk ukuran: " + ukuran);
        } else {
            System.out.println("Ukuran tidak ditemukan atau produk tidak cocok: " + ukuran);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambah stok: " + ex.getMessage());
    }
}

private void tampilkanDetailBarangYangDitambah() {
    String selected = (String) cbdata.getSelectedItem();
    if (selected == null || selected.isEmpty()) return;

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT p.brand, d.size, d.stok FROM produk p JOIN detail_produk d ON p.idProduk = d.idProduk WHERE p.brand = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, selected);
        ResultSet rs = stmt.executeQuery();

        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append("Produk: ").append(rs.getString("brand"))
              .append(", Ukuran: ").append(rs.getString("size"))
              .append(", Stok: ").append(rs.getInt("stok"))
              .append("\n");
        }

        tsdetailbarang.setText(sb.toString());

        isiUkuranUntukComboBox(selected); // Panggil untuk isi cbukuranstok

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menampilkan detail barang: " + ex.getMessage());
    }
}







// Tambahkan pemanggilan berikut di konstruktor admintest
// isiComboTransaksiMenunggu();
// jComboBox1.addActionListener(e -> {
//     if (jComboBox1.getSelectedItem() != null) {
//         tampilkanDetailTransaksi(Integer.parseInt((String) jComboBox1.getSelectedItem()));
//     }
// });
// jButton4.addActionListener(e -> konfirmasiPesanan());

    // In the image upload button handler:
 
    /**
     * Creates new form admintest
     */
    public admintest() {
        initComponents();
        isiComboTransaksiMenunggu();
jComboBox1.addActionListener(e -> {
    if (jComboBox1.getSelectedItem() != null) {
        tampilkanDetailTransaksi(Integer.parseInt((String) jComboBox1.getSelectedItem()));
    }
});
jButton4.addActionListener(e -> konfirmasiPesanan());

                List<String> semuaNama = BarangDB.getSemuaNamaProduk();
    cbdata.removeAllItems();
    for (String nama : semuaNama) {
    cbdata.addItem(nama);
    }

        // Inside initComponents()
    // ... existing code ...
        // Add these lines to initialize txtDeleteId
 

// Add to Layout (adjust based on your layout manager)
javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
getContentPane().setLayout(layout);


        btnhapus = new javax.swing.JButton();

     // Pastikan memanggil method yang benar
//GambarManager.tampilkanSemuaGambarKeLabel(new JLabel[] { lbimg1, lbimg2, lbimg3, lbimg4 });
// lbimg1 dari form Aplikasi
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtfdes1 = new javax.swing.JTextArea();
        btninput = new javax.swing.JButton();
        imgdetail = new javax.swing.JLabel();
        txtnama1 = new javax.swing.JTextField();
        txt1u1 = new javax.swing.JTextField();
        txt1u2 = new javax.swing.JTextField();
        txt1u3 = new javax.swing.JTextField();
        txt1u4 = new javax.swing.JTextField();
        btnmenu = new javax.swing.JButton();
        txtharga1 = new javax.swing.JTextField();
        txtkonfirmasi1 = new javax.swing.JButton();
        btnhapus = new javax.swing.JButton();
        cbdata = new javax.swing.JComboBox<>();
        btnreset = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbimg2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tsdetailbarang = new javax.swing.JTextArea();
        btnkurang = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtjumlah = new javax.swing.JTextField();
        btntambah = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lbtambahbarang = new javax.swing.JLabel();
        lbdetailbarangditambah = new javax.swing.JLabel();
        lbbarangdipesan = new javax.swing.JLabel();
        detailgambar = new javax.swing.JLabel();
        btnstok = new javax.swing.JButton();
        cbukuranstok = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(700, 507));

        txtfdes1.setColumns(20);
        txtfdes1.setRows(5);
        jScrollPane1.setViewportView(txtfdes1);

        btninput.setText("input image");
        btninput.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btninputAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btninput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btninputActionPerformed(evt);
            }
        });

        imgdetail.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                imgdetailAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtnama1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnama1ActionPerformed(evt);
            }
        });

        btnmenu.setText("Kembali ke menu");
        btnmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmenuActionPerformed(evt);
            }
        });

        txtkonfirmasi1.setText("Konfirmasi");
        txtkonfirmasi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkonfirmasi1ActionPerformed(evt);
            }
        });

        btnhapus.setText("Hapus");
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        cbdata.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbdata.setToolTipText("");
        cbdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbdataActionPerformed(evt);
            }
        });

        btnreset.setText("reset");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton3.setText("hapus riwayat");

        jButton4.setText("konfirmasi");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Pengaturan Admin");

        tsdetailbarang.setColumns(20);
        tsdetailbarang.setRows(5);
        jScrollPane4.setViewportView(tsdetailbarang);

        btnkurang.setText("-");
        btnkurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkurangActionPerformed(evt);
            }
        });

        jLabel6.setText("Jumlah");

        txtjumlah.setText("0");
        txtjumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahActionPerformed(evt);
            }
        });

        btntambah.setText("+");
        btntambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntambahActionPerformed(evt);
            }
        });

        jLabel7.setText("di tambah stoknya");

        lbtambahbarang.setText("Tambah Barang");

        lbdetailbarangditambah.setText("Detail barang yang di tambah");

        lbbarangdipesan.setText("Barang Yang di Pesan");

        detailgambar.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                detailgambarAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnstok.setText("Tambah Stok");
        btnstok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnstokActionPerformed(evt);
            }
        });

        cbukuranstok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtkonfirmasi1)
                        .addComponent(txt1u1)
                        .addComponent(jScrollPane1)
                        .addComponent(txt1u2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt1u3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt1u4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtharga1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtnama1)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addComponent(btninput)
                            .addGap(24, 24, 24)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(imgdetail, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnreset)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cbdata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnhapus))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbdetailbarangditambah, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(detailgambar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(btnstok))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnkurang)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btntambah))
                    .addComponent(cbukuranstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnmenu))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbbarangdipesan, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbimg2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69)))
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(380, 380, 380)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(lbtambahbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lbtambahbarang)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(imgdetail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btninput)
                            .addComponent(lbdetailbarangditambah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtnama1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtharga1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt1u1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txt1u2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt1u3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt1u4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(detailgambar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btntambah)
                                    .addComponent(btnkurang))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbukuranstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(btnstok)))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtkonfirmasi1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbdata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnreset)
                            .addComponent(btnhapus)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbbarangdipesan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbimg2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4)
                            .addComponent(btnmenu))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void imgdetailAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_imgdetailAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_imgdetailAncestorAdded

    private void btninputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btninputActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getName();
        File targetFile = new File("src/main/java/produk/" + fileName);

        try {
            // Salin gambar ke folder produk
            Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Simpan path gambar ke variabel global
            this.imagePath = "produk/" + fileName;

            // Tampilkan preview ke label lbimg1
            ImageIcon icon = new ImageIcon(targetFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            imgdetail.setIcon(new ImageIcon(img));

            JOptionPane.showMessageDialog(this, "Gambar berhasil dipilih dan disalin!");

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan gambar: " + ex.getMessage());
        }
    }
// TODO add your handling code here:
    }//GEN-LAST:event_btninputActionPerformed

    private void btninputAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btninputAncestorAdded
// TODO add your handling code here:
    }//GEN-LAST:event_btninputAncestorAdded

    private void txtkonfirmasi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkonfirmasi1ActionPerformed
String nama = txtnama1.getText().trim();
if (nama.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Nama produk tidak boleh kosong!");
    return;
}
updateCbdata();  // perbarui isi comboBox
cbdata.setSelectedItem(nama); // otomatis pilih produk yang baru

    String harga = txtharga1.getText().trim();
    String deskripsi = txtfdes1.getText().trim();
    String[] ukuran = {
        txt1u1.getText().trim(),
        txt1u2.getText().trim(),
        txt1u3.getText().trim(),
        txt1u4.getText().trim()
    };

    // Validasi
    if (nama.isEmpty() || harga.isEmpty() || imagePath.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama, harga, dan gambar wajib diisi!");
        return;
    }

    try {
        new BigDecimal(harga); // validasi format harga

        Barang barang = new Barang(nama, harga, deskripsi, imagePath, ukuran);
        BarangDB.simpanKeDatabase(barang);

        // Reset form
        txtnama1.setText("");
        txtharga1.setText("");
        txtfdes1.setText("");
        txt1u1.setText(""); txt1u2.setText(""); txt1u3.setText(""); txt1u4.setText("");
        imgdetail.setIcon(null);
        imagePath = "";

        JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
    }
   // TODO add your handling code here:
    }//GEN-LAST:event_txtkonfirmasi1ActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
String namaDipilih = (String) cbdata.getSelectedItem();
if (namaDipilih != null && !namaDipilih.isEmpty()) {
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Yakin ingin menghapus semua produk dengan nama \"" + namaDipilih + "\"?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        // Ambil semua path gambar untuk nama yang dipilih
        List<String> imagePaths = BarangDB.getImagePathsByNama(namaDipilih);  // ← kamu perlu membuat method ini

        // Hapus file gambar di folder produk
        for (String path : imagePaths) {
            File file = new File("src/main/java/" + path);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Gambar berhasil dihapus: " + file.getAbsolutePath());
                } else {
                    System.out.println("Gagal menghapus gambar: " + file.getAbsolutePath());
                }
            }
        }

        // Hapus dari database
        BarangDB.deleteProdukByNama(namaDipilih);

        // Perbarui combo box
        cbdata.removeAllItems();
        for (String nama : BarangDB.getSemuaNamaProduk()) {
            cbdata.addItem(nama);
        }

        JOptionPane.showMessageDialog(this, "Produk dan gambar berhasil dihapus.");
    }
} else {
    JOptionPane.showMessageDialog(this, "Silakan pilih nama dari ComboBox terlebih dahulu.");
}
 // TODO add your handling code here:
    }//GEN-LAST:event_btnhapusActionPerformed

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
    txtnama1.setText("");
    txtharga1.setText("");
    txtfdes1.setText("");
    txt1u1.setText(""); txt1u2.setText(""); txt1u3.setText(""); txt1u4.setText("");
    imgdetail.setIcon(null);
    imagePath = "";            // TODO add your handling code here:
    }//GEN-LAST:event_btnresetActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String selected = (String) jComboBox1.getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE histori_transaksi SET status = 'Disetujui' WHERE id_transaksi = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(selected));
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Pesanan dikonfirmasi dan disetujui.");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal konfirmasi: " + ex.getMessage());
    }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
    String sql = "SELECT id_transaksi FROM histori_transaksi WHERE status = 'Menunggu'";
    PreparedStatement stmt = conn.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery();

    jComboBox1.removeAllItems();
    while (rs.next()) {
        jComboBox1.addItem(String.valueOf(rs.getInt("id_transaksi")));
    }
}       catch (SQLException ex) {
            Logger.getLogger(admintest.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void btnmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmenuActionPerformed
        Aplikasi app = new Aplikasi();
        app.setVisible(true);
        app.pack();
        app.setLocationRelativeTo(null);
        this.dispose(); // tutup admintest// TODO add your handling code here:
    }//GEN-LAST:event_btnmenuActionPerformed

    private void txtnama1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnama1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnama1ActionPerformed

    private void btnkurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkurangActionPerformed
        int jumlah = Integer.parseInt(txtjumlah.getText());
        if (jumlah > 0) {
            jumlah--;
            txtjumlah.setText(String.valueOf(jumlah)); }     // TODO add your handling code here:
    }//GEN-LAST:event_btnkurangActionPerformed

    private void txtjumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjumlahActionPerformed

    private void btntambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntambahActionPerformed
    int jumlah = Integer.parseInt(txtjumlah.getText());
    txtjumlah.setText(String.valueOf(jumlah + 1));           // TODO add your handling code here:
    }//GEN-LAST:event_btntambahActionPerformed

    private void cbdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbdataActionPerformed
    if (cbdata.getSelectedItem() != null) {
        tampilkanDetailBarangYangDitambah();
    }
 /* cbdata.removeAllItems();
ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand FROM produk");
while (rs.next()) {
    cbdata.addItem(rs.getString("brand"));
}*/
        // TODO add your handling code here:
    }//GEN-LAST:event_cbdataActionPerformed

    private void detailgambarAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_detailgambarAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_detailgambarAncestorAdded

    private void btnstokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnstokActionPerformed
        String nama = (String) cbdata.getSelectedItem();
    String ukuran = (String) cbukuranstok.getSelectedItem();
    String jumlahText = txtjumlah.getText().trim();

    if (nama == null || ukuran == null || jumlahText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih produk, ukuran, dan isi jumlah terlebih dahulu.");
        return;
    }

    try {
        int jumlah = Integer.parseInt(jumlahText);
        tambahStokUkuran(nama, ukuran, jumlah);
        JOptionPane.showMessageDialog(this, "Stok berhasil ditambahkan!");
        tampilkanDetailBarangYangDitambah();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnstokActionPerformed

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
            java.util.logging.Logger.getLogger(admintest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admintest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admintest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admintest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admintest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnhapus;
    private javax.swing.JButton btninput;
    private javax.swing.JButton btnkurang;
    private javax.swing.JButton btnmenu;
    private javax.swing.JButton btnreset;
    private javax.swing.JButton btnstok;
    private javax.swing.JButton btntambah;
    private javax.swing.JComboBox<String> cbdata;
    private javax.swing.JComboBox<String> cbukuranstok;
    private javax.swing.JLabel detailgambar;
    private javax.swing.JLabel imgdetail;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbbarangdipesan;
    private javax.swing.JLabel lbdetailbarangditambah;
    private javax.swing.JLabel lbimg2;
    private javax.swing.JLabel lbtambahbarang;
    private javax.swing.JTextArea tsdetailbarang;
    private javax.swing.JTextField txt1u1;
    private javax.swing.JTextField txt1u2;
    private javax.swing.JTextField txt1u3;
    private javax.swing.JTextField txt1u4;
    private javax.swing.JTextArea txtfdes1;
    private javax.swing.JTextField txtharga1;
    private javax.swing.JTextField txtjumlah;
    private javax.swing.JButton txtkonfirmasi1;
    private javax.swing.JTextField txtnama1;
    // End of variables declaration//GEN-END:variables
}
