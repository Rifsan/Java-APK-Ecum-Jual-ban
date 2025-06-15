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
    private int id;
    private String nama;
    private String harga;
    private String deskripsi;
    private String imagePath;
    private String[] ukuran;

    public Barang(int id, String nama, String harga, String deskripsi, String imagePath, String[] ukuran) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
        this.ukuran = ukuran;
    }

    public Barang(String nama, String harga, String deskripsi, String imagePath, String[] ukuran) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
        this.ukuran = ukuran;
    }

    public int getId() {
        return id;
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

    public String getImagePath() {
        return imagePath;
    }

    public String[] getUkuran() {
        return ukuran;
    }

    void setImagePath(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
