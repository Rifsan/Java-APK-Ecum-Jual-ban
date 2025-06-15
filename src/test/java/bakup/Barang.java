/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;

/**
 *
 * @author acer
 */


public class Barang {

    String nama;
    private String harga, deskripsi;
    private String ukuran1, ukuran2, ukuran3, ukuran4;

    public Barang(String nama, String harga, String deskripsi,
                  String ukuran1, String ukuran2, String ukuran3, String ukuran4) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.ukuran1 = ukuran1;
        this.ukuran2 = ukuran2;
        this.ukuran3 = ukuran3;
        this.ukuran4 = ukuran4;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getUkuran1() {
        return ukuran1;
    }

    public String getUkuran2() {
        return ukuran2;
    }

    public String getUkuran3() {
        return ukuran3;
    }

    public String getUkuran4() {
        return ukuran4;
    }

    String getGambar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
