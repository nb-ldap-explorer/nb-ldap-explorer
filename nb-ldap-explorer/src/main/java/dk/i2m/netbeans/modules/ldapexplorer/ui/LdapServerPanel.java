/*
 *  Copyright 2010 Interactive Media Management
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package dk.i2m.netbeans.modules.ldapexplorer.ui;

import dk.i2m.netbeans.modules.ldapexplorer.model.Authentication;
import dk.i2m.netbeans.modules.ldapexplorer.model.Krb5LoginConf;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import org.openide.util.Exceptions;

/**
 * Panel showing the properties of an {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServerPanel extends javax.swing.JPanel {

    /** 
     * Creates new instance of {@link LdapServerPanel}.
     */
    public LdapServerPanel() {
        initComponents();
    }

    public void setHostname(String hostname) {
        this.txtHostname.setText(hostname);
    }

    public String getHostname() {
        return txtHostname.getText();
    }

    public void setLabel(String label) {
        this.txtLabel.setText(label);
    }

    public String getLabel() {
        return txtLabel.getText();
    }

    public void setPort(Integer port) {
        this.txtPort.setText(String.valueOf(port));
    }

    public Integer getPort() {
        try {
            return Integer.valueOf(txtPort.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void setTimeout(Integer timeout) {
        this.txtTimeout.setText(String.valueOf(timeout));
    }

    public Integer getTimeout() {
        try {
            return Integer.valueOf(txtTimeout.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void setBaseDN(String basedn) {
        this.txtBaseDn.setText(basedn);
    }

    public String getBaseDN() {
        return txtBaseDn.getText();
    }

    public Authentication getAuthentication() {
        return (Authentication) cbAuthentication.getSelectedItem();
    }

    public void setAuthentication(Authentication authentication) {
        cbAuthentication.setSelectedItem(authentication);
    }

    public String getBind() {
        return txtBind.getText();
    }

    public void setBind(String bind) {
        this.txtBind.setText(bind);
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void setPassword(String password) {
        this.txtPassword.setText(password);
    }

    public File getKrb5Keytab() {
        return new File(txtKrb5Keytab.getText());
    }

    public void setKrb5Keytab(File keytab) {
        try {
            txtKrb5Keytab.setText(keytab.getCanonicalPath());
        } catch (IOException ex) {
            txtKrb5Keytab.setText(keytab.getAbsolutePath());
        }
    }

    public Krb5LoginConf getKrb5LoginConf() {
        return (Krb5LoginConf) cbKrb5LoginConf.getSelectedItem();
    }

    public void setKrb5LoginConf(Krb5LoginConf loginConf) {
        cbKrb5LoginConf.setSelectedItem(loginConf);
    }

    public String getKrb5Username() {
        return txtKrb5Username.getText();
    }

    public void setKrb5Username(String username) {
        txtKrb5Username.setText(username);
    }

    public String getKrb5Password() {
        return txtKrb5Password.getText();
    }

    public void setKrb5Password(String password) {
        txtKrb5Password.setText(password);
    }
    
    /**
     * Determines if secure socket layer should be enabled for the connection.
     *
     * @return <code>true</code> if secure socket layer should be enabled,
     *         otherwise <code>false</code>
     */
    public boolean isSecureSocketLayerEnabled() {
        return this.cbSecure.isSelected();
    }

    /**
     * Sets the secure socket layer requirement for the connection.
     *
     * @param enableSsl
     *          <code>true</code> if secure socket layer is required for
     *          the connection, otherwise <code>false</code>
     */
    public void setSecureSocketLayerEnabled(boolean enableSsl) {
        this.cbSecure.setSelected(enableSsl);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTabbed = new javax.swing.JTabbedPane();
        pnlConnection = new javax.swing.JPanel();
        lblHost = new javax.swing.JLabel();
        txtHostname = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        lblBaseDn = new javax.swing.JLabel();
        txtBaseDn = new javax.swing.JTextField();
        lblAuthentication = new javax.swing.JLabel();
        cbAuthentication = new javax.swing.JComboBox();
        cbSecure = new javax.swing.JCheckBox();
        lblLabel = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        lblTimeout = new javax.swing.JLabel();
        txtTimeout = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        txtBind = new javax.swing.JTextField();
        lblBind = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        lblKrb5LoginConf = new javax.swing.JLabel();
        cbKrb5LoginConf = new javax.swing.JComboBox();
        txtKrb5Username = new javax.swing.JTextField();
        txtKrb5Password = new javax.swing.JTextField();
        txtKrb5Keytab = new javax.swing.JTextField();
        btnKrb5Keytab = new javax.swing.JButton();
        lblKrb5Username = new javax.swing.JLabel();
        lblKrb5Password = new javax.swing.JLabel();
        lblKrb5Keytab = new javax.swing.JLabel();

        lblHost.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblHost.text")); // NOI18N

        txtHostname.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtHostname.text")); // NOI18N

        lblPort.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblPort.text")); // NOI18N

        txtPort.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtPort.text")); // NOI18N

        lblBaseDn.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblBaseDn.text")); // NOI18N

        txtBaseDn.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtBaseDn.text")); // NOI18N

        lblAuthentication.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblAuthentication.text")); // NOI18N

        cbAuthentication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAuthenticationActionPerformed(evt);
            }
        });
        DefaultComboBoxModel cbAuthenticationModel = new DefaultComboBoxModel(Authentication.values());

        cbAuthentication.setModel(cbAuthenticationModel);

        cbSecure.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.cbSecure.text")); // NOI18N

        lblLabel.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblLabel.text")); // NOI18N

        txtLabel.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtLabel.text")); // NOI18N

        lblTimeout.setLabelFor(txtTimeout);
        lblTimeout.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblTimeout.text")); // NOI18N

        txtTimeout.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtTimeout.text")); // NOI18N

        txtBind.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtBind.text")); // NOI18N

        lblBind.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblBind.text")); // NOI18N

        lblPassword.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblPassword.text")); // NOI18N
        lblPassword.setMaximumSize(new java.awt.Dimension(71, 17));
        lblPassword.setMinimumSize(new java.awt.Dimension(71, 17));
        lblPassword.setPreferredSize(new java.awt.Dimension(71, 17));

        txtPassword.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtPassword.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(lblBind, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(68, 68, 68)
                        .add(txtBind, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 570, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(lblPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(7, 7, 7)
                        .add(txtPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtBind, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBind))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        lblKrb5LoginConf.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5LoginConf.text")); // NOI18N
        lblKrb5LoginConf.setPreferredSize(new java.awt.Dimension(71, 17));

        txtKrb5Username.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Username.text")); // NOI18N
        txtKrb5Username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKrb5UsernameActionPerformed(evt);
            }
        });

        txtKrb5Password.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Password.text")); // NOI18N

        txtKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Keytab.text")); // NOI18N

        btnKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.btnKrb5Keytab.text")); // NOI18N
        btnKrb5Keytab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKrb5KeytabActionPerformed(evt);
            }
        });

        lblKrb5Username.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Username.text")); // NOI18N

        lblKrb5Password.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Password.text")); // NOI18N

        lblKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Keytab.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(lblKrb5LoginConf, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(35, 35, 35)
                        .add(cbKrb5LoginConf, 0, 564, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblKrb5Username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblKrb5Password)
                            .add(lblKrb5Keytab))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtKrb5Password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                            .add(txtKrb5Username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(txtKrb5Keytab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnKrb5Keytab)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblKrb5LoginConf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbKrb5LoginConf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtKrb5Username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblKrb5Username))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtKrb5Password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblKrb5Password))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtKrb5Keytab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnKrb5Keytab)
                    .add(lblKrb5Keytab))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        DefaultComboBoxModel cbKrb5LoginConfModel = new DefaultComboBoxModel(Krb5LoginConf.values());

        cbKrb5LoginConf.setModel(cbKrb5LoginConfModel);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        org.jdesktop.layout.GroupLayout pnlConnectionLayout = new org.jdesktop.layout.GroupLayout(pnlConnection);
        pnlConnection.setLayout(pnlConnectionLayout);
        pnlConnectionLayout.setHorizontalGroup(
            pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlConnectionLayout.createSequentialGroup()
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlConnectionLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlConnectionLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pnlConnectionLayout.createSequentialGroup()
                                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblLabel)
                                    .add(lblHost)
                                    .add(lblPort))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtHostname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlConnectionLayout.createSequentialGroup()
                                .add(lblTimeout)
                                .add(25, 25, 25)
                                .add(txtTimeout, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))
                            .add(pnlConnectionLayout.createSequentialGroup()
                                .add(lblBaseDn)
                                .add(55, 55, 55)
                                .add(txtBaseDn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))))
                    .add(pnlConnectionLayout.createSequentialGroup()
                        .add(127, 127, 127)
                        .add(cbSecure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))
                    .add(pnlConnectionLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(lblAuthentication)
                        .add(18, 18, 18)
                        .add(cbAuthentication, 0, 591, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlConnectionLayout.setVerticalGroup(
            pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlConnectionLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblLabel)
                    .add(txtLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblHost)
                    .add(txtHostname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPort)
                    .add(txtPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtTimeout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTimeout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblBaseDn)
                    .add(txtBaseDn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbSecure)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlConnectionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbAuthentication, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblAuthentication))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTabbed.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.pnlConnection.TabConstraints.tabTitle"), pnlConnection); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTabbed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlTabbed)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbAuthenticationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAuthenticationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAuthenticationActionPerformed

    private void txtKrb5UsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKrb5UsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKrb5UsernameActionPerformed

    private void btnKrb5KeytabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKrb5KeytabActionPerformed
        JFileChooser jfc = new JFileChooser(getKrb5Keytab());
        int returnValue = jfc.showOpenDialog(this);
        if ( returnValue == JFileChooser.APPROVE_OPTION ) {
            this.setKrb5Keytab(jfc.getSelectedFile());
        }
    }//GEN-LAST:event_btnKrb5KeytabActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKrb5Keytab;
    private javax.swing.JComboBox cbAuthentication;
    private javax.swing.JComboBox cbKrb5LoginConf;
    private javax.swing.JCheckBox cbSecure;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAuthentication;
    private javax.swing.JLabel lblBaseDn;
    private javax.swing.JLabel lblBind;
    private javax.swing.JLabel lblHost;
    private javax.swing.JLabel lblKrb5Keytab;
    private javax.swing.JLabel lblKrb5LoginConf;
    private javax.swing.JLabel lblKrb5Password;
    private javax.swing.JLabel lblKrb5Username;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblTimeout;
    private javax.swing.JPanel pnlConnection;
    private javax.swing.JTabbedPane pnlTabbed;
    private javax.swing.JTextField txtBaseDn;
    private javax.swing.JTextField txtBind;
    private javax.swing.JTextField txtHostname;
    private javax.swing.JTextField txtKrb5Keytab;
    private javax.swing.JTextField txtKrb5Password;
    private javax.swing.JTextField txtKrb5Username;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtTimeout;
    // End of variables declaration//GEN-END:variables
}
