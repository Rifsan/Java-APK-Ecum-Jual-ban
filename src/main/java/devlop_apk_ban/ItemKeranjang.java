/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;

/**
 *
 * @author acer
 */
public class ItemKeranjang {
    private int idProduk;
    private String nama;
    private String ukuran;
    private int jumlah;
    private int harga;

    public ItemKeranjang(int idProduk, String nama, String ukuran, int jumlah, int harga) {
        this.idProduk = idProduk;
        this.nama = nama;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    // Tambahan constructor untuk fleksibilitas
    public ItemKeranjang() {}

    // Getter & Setter
    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getTotalHarga() {
        return jumlah * harga;
    }

    public void tambahJumlah() {
        jumlah++;
    }

    public void kurangJumlah() {
        if (jumlah > 1) jumlah--;
    }
}

