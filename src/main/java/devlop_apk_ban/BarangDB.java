/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class BarangDB {
    public static void simpanKeDatabase(Barang barang) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "");
            conn.setAutoCommit(false);

            // Debug: Print values being inserted
            System.out.println("Inserting Product:");
            System.out.println("Nama: " + barang.getNama());
            System.out.println("Harga: " + barang.getHarga());
            System.out.println("Deskripsi: " + barang.getDeskripsi());
            System.out.println("Image Path: " + barang.getImagePath());
            System.out.println("Ukuran: " + Arrays.toString(barang.getUkuran()));

            // Insert into produk
            String sqlProduk = "INSERT INTO produk (brand, harga, deskripsi) VALUES (?, ?, ?)";
            try (PreparedStatement pstProduk = conn.prepareStatement(sqlProduk, Statement.RETURN_GENERATED_KEYS)) {
                pstProduk.setString(1, barang.getNama());
                pstProduk.setBigDecimal(2, new BigDecimal(barang.getHarga()));
                pstProduk.setString(3, barang.getDeskripsi());
                pstProduk.executeUpdate();

                // Get generated product ID
                try (ResultSet rs = pstProduk.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idProduk = rs.getInt(1);
                        System.out.println("Generated Product ID: " + idProduk);

                        // Insert sizes
                        String sqlDetail = "INSERT INTO detail_produk (idProduk, size) VALUES (?, ?)";
                        try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail)) {
                            for (String size : barang.getUkuran()) {
                                if (!size.isEmpty()) {
                                    pstDetail.setInt(1, idProduk);
                                    pstDetail.setString(2, size);
                                    pstDetail.addBatch();
                                    System.out.println("Inserting Size: " + size);
                                }
                            }
                            pstDetail.executeBatch();
                        }

                        // Insert image
                        if (barang.getImagePath() != null && !barang.getImagePath().isEmpty()) {
                            String sqlGambar = "INSERT INTO gambar (idProduk, namaFile, path) VALUES (?, ?, ?)";
                            try (PreparedStatement pstGambar = conn.prepareStatement(sqlGambar)) {
                                pstGambar.setInt(1, idProduk);
                                pstGambar.setString(2, "product_image");
                                pstGambar.setString(3, barang.getImagePath());
                                pstGambar.executeUpdate();
                                System.out.println("Inserting Image Path: " + barang.getImagePath());
                            }
                        }
                    }
                }
            }
            conn.commit();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void deleteProduk(int idProduk) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
            // Delete from child tables first
            String sqlDetail = "DELETE FROM detail_produk WHERE idProduk = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlDetail)) {
                pst.setInt(1, idProduk);
                pst.executeUpdate();
            }

            String sqlGambar = "DELETE FROM gambar WHERE idProduk = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlGambar)) {
                pst.setInt(1, idProduk);
                pst.executeUpdate();
            }

            // Delete from main table
            String sqlProduk = "DELETE FROM produk WHERE idProduk = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlProduk)) {
                pst.setInt(1, idProduk);
                int rowsDeleted = pst.executeUpdate();
                
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                } else {
                    JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static List<String> getSemuaNamaProduk() {
    List<String> namaList = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "");
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand FROM produk")) {

        while (rs.next()) {
            namaList.add(rs.getString("brand"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return namaList;
}
public static void deleteProdukByNama(String namaProduk) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        // Ambil semua idProduk dengan nama tersebut
        String sqlSelect = "SELECT idProduk FROM produk WHERE brand = ?";
        try (PreparedStatement pstSelect = conn.prepareStatement(sqlSelect)) {
            pstSelect.setString(1, namaProduk);
            try (ResultSet rs = pstSelect.executeQuery()) {
                while (rs.next()) {
                    int idProduk = rs.getInt("idProduk");

                    // Hapus dari detail_produk
                    try (PreparedStatement pst = conn.prepareStatement("DELETE FROM detail_produk WHERE idProduk = ?")) {
                        pst.setInt(1, idProduk);
                        pst.executeUpdate();
                    }

                    // Hapus dari gambar
                    try (PreparedStatement pst = conn.prepareStatement("DELETE FROM gambar WHERE idProduk = ?")) {
                        pst.setInt(1, idProduk);
                        pst.executeUpdate();
                    }

                    // Hapus dari produk
                    try (PreparedStatement pst = conn.prepareStatement("DELETE FROM produk WHERE idProduk = ?")) {
                        pst.setInt(1, idProduk);
                        pst.executeUpdate();
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Semua data dengan nama \"" + namaProduk + "\" berhasil dihapus!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal menghapus: " + e.getMessage());
        e.printStackTrace();
    }
}

public static List<String> getImagePathsByNama(String nama) {
    List<String> paths = new ArrayList<>();
    
    // Daftarkan driver
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        return paths;
    }

    // Koneksi database
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT imagePath FROM barang WHERE nama = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nama);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            paths.add(rs.getString("imagePath"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return paths;
}
public static int cariIdProdukByNama(String namaProduk) {
    int idProduk = -1;

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "")) {
        String sql = "SELECT idProduk FROM produk WHERE brand = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaProduk);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idProduk = rs.getInt("idProduk");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mencari idProduk: " + e.getMessage());
    }

    return idProduk;
}


public static List<Barang> getSemuaBarang() {
    List<Barang> daftarBarang = new ArrayList<>();

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbban", "root", "");
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM produk")) {

        while (rs.next()) {
            int idProduk = rs.getInt("idProduk");
            String nama = rs.getString("brand");
            String harga = rs.getString("harga");
            String deskripsi = rs.getString("deskripsi");

            // Ambil ukuran dari detail_produk
            List<String> ukuranList = new ArrayList<>();
            String sqlUkuran = "SELECT size FROM detail_produk WHERE idProduk = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlUkuran)) {
                pst.setInt(1, idProduk);
                ResultSet rsUkuran = pst.executeQuery();
                while (rsUkuran.next()) {
                    ukuranList.add(rsUkuran.getString("size"));
                }
            }

            // Ambil path gambar dari tabel gambar
            String imagePath = "";
            String sqlGambar = "SELECT path FROM gambar WHERE idProduk = ? LIMIT 1";
            try (PreparedStatement pst = conn.prepareStatement(sqlGambar)) {
                pst.setInt(1, idProduk);
                ResultSet rsGambar = pst.executeQuery();
                if (rsGambar.next()) {
                    imagePath = rsGambar.getString("path");
                }
            }

            Barang barang = new Barang(nama, harga, deskripsi, imagePath, ukuranList.toArray(new String[0]));
            daftarBarang.add(barang);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mengambil data barang: " + e.getMessage());
    }

    return daftarBarang;
}


}
