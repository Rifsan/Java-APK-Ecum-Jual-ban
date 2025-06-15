/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author acer
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestKoneksiLihatDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = ""; // ganti jika ada password

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Koneksi ke MySQL berhasil!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW DATABASES");

            System.out.println("📂 Daftar Database:");
            while (rs.next()) {
                System.out.println(" - " + rs.getString(1));
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Gagal terhubung ke MySQL:");
            e.printStackTrace();
        }
    }
}
