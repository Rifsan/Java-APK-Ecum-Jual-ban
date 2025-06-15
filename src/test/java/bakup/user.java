/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;

/**
 *
 * @author acer
 */
public class user {
    private int idPengguna;
    private String username;
    private String password;
    private String peran; // 'admin' atau 'user'

    // Getter dan Setter
    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPeran() { return peran; }
    public void setPeran(String peran) { this.peran = peran; }
}
