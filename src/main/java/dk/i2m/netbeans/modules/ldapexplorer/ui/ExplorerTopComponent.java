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

import dk.i2m.netbeans.modules.ldapexplorer.LdapService;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.Utilities;

/**
 * {@link TopComponent} implementing an explorer view of an LDAP service.
 */
final class ExplorerTopComponent extends TopComponent {

    private static ExplorerTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final String ICON_PATH =
            "dk/i2m/netbeans/modules/ldapexplorer/resources/address_book_32.png";
    private static final String PREFERRED_ID = "ExplorerTopComponent";
    private DefaultTreeModel nodes = null;
    private DefaultMutableTreeNode root = null;

    private ExplorerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ExplorerTopComponent.class,
                "CTL_ExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(ExplorerTopComponent.class,
                "HINT_ExplorerTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));

        TreeSelectionListener nodeSelect = new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.getNewLeadSelectionPath() == null) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().
                        getLastPathComponent();

                String ldapUrl = "ldap://" + txtHost.getText() + ":" + txtPort.
                        getText() + "/" + txtBaseDN.getText();

                try {
                    TreePath path = e.getNewLeadSelectionPath();

                    StringBuffer dn = new StringBuffer();

                    int i = 0;
                    for (Object obj : path.getPath()) {
                        // Ignore root
                        if (i > 0) {
                            if (i > 1) {
                                dn.insert(0, ",");
                            }
                            dn.insert(0, obj.toString());
                        }
                        i++;
                    }
                    StringBuffer sb = new StringBuffer("dn: ");
                    sb.append(dn.toString());
                    sb.append(",");
                    sb.append(txtBaseDN.getText());

                    sb.append(System.getProperty("line.separator"));


                    String auth = (String) cbAuthentication.getSelectedItem();
                    Map<String, String> attributes;
                    if (auth.equalsIgnoreCase(LdapService.AUTHENTICATION_SIMPLE)) {
                        sb.append(LdapService.getDefault().getAttributes(ldapUrl, txtUser.
                                getText(), new String(txtPassword.getPassword()), dn.
                                toString()));
                        attributes = LdapService.getDefault().getAttributeMap(
                                ldapUrl, txtUser.getText(), new String(txtPassword.
                                getPassword()), dn.toString());
                    } else {
                        sb.append(LdapService.getDefault().getAttributes(ldapUrl, dn.
                                toString()));
                        attributes = LdapService.getDefault().getAttributeMap(
                                ldapUrl, "", "", dn.toString());
                    }

                    txtOutput.setText(sb.toString());

                    String dnValue = dn.toString() + "," + txtBaseDN.getText();
                    DefaultTableModel model = (DefaultTableModel) tblAttributes.
                            getModel();
                    model.setRowCount(0);
                    model.addRow(new Object[]{"dn", dnValue});
                    for (String key : attributes.keySet()) {
                        String val = attributes.get(key);
                        model.addRow(new Object[]{key, val});
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        };

        ldapTree.addTreeSelectionListener(nodeSelect);
    }

    /**
     * Gets the tree of the current LDAP connection.
     *
     * @return {@link TreeModel} representing the tree of the current LDAP
     *         connection
     */
    public TreeModel getNodes() {
        if (nodes == null) {
            root = new DefaultMutableTreeNode("not connected");
            nodes = new DefaultTreeModel(root);
        }

        return nodes;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHost = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        lblBaseDN = new javax.swing.JLabel();
        txtBaseDN = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbAuthentication = new javax.swing.JComboBox();
        lblUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnConnect = new javax.swing.JButton();
        treeDetailSplitter = new javax.swing.JSplitPane();
        overviewTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        ldapTree = new javax.swing.JTree();
        detailTabbedPane = new javax.swing.JTabbedPane();
        formPane = new javax.swing.JScrollPane();
        tblAttributes = new javax.swing.JTable();
        ldifPane = new javax.swing.JScrollPane();
        txtOutput = new javax.swing.JTextPane();

        org.openide.awt.Mnemonics.setLocalizedText(lblHost, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblHost.text")); // NOI18N

        txtHost.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtHost.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblPort, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblPort.text")); // NOI18N

        txtPort.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtPort.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblBaseDN, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblBaseDN.text")); // NOI18N
        lblBaseDN.setToolTipText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblBaseDN.toolTipText")); // NOI18N

        txtBaseDN.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtBaseDN.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.jLabel1.text")); // NOI18N

        cbAuthentication.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Simple" }));
        cbAuthentication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAuthenticationActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lblUser, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblUser.text")); // NOI18N

        txtUser.setEditable(false);
        txtUser.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtUser.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblPassword, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.lblPassword.text")); // NOI18N

        txtPassword.setEditable(false);
        txtPassword.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtPassword.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btnConnect, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnConnect.text")); // NOI18N
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        treeDetailSplitter.setDividerLocation(200);
        treeDetailSplitter.setDividerSize(4);

        ldapTree.setModel(getNodes());
        ldapTree.setMinimumSize(new java.awt.Dimension(200, 0));
        ldapTree.setPreferredSize(new java.awt.Dimension(200, 76));
        jScrollPane3.setViewportView(ldapTree);

        overviewTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.jScrollPane3.TabConstraints.tabTitle"), jScrollPane3); // NOI18N

        treeDetailSplitter.setLeftComponent(overviewTabbedPane);

        tblAttributes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attribute", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        formPane.setViewportView(tblAttributes);

        detailTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.formPane.TabConstraints.tabTitle"), formPane); // NOI18N

        ldifPane.setViewportView(txtOutput);

        detailTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.ldifPane.TabConstraints.tabTitle"), ldifPane); // NOI18N

        treeDetailSplitter.setRightComponent(detailTabbedPane);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(lblHost)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtHost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblPort)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblBaseDN)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtBaseDN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbAuthentication, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblUser)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblPassword)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtPassword)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(btnConnect))
            .add(treeDetailSplitter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblHost)
                    .add(txtHost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPort)
                    .add(txtPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBaseDN)
                    .add(txtBaseDN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnConnect))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(cbAuthentication, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblUser)
                    .add(txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblPassword)
                    .add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(treeDetailSplitter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Recurring method for building the LDAP browsing tree.
     *
     * @param parent
     *          Node for which to add the children
     * @param url
     *          URL of the LDAP service
     * @param dn
     *          Distinguished name of the node to add to the parent
     */
    private void buildTree(DefaultMutableTreeNode parent, String url, String dn) {
        LdapService ldapService = LdapService.getDefault();

        try {
            String children[];
            String auth = (String) cbAuthentication.getSelectedItem();
            if (auth.equalsIgnoreCase(LdapService.AUTHENTICATION_SIMPLE)) {
                children = ldapService.getChildren(url, txtUser.getText(), new String(txtPassword.
                        getPassword()), dn);
            } else {
                children = ldapService.getChildren(url, dn);
            }


            for (String child : children) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);

                String newDN = child;
                if (!dn.isEmpty()) {
                    newDN = newDN + "," + dn;
                }

                buildTree(node, url, newDN);
                parent.add(node);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    /**
     * Event handler for when the "Connect" button is pressed.
     *
     * @param evt
     *          Event that invoked the handler
     */
    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed

        String ldapUrl = "ldap://" + txtHost.getText() + ":" + txtPort.getText()
                + "/" + txtBaseDN.getText();

        this.root = new DefaultMutableTreeNode(ldapUrl);

        buildTree(root, ldapUrl, "");

        this.nodes = new DefaultTreeModel(root);
        this.ldapTree.setModel(nodes);
        this.ldapTree.revalidate();
    }//GEN-LAST:event_btnConnectActionPerformed

    private void cbAuthenticationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAuthenticationActionPerformed
        String selectedItem = (String) cbAuthentication.getSelectedItem();

        if (selectedItem.equalsIgnoreCase("none")) {
            txtUser.setEditable(false);
            txtPassword.setEditable(false);
        } else {
            txtUser.setEditable(true);
            txtPassword.setEditable(true);
        }
    }//GEN-LAST:event_cbAuthenticationActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JComboBox cbAuthentication;
    private javax.swing.JTabbedPane detailTabbedPane;
    private javax.swing.JScrollPane formPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBaseDN;
    private javax.swing.JLabel lblHost;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTree ldapTree;
    private javax.swing.JScrollPane ldifPane;
    private javax.swing.JTabbedPane overviewTabbedPane;
    private javax.swing.JTable tblAttributes;
    private javax.swing.JSplitPane treeDetailSplitter;
    private javax.swing.JTextField txtBaseDN;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextPane txtOutput;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized ExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new ExplorerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ExplorerTopComponent instance. Never call {@link #getDefault}
     * directly!
     */
    public static synchronized ExplorerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(
                PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ExplorerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID
                    + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ExplorerTopComponent) {
            return (ExplorerTopComponent) win;
        }
        Logger.getLogger(ExplorerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ExplorerTopComponent.getDefault();
        }
    }
}
