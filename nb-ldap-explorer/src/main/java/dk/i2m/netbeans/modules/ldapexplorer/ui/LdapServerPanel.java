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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openide.awt.HtmlBrowser.URLDisplayer;

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

    public boolean isIgnoreTlsErrors() {
        return this.cbIgnoreTlsErrors.isSelected();
    }

    public void setIgnoreTlsErrors(boolean ignoreTlsErrors) {
        this.cbIgnoreTlsErrors.setSelected(ignoreTlsErrors);
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        lblLabel = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        lblTimeout = new javax.swing.JLabel();
        txtTimeout = new javax.swing.JTextField();
        cbSecure = new javax.swing.JCheckBox();
        cbIgnoreTlsErrors = new javax.swing.JCheckBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        pnlSecuritySimple = new javax.swing.JPanel();
        lblBind = new javax.swing.JLabel();
        txtBind = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        pnlSecurityKerberos5 = new javax.swing.JPanel();
        lblKrb5LoginConf = new javax.swing.JLabel();
        cbKrb5LoginConf = new javax.swing.JComboBox();
        lblKrb5Username = new javax.swing.JLabel();
        txtKrb5Username = new javax.swing.JTextField();
        lblKrb5Password = new javax.swing.JLabel();
        txtKrb5Password = new javax.swing.JTextField();
        lblKrb5Keytab = new javax.swing.JLabel();
        txtKrb5Keytab = new javax.swing.JTextField();
        btnKrb5Keytab = new javax.swing.JButton();
        kerberosInfoScrollPane = new javax.swing.JScrollPane();
        kerberosInfoTextPane = new javax.swing.JEditorPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        pnlConnection.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlConnection.setLayout(new java.awt.GridBagLayout());

        lblHost.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblHost.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblHost, gridBagConstraints);

        txtHostname.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtHostname.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(txtHostname, gridBagConstraints);

        lblPort.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblPort.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblPort, gridBagConstraints);

        txtPort.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtPort.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(txtPort, gridBagConstraints);

        lblBaseDn.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblBaseDn.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblBaseDn, gridBagConstraints);

        txtBaseDn.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtBaseDn.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(txtBaseDn, gridBagConstraints);

        lblAuthentication.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblAuthentication.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblAuthentication, gridBagConstraints);

        cbAuthentication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAuthenticationActionPerformed(evt);
            }
        });
        DefaultComboBoxModel cbAuthenticationModel = new DefaultComboBoxModel(Authentication.values());

        cbAuthentication.setModel(cbAuthenticationModel);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(cbAuthentication, gridBagConstraints);

        lblLabel.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblLabel, gridBagConstraints);

        txtLabel.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlConnection.add(txtLabel, gridBagConstraints);

        lblTimeout.setLabelFor(txtTimeout);
        lblTimeout.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblTimeout.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConnection.add(lblTimeout, gridBagConstraints);

        txtTimeout.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtTimeout.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(txtTimeout, gridBagConstraints);

        cbSecure.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.cbSecure.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(cbSecure, gridBagConstraints);

        cbIgnoreTlsErrors.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.cbIgnoreTlsErrors.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlConnection.add(cbIgnoreTlsErrors, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlConnection.add(filler1, gridBagConstraints);

        pnlTabbed.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.pnlConnection.TabConstraints.tabTitle"), pnlConnection); // NOI18N

        pnlSecuritySimple.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlSecuritySimple.setAlignmentY(0.0F);
        pnlSecuritySimple.setLayout(new java.awt.GridBagLayout());

        lblBind.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblBind.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSecuritySimple.add(lblBind, gridBagConstraints);

        txtBind.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtBind.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlSecuritySimple.add(txtBind, gridBagConstraints);

        lblPassword.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblPassword.text")); // NOI18N
        lblPassword.setMaximumSize(new java.awt.Dimension(71, 17));
        lblPassword.setMinimumSize(new java.awt.Dimension(71, 17));
        lblPassword.setPreferredSize(new java.awt.Dimension(71, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSecuritySimple.add(lblPassword, gridBagConstraints);

        txtPassword.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtPassword.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSecuritySimple.add(txtPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlSecuritySimple.add(filler2, gridBagConstraints);

        pnlTabbed.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.pnlSecuritySimple.TabConstraints.tabTitle"), pnlSecuritySimple); // NOI18N

        pnlSecurityKerberos5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlSecurityKerberos5.setLayout(new java.awt.GridBagLayout());

        lblKrb5LoginConf.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5LoginConf.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        pnlSecurityKerberos5.add(lblKrb5LoginConf, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        pnlSecurityKerberos5.add(cbKrb5LoginConf, gridBagConstraints);
        DefaultComboBoxModel cbKrb5LoginConfModel = new DefaultComboBoxModel(Krb5LoginConf.values());

        cbKrb5LoginConf.setModel(cbKrb5LoginConfModel);

        lblKrb5Username.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Username.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        pnlSecurityKerberos5.add(lblKrb5Username, gridBagConstraints);

        txtKrb5Username.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Username.text")); // NOI18N
        txtKrb5Username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKrb5UsernameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        pnlSecurityKerberos5.add(txtKrb5Username, gridBagConstraints);

        lblKrb5Password.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Password.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        pnlSecurityKerberos5.add(lblKrb5Password, gridBagConstraints);

        txtKrb5Password.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Password.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        pnlSecurityKerberos5.add(txtKrb5Password, gridBagConstraints);

        lblKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.lblKrb5Keytab.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        pnlSecurityKerberos5.add(lblKrb5Keytab, gridBagConstraints);

        txtKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.txtKrb5Keytab.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 331;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        pnlSecurityKerberos5.add(txtKrb5Keytab, gridBagConstraints);

        btnKrb5Keytab.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.btnKrb5Keytab.text")); // NOI18N
        btnKrb5Keytab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKrb5KeytabActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        pnlSecurityKerberos5.add(btnKrb5Keytab, gridBagConstraints);

        kerberosInfoTextPane.setBackground(new java.awt.Color(254, 254, 254));
        kerberosInfoTextPane.setContentType(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.kerberosInfoTextPane.contentType_1")); // NOI18N
        kerberosInfoTextPane.setEditable(false);
        kerberosInfoTextPane.addHyperlinkListener(
            new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    if( he.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
                        URLDisplayer.getDefault().showURL(he.getURL());
                    }
                }
            }
        );
        kerberosInfoTextPane.setText(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.kerberosInfoTextPane.text")); // NOI18N
        kerberosInfoTextPane.setPreferredSize(new java.awt.Dimension(1000, 400));
        kerberosInfoScrollPane.setViewportView(kerberosInfoTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlSecurityKerberos5.add(kerberosInfoScrollPane, gridBagConstraints);

        pnlTabbed.addTab(org.openide.util.NbBundle.getMessage(LdapServerPanel.class, "LdapServerPanel.pnlSecurityKerberos5.TabConstraints.tabTitle"), pnlSecurityKerberos5); // NOI18N

        add(pnlTabbed);
    }// </editor-fold>//GEN-END:initComponents

    private void cbAuthenticationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAuthenticationActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbAuthenticationActionPerformed

    private void btnKrb5KeytabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKrb5KeytabActionPerformed
        JFileChooser jfc = new JFileChooser(getKrb5Keytab());
        int returnValue = jfc.showOpenDialog(this);
        if ( returnValue == JFileChooser.APPROVE_OPTION ) {
            this.setKrb5Keytab(jfc.getSelectedFile());
        }
}//GEN-LAST:event_btnKrb5KeytabActionPerformed

    private void txtKrb5UsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKrb5UsernameActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtKrb5UsernameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKrb5Keytab;
    private javax.swing.JComboBox cbAuthentication;
    private javax.swing.JCheckBox cbIgnoreTlsErrors;
    private javax.swing.JComboBox cbKrb5LoginConf;
    private javax.swing.JCheckBox cbSecure;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JScrollPane kerberosInfoScrollPane;
    private javax.swing.JEditorPane kerberosInfoTextPane;
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
    private javax.swing.JPanel pnlSecurityKerberos5;
    private javax.swing.JPanel pnlSecuritySimple;
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
