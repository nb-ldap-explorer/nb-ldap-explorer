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

import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServerNode;
import dk.i2m.netbeans.modules.ldapexplorer.model.ConnectionException;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapEntry;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapEntryChildren;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapEntryNode;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapSearchEntryChildren;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapSearchEntryNode;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import dk.i2m.netbeans.modules.ldapexplorer.model.QueryException;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Hex;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.util.Utilities;
import org.openide.windows.CloneableTopComponent;

/**
 * {@link TopComponent} implementing an explorer view of an LDAP service.
 */
public final class ExplorerTopComponent extends CloneableTopComponent implements
        ExplorerManager.Provider, LookupListener {

    private ExplorerManager em = new ExplorerManager();
    private ResourceBundle bundle = NbBundle.getBundle(
            ExplorerTopComponent.class);
    private static final String PREFERRED_ID = "ExplorerTopComponent";
    private LdapServer server;
    private Lookup.Result result = null;

    /**
     * Creates a new instance of {@link ExplorerTopComponent}.
     */
    public ExplorerTopComponent() {
        initComponents();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        // Let the ExplorerManager dynamically put nodes in the lookup upon
        // selection
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        em.setRootContext(new LdapEntryNode(new LdapEntryChildren()));

        setName(bundle.getString("CTL_ExplorerTopComponent"));
        setIcon(ImageUtilities.loadImage(bundle.getString(
                "ICON_ExplorerTopComponent"), true));

        // Listen for changes in the selection of LdapEntryNodes
        Lookup.Template tpl = new Lookup.Template(LdapEntryNode.class);
        result = getLookup().lookup(tpl);
        result.addLookupListener(this);
        resultChanged(null);

        // Find the current LdapServer that was selected
        LdapServerNode node = Utilities.actionsGlobalContext().lookup(
                LdapServerNode.class);

        if (node != null) {
            this.server = node.getServer();
            prepareBrowsing();
        }
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        if (this.server != null) {
            try {
                this.server.disconnect();
            } catch (ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "An error occurred", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public ExplorerManager getExplorerManager() {
        return em;
    }

    /**
     * Event handler for when an {@link LdapEntryNode} is selected.
     *
     * @param ev
     *          Event that invoked the handler
     */
    public void resultChanged(LookupEvent ev) {

        Collection c = result.allInstances();

        if (!c.isEmpty()) {
            LdapEntryNode e = (LdapEntryNode) c.iterator().next();

            // Get the model of the attribute table
            DefaultTableModel model =
                    (DefaultTableModel) tblAttributes.getModel();
            model.setRowCount(0);

            try {
                // Get the LdapEntry associated with this node
                LdapEntry entry = e.getLookup().lookup(LdapEntry.class);

                // Show LdapEntry if it was found for this node
                // Note: On the root node there is no LdapEntry, hence the check
                if (entry != null) {
                    entry = server.getEntry(entry.getDn());
                    ldifPane.scrollRectToVisible( new Rectangle(0, 0, 1, 1));
                    txtLdif.setText(entry.toLDIF());

                    // Add the DN as it is not part of the attributes
                    addRow(model, "dn", entry.getDn());

                    for (String att : entry.getAttributes().keySet()) {
                        for (Object val : entry.getAttributes().get(att)) {
                            addRow(model, att, val);
                        }
                    }
                } else {
                    txtLdif.setText("");
                }
            } catch (QueryException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void addRow(DefaultTableModel model, String att, Object val) {
        final String LBL_PREFIX = "ATTRIBUTE_FRIENDLY_NAME_";
        String attName = att;
        if (bundle.containsKey(LBL_PREFIX + att.toLowerCase())) {
            attName = bundle.getString(LBL_PREFIX + att.toLowerCase());
        }

        if (val instanceof byte[]) {
            // Field is binary - convert it to hexadecimals
            model.addRow(
                    new Object[]{attName, att, Hex.encodeHexString((byte[]) val)});
        } else {
            model.addRow(new Object[]{attName, att, val});
        }
    }

    private void prepareBrowsing() {
        txtFilter.setText("");
        setToolTipText(this.server.toString());
        if (this.server.isLabelSet()) {
            setDisplayName(this.server.getLabel());
        } else {
            setDisplayName(this.server.toString());
        }

        LdapEntryChildren children = new LdapEntryChildren();
        children.setLdapServer(server);
        children.setParent(this.server.getBaseDN());
        em.setRootContext(new LdapEntryNode(children));
        em.getRootContext().setDisplayName(this.server.getBaseDN());
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

        splitPane = new javax.swing.JSplitPane();
        tabbedDetails = new javax.swing.JTabbedPane();
        pnlAttributes = new javax.swing.JPanel();
        attributePane = new javax.swing.JScrollPane();
        tblAttributes = new javax.swing.JTable();
        pnlLdif = new javax.swing.JPanel();
        ldifPane = new javax.swing.JScrollPane();
        txtLdif = new javax.swing.JEditorPane();
        treePane = new BeanTreeView();
        jPanel1 = new javax.swing.JPanel();
        txtFilter = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        splitPane.setDividerLocation(170);
        splitPane.setDividerSize(5);

        tblAttributes.setAutoCreateRowSorter(true);
        tblAttributes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attribute (name)", "Attribute (technical)", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAttributes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        attributePane.setViewportView(tblAttributes);
        tblAttributes.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblAttributes.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title0")); // NOI18N
        tblAttributes.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblAttributes.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title2")); // NOI18N
        tblAttributes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblAttributes.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title1")); // NOI18N

        org.jdesktop.layout.GroupLayout pnlAttributesLayout = new org.jdesktop.layout.GroupLayout(pnlAttributes);
        pnlAttributes.setLayout(pnlAttributesLayout);
        pnlAttributesLayout.setHorizontalGroup(
            pnlAttributesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .add(attributePane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAttributesLayout.setVerticalGroup(
            pnlAttributesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlAttributesLayout.createSequentialGroup()
                .add(attributePane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlAttributes.TabConstraints.tabTitle"), pnlAttributes); // NOI18N

        txtLdif.setEditable(false);
        txtLdif.setEditorKit(CloneableEditorSupport.getEditorKit("text/ldif"));
        ldifPane.setViewportView(txtLdif);

        org.jdesktop.layout.GroupLayout pnlLdifLayout = new org.jdesktop.layout.GroupLayout(pnlLdif);
        pnlLdif.setLayout(pnlLdifLayout);
        pnlLdifLayout.setHorizontalGroup(
            pnlLdifLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlLdifLayout.createSequentialGroup()
                .addContainerGap()
                .add(ldifPane)
                .addContainerGap())
        );
        pnlLdifLayout.setVerticalGroup(
            pnlLdifLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ldifPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
        );

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlLdif.TabConstraints.tabTitle"), pnlLdif); // NOI18N

        splitPane.setRightComponent(tabbedDetails);
        splitPane.setLeftComponent(treePane);

        txtFilter.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtFilter.text")); // NOI18N
        txtFilter.setToolTipText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtFilter.toolTipText")); // NOI18N
        txtFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFilterActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnFilter, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnFilter.text")); // NOI18N
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnReset, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnReset.text")); // NOI18N
        btnReset.setToolTipText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnReset.toolTipText")); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(txtFilter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnFilter)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnReset))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(txtFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnReset)
                .add(btnFilter))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitPane))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void executeFilter() {
        String filterText = txtFilter.getText().trim();
        if (filterText.isEmpty()) {
            prepareBrowsing();
        } else {
            try {
                List<LdapEntry> searchResults = server.search(filterText);
                LdapSearchEntryChildren children = new LdapSearchEntryChildren(searchResults);
                children.setLdapServer(server);
                em.setRootContext(new LdapSearchEntryNode(children));

            } catch (QueryException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        executeFilter();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtFilter.setText("");
        executeFilter();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFilterActionPerformed
        executeFilter();
    }//GEN-LAST:event_txtFilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane attributePane;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnReset;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane ldifPane;
    private javax.swing.JPanel pnlAttributes;
    private javax.swing.JPanel pnlLdif;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedDetails;
    private javax.swing.JTable tblAttributes;
    private javax.swing.JScrollPane treePane;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JEditorPane txtLdif;
    // End of variables declaration//GEN-END:variables
}
