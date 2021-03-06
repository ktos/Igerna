/*
 * Igerna, version 0.2
 *
 * Copyright (C) Marcin Badurowicz 2009-2010
 *
 *
 * This file is part of Igerna.
 *
 * Igerna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Igerna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * Licensealong with Igerna. If not, see <http://www.gnu.org/licenses/>.
 */

package info.ktos.igerna.manager;

import info.ktos.igerna.Config;
import info.ktos.igerna.IgernaServer;
import info.ktos.igerna.UserCredentialsProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Klasa reprezentująca okno okienkowej aplikacji do zarządzania
 * serwerem Igerna
 */
public class Manager extends javax.swing.JFrame
{

    private Config conf;
    private UserCredentialsProvider ucp;
    private DefaultTableModel utm;

    /** Creates new form Manager */
    public Manager()
    {
        initComponents();

        // wczytywanie konfiguracji
        try
        {
            conf = new Config("igerna.conf");
            conf.readFile();            
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(rootPane, "Wystąpił błąd odczytu pliku konfiguracyjnego, tworzę nowy plik.", "Igerna", JOptionPane.WARNING_MESSAGE, null);
        }

        try
        {
            ucp = new UserCredentialsProvider(conf.getStringEntry("path", "passwd", "passwd"));
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(rootPane, "Wystąpił błąd odczytu pliku użytkowników.", "Igerna", JOptionPane.ERROR_MESSAGE, null);
            System.exit(1);
        }

        jtfHost.setText(conf.getStringEntry("bind", "host", "127.0.0.1"));
        jtfPort.setText(conf.getStringEntry("bind", "port", "5222"));


        utm = new DefaultTableModel(new String[] { "Login użytkonika", "Hasło", "Nazwa" }, 0);
        jtUsersTable.setModel(utm);

        try
        {
            for (String[] s : ucp.getUserData())
            {
                utm.addRow(new String[] { s[0], s[1], s[4] });
            }
        }
        catch (Exception ex)
        {
            
        }

        // listener zmian w tabelce
        jtUsersTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e)
            {
                TableModel model = (TableModel)e.getSource();

                if (e.getType() == TableModelEvent.DELETE)
                {
                    int row = e.getFirstRow();
                    ucp.removeUser(row);
                }
                else if (e.getType() == TableModelEvent.INSERT)
                {
                    DefaultTableModel dtm = (DefaultTableModel)model;
                    Vector data = dtm.getDataVector();

                    Vector d0 = (Vector)data.elementAt(0);                    
                                        
                    ucp.addUser((String)d0.elementAt(0), "", "");
                }
                else
                {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    

                    // jeśli zostało zmienione hasło, przelicz je na sumę
                    // MD5 tego hasła
                    if (col == 1)
                    {
                        String org = (String)model.getValueAt(row, col);

                        // jeśli dane hasła mają 32 znaki to jest to pewnie
                        // suma kontrolna, prawda?
                        // brzydki hack, ale działa skutecznie :-)
                        if (org.length() != 32)
                        {
                            String newd = UserCredentialsProvider.md5(org);
                            model.setValueAt(newd, row, col);
                        }
                    }
                    
                    // odzwierciedlenie zmian w tabeli w UCP
                    ucp.changeUser(row, (String)model.getValueAt(row, 0),
                            (String)model.getValueAt(row, 1),
                            (String)model.getValueAt(row, 2));
                }
            }
        });
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtpTabs = new javax.swing.JTabbedPane();
        jpServerMan = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtfHost = new javax.swing.JTextField();
        jtfPort = new javax.swing.JTextField();
        jbStart = new javax.swing.JButton();
        jbStop = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jpUserMan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsersTable = new javax.swing.JTable();
        jbAddUser = new javax.swing.JButton();
        jbDeleteUser = new javax.swing.JButton();
        jpButtons = new javax.swing.JPanel();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Igerna - konfiguracja");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("mainFrame"); // NOI18N

        jLabel2.setText("Port na którym serwer będzie nasłuchiwał:");

        jLabel1.setText("Host lub adres IP na którym serwer będzie nasłuchiwał:");

        jtfHost.setText("127.0.0.1");

        jtfPort.setText("5222");

        jbStart.setText("Uruchom serwer");
        jbStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStartActionPerformed(evt);
            }
        });

        jbStop.setText("Zatrzymaj serwer");
        jbStop.setEnabled(false);
        jbStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStopActionPerformed(evt);
            }
        });

        jLabel3.setText("Serwer można także uruchomić uruchamiając główną klasę aplikacji - IgernaServer");

        javax.swing.GroupLayout jpServerManLayout = new javax.swing.GroupLayout(jpServerMan);
        jpServerMan.setLayout(jpServerManLayout);
        jpServerManLayout.setHorizontalGroup(
            jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpServerManLayout.createSequentialGroup()
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpServerManLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jbStart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jbStop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfHost, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpServerManLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jpServerManLayout.setVerticalGroup(
            jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpServerManLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jbStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jtpTabs.addTab("Zarządzanie serwerem", jpServerMan);

        jtUsersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nazwa użytkownika", "Hasło użytkownika", "Opis użytkownika (geckos)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtUsersTable);

        jbAddUser.setText("Dodaj");
        jbAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddUserActionPerformed(evt);
            }
        });

        jbDeleteUser.setText("Usuń");
        jbDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDeleteUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpUserManLayout = new javax.swing.GroupLayout(jpUserMan);
        jpUserMan.setLayout(jpUserManLayout);
        jpUserManLayout.setHorizontalGroup(
            jpUserManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUserManLayout.createSequentialGroup()
                .addComponent(jbAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbDeleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
        );
        jpUserManLayout.setVerticalGroup(
            jpUserManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpUserManLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpUserManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAddUser)
                    .addComponent(jbDeleteUser)))
        );

        jtpTabs.addTab("Zarządzanie użytkownikami", jpUserMan);

        jbOK.setText("OK");
        jbOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKActionPerformed(evt);
            }
        });

        jbCancel.setText("Anuluj");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpButtonsLayout = new javax.swing.GroupLayout(jpButtons);
        jpButtons.setLayout(jpButtonsLayout);
        jpButtonsLayout.setHorizontalGroup(
            jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpButtonsLayout.createSequentialGroup()
                .addContainerGap(420, Short.MAX_VALUE)
                .addComponent(jbOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancel)
                .addContainerGap())
        );
        jpButtonsLayout.setVerticalGroup(
            jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpButtonsLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbOK)
                    .addComponent(jbCancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
            .addComponent(jpButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jtpTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbCancelActionPerformed
    {//GEN-HEADEREND:event_jbCancelActionPerformed

        // zamykamy aplikację, zmiany zostają zignorowane
        Manager.this.dispose();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jbOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbOKActionPerformed
    {//GEN-HEADEREND:event_jbOKActionPerformed

        // ustawianie i zapisywanie konfiguracji
        if (!jtfHost.getText().equals(conf.getStringEntry("bind", "host", "127.0.0.1")))
            conf.setStringEntry("bind", "host", jtfHost.getText());

        if (!jtfPort.getText().equals(conf.getStringEntry("bind", "port", "5222")))
            conf.setStringEntry("bind", "port", jtfPort.getText());        

        try
        {
            conf.saveFile();
            ucp.saveToFile();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());        
            JOptionPane.showMessageDialog(rootPane, "Wystąpił błąd zapisu do pliku konfiguracyjnego lub pliku użytkowników!", "Igerna", JOptionPane.ERROR_MESSAGE, null);
        }

        // zamykamy aplikację i zapisujemy zmiany
        Manager.this.dispose();
    }//GEN-LAST:event_jbOKActionPerformed

    private void jbStartActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbStartActionPerformed
    {//GEN-HEADEREND:event_jbStartActionPerformed
        Thread t = new Thread() {

            @Override
            public void run()
            {
                IgernaServer.startServer();
            }
        };
        t.start();

        jbStart.setEnabled(false);
        jbStop.setEnabled(true);
    }//GEN-LAST:event_jbStartActionPerformed

    private void jbStopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbStopActionPerformed
    {//GEN-HEADEREND:event_jbStopActionPerformed
        IgernaServer.stop();
        jbStart.setEnabled(true);
        jbStop.setEnabled(false);
    }//GEN-LAST:event_jbStopActionPerformed

    private void jbAddUserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbAddUserActionPerformed
    {//GEN-HEADEREND:event_jbAddUserActionPerformed
        utm.addRow(new String[] { "login (zmień to)", "hasło (md5 samo się ustawi)", "przyjazna nazwa" });
    }//GEN-LAST:event_jbAddUserActionPerformed

    private void jbDeleteUserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbDeleteUserActionPerformed
    {//GEN-HEADEREND:event_jbDeleteUserActionPerformed
        if (jtUsersTable.getSelectedRow() != -1)
            utm.removeRow(jtUsersTable.getSelectedRow());
    }//GEN-LAST:event_jbDeleteUserActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddUser;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDeleteUser;
    private javax.swing.JButton jbOK;
    private javax.swing.JButton jbStart;
    private javax.swing.JButton jbStop;
    private javax.swing.JPanel jpButtons;
    private javax.swing.JPanel jpServerMan;
    private javax.swing.JPanel jpUserMan;
    private javax.swing.JTable jtUsersTable;
    private javax.swing.JTextField jtfHost;
    private javax.swing.JTextField jtfPort;
    private javax.swing.JTabbedPane jtpTabs;
    // End of variables declaration//GEN-END:variables

}
