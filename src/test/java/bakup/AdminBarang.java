/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlop_apk_ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminBarang {

public class admintest extends javax.swing.JFrame {



    /**
     * Creates new form admintest
     */
    public admintest() {
        initComponents();

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        btnimg1 = new javax.swing.JButton();
        btnimg2 = new javax.swing.JButton();
        btnimg3 = new javax.swing.JButton();
        btnimg4 = new javax.swing.JButton();
        lbimg1 = new javax.swing.JLabel();
        txtnama1 = new javax.swing.JTextField();
        txtnama2 = new javax.swing.JTextField();
        txtnama4 = new javax.swing.JTextField();
        txtnama3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtfdes1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtfdes2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtfdes4 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtfdes3 = new javax.swing.JTextArea();
        txt1u1 = new javax.swing.JTextField();
        txt1u2 = new javax.swing.JTextField();
        txt1u3 = new javax.swing.JTextField();
        txt1u4 = new javax.swing.JTextField();
        txt2u1 = new javax.swing.JTextField();
        txt2u2 = new javax.swing.JTextField();
        txt2u3 = new javax.swing.JTextField();
        txt2u4 = new javax.swing.JTextField();
        txt3u3 = new javax.swing.JTextField();
        txt3u1 = new javax.swing.JTextField();
        txt3u4 = new javax.swing.JTextField();
        txt3u2 = new javax.swing.JTextField();
        txt4u3 = new javax.swing.JTextField();
        txt4u1 = new javax.swing.JTextField();
        txt4u4 = new javax.swing.JTextField();
        txt4u2 = new javax.swing.JTextField();
        btnmenu = new javax.swing.JButton();
        btnDeleteAll = new javax.swing.JButton();
        lbimg2 = new javax.swing.JLabel();
        lbimg3 = new javax.swing.JLabel();
        lbimg4 = new javax.swing.JLabel();
        txtharga2 = new javax.swing.JTextField();
        txtharga3 = new javax.swing.JTextField();
        txtharga1 = new javax.swing.JTextField();
        txtharga4 = new javax.swing.JTextField();
        txtkonfirmasi1 = new javax.swing.JButton();
        txthapus = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        konfirmasi2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        konfirmasi3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        konfirmasi4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnimg1.setText("input image");
        btnimg1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnimg1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnimg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimg1ActionPerformed(evt);
            }
        });

        btnimg2.setText("input image");
        btnimg2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnimg2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnimg2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimg2ActionPerformed(evt);
            }
        });

        btnimg3.setText("input image");
        btnimg3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnimg3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnimg3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimg3ActionPerformed(evt);
            }
        });

        btnimg4.setText("input image");
        btnimg4.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnimg4AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnimg4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimg4ActionPerformed(evt);
            }
        });

        lbimg1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lbimg1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtnama3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnama3ActionPerformed(evt);
            }
        });

        txtfdes1.setColumns(20);
        txtfdes1.setRows(5);
        jScrollPane1.setViewportView(txtfdes1);

        txtfdes2.setColumns(20);
        txtfdes2.setRows(5);
        jScrollPane2.setViewportView(txtfdes2);

        txtfdes4.setColumns(20);
        txtfdes4.setRows(5);
        jScrollPane3.setViewportView(txtfdes4);

        txtfdes3.setColumns(20);
        txtfdes3.setRows(5);
        jScrollPane4.setViewportView(txtfdes3);

        btnmenu.setText("Kembali ke menu");
        btnmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmenuActionPerformed(evt);
            }
        });

        btnDeleteAll.setText("Hapus Gambar");
        btnDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllActionPerformed(evt);
            }
        });

        txtkonfirmasi1.setText("Konfirmasi");
        txtkonfirmasi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkonfirmasi1ActionPerformed(evt);
            }
        });

        txthapus.setText("Hapus");
        txthapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthapusActionPerformed(evt);
            }
        });

        jButton1.setText("Hapus");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        konfirmasi2.setText("Konfirmasi");
        konfirmasi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                konfirmasi2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        konfirmasi3.setText("Konfirmasi");
        konfirmasi3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                konfirmasi3ActionPerformed(evt);
            }
        });

        jButton5.setText("Hapus");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        konfirmasi4.setText("Konfirmasi");
        konfirmasi4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                konfirmasi4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbimg1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnimg1)
                        .addGap(19, 19, 19)))
                .addGap(63, 63, 63)
                .addComponent(btnimg2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnimg3)
                .addGap(92, 92, 92)
                .addComponent(btnimg4)
                .addGap(45, 45, 45))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteAll)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txthapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtkonfirmasi1))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt1u1)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(txt1u2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt1u3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt1u4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(txtnama1))
                                .addComponent(txtharga1, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lbimg2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(lbimg3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtnama2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtnama3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(txtharga2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(txtharga3)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(konfirmasi2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txt2u1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt2u2)
                                    .addComponent(txt2u3)
                                    .addComponent(txt2u4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt3u1)
                                    .addComponent(txt3u2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt3u3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt3u4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(konfirmasi3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbimg4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(konfirmasi4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txt4u1)
                                        .addComponent(txt4u2, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt4u3, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt4u4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txtharga4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnama4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmenu)
                        .addGap(24, 24, 24))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbimg1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(lbimg2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbimg3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbimg4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnimg1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnama2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnama4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnimg3)
                            .addComponent(btnimg4)
                            .addComponent(btnimg2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnama3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtnama1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtharga2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtharga3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtharga4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtharga1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt2u1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt2u2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt2u3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt2u4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt3u1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt3u2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt3u3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt3u4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt4u1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt4u2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt4u3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt4u4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt1u1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt1u2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1u3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt1u4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkonfirmasi1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthapus)
                    .addComponent(jButton1)
                    .addComponent(konfirmasi2)
                    .addComponent(jButton3)
                    .addComponent(konfirmasi3)
                    .addComponent(jButton5)
                    .addComponent(konfirmasi4))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteAll)
                    .addComponent(btnmenu))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void btnmenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
    Aplikasi app = new Aplikasi();
    app.setVisible(true);
    app.pack();
    app.setLocationRelativeTo(null);
    this.dispose(); // tutup admintest// TODO add your handling code here:
    }                                       

    private void lbimg1AncestorAdded(javax.swing.event.AncestorEvent evt) {                                     
        // TODO add your handling code here:
    }                                    

    private void btnimg1ActionPerformed(java.awt.event.ActionEvent evt) {                                        
    /**  JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Gambar");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar", "jpg", "jpeg", "png"));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        try {
            // Pastikan folder 'images' dan 'data' ada
            File imageDir = new File("images");
            if (!imageDir.exists()) imageDir.mkdir();

            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdir();

            // Salin file gambar baru
            String relativePath = "images/" + selectedFile.getName();
            File destFile = new File(relativePath);

            try (InputStream in = new FileInputStream(selectedFile);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }

            // Simpan path gambar baru di file
            GambarManager.simpanPathGambar(relativePath);

            // Tampilkan gambar di label lbimg1
            GambarManager.tampilkanSemuaGambarKeLabel(new JLabel[] { lbimg1 });

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan gambar.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }*/
    // TODO add your handling code here:
    }                                       

    private void btnimg2ActionPerformed(java.awt.event.ActionEvent evt) {                                        
    /**JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Gambar");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar", "jpg", "jpeg", "png"));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        try {
            // Pastikan folder 'images' dan 'data' ada
            File imageDir = new File("images");
            if (!imageDir.exists()) imageDir.mkdir();

            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdir();

            // Salin file gambar baru
            String relativePath = "images/" + selectedFile.getName();
            File destFile = new File(relativePath);

            try (InputStream in = new FileInputStream(selectedFile);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }

            // Simpan path gambar baru di file
            GambarManager.simpanPathGambar(relativePath);

            // Tampilkan gambar di label lbimg1
            GambarManager.tampilkanSemuaGambarKeLabel(new JLabel[] { lbimg2 });

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan gambar.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }         TODO add your handling code here:*/
    }                                       

    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {                                             
    /**int konfirmasi = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin ingin menghapus semua gambar?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION
    );

    if (konfirmasi == JOptionPane.YES_OPTION) {
        GambarManager.hapusSemuaDataGambar();

        // Kosongkan semua label gambar (jika punya)
        lbimg1.setIcon(null); lbimg1.setText("Gambar 1");
        lbimg2.setIcon(null); lbimg2.setText("Gambar 2");
        lbimg3.setIcon(null); lbimg3.setText("Gambar 3");
        lbimg4.setIcon(null); lbimg4.setText("Gambar 4");

        JOptionPane.showMessageDialog(this, "Semua gambar berhasil dihapus.");
    } else {
        JOptionPane.showMessageDialog(this, "Penghapusan dibatalkan.");
    }*/
            // TODO add your handling code here:
    }                                            

    private void btnimg1AncestorAdded(javax.swing.event.AncestorEvent evt) {                                      
    /**btnimg1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // Menampilkan gambar pertama pada lbimg1
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg1, 0);
    }
});

btnimg1 = new javax.swing.JButton();

btnimg1.setText("input image");

btnimg1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg1, 1);
        btnimg1ActionPerformed(evt);
    }
});*/

// Code adding the component to the parent container - not shown here
        // TODO add your handling code here:
    }                                     

    private void btnimg2AncestorAdded(javax.swing.event.AncestorEvent evt) {                                      
    /**btnimg2.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // Menampilkan gambar pertama pada lbimg1
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg2, 2);
    }
});

btnimg2 = new javax.swing.JButton();

btnimg2.setText("input image");

btnimg2.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg2, 3);
        btnimg1ActionPerformed(evt);
    }
});*/       // TODO add your handling code here:
    }                                     

    private void btnimg3ActionPerformed(java.awt.event.ActionEvent evt) {                                        
  //         // TODO add your handling code here:   // TODO add your handling code here:
    }                                       

    private void btnimg3AncestorAdded(javax.swing.event.AncestorEvent evt) {                                      
        btnimg3.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // Menampilkan gambar pertama pada lbimg1
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg3, 0);
    }
});

btnimg3 = new javax.swing.JButton();

btnimg3.setText("input image");

btnimg3.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        GambarManager.tampilkanGambarKeLabelBerdasarkanIndex(lbimg3, 0);
        btnimg1ActionPerformed(evt);
    }
});        // TODO add your handling code here:
    }                                     

    private void btnimg4ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void btnimg4AncestorAdded(javax.swing.event.AncestorEvent evt) {                                      
         // TODO add your handling code here:
    }                                     

    private void txtnama3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void txtkonfirmasi1ActionPerformed(java.awt.event.ActionEvent evt) {                                               
    String nama = txtnama1.getText();
    String harga = txtharga1.getText();
    String deskripsi = txtfdes1.getText();
    String ukuran1 = txt1u1.getText();
    String ukuran2 = txt1u2.getText();
    String ukuran3 = txt1u3.getText();
    String ukuran4 = txt1u4.getText();

 AdminBarang.simpanBarang(nama, harga, deskripsi, ukuran1, ukuran2, ukuran3, ukuran4);
   // TODO add your handling code here:
    }                                              

    private void txthapusActionPerformed(java.awt.event.ActionEvent evt) {                                         
     AdminBarang.hapusSemuaBarang(); // hapus data

    // Kosongkan tampilan tombol
    txtnama1.setText("");
    txtharga1.setText("");
    txtfdes1.setText("");
    txt1u1.setText("");
    txt1u2.setText("");
    txt1u3.setText("");
    txt1u4.setText("");

        JOptionPane.showMessageDialog(this, "Semua data berhasil dihapus.");// TODO add your handling code here:
    }                                        

}