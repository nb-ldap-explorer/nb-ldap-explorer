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
import dk.i2m.netbeans.modules.ldapexplorer.model.SearchContext;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Hex;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.*;
import org.openide.windows.TopComponent;
import org.openide.windows.CloneableTopComponent;

/**
 * {@link TopComponent} implementing an explorer view of an LDAP service.
 */
public final class ExplorerTopComponent extends CloneableTopComponent implements
        ExplorerManager.Provider, LookupListener {

    private static final Logger LOG
            = Logger.getLogger(ExplorerTopComponent.class.getName());

    private ExplorerManager em = new ExplorerManager();
    private ResourceBundle bundle = NbBundle.getBundle(
            ExplorerTopComponent.class);
    private static final String PREFERRED_ID = "ExplorerTopComponent";
    private LdapServer server;
    private Lookup.Result result = null;
    private boolean inQuery = false;
    private SearchContext searchContext = new SearchContext();

    /**
     * Creates a new instance of {@link ExplorerTopComponent}.
     */
    public ExplorerTopComponent() {
        // Let the ExplorerManager dynamically put nodes in the lookup upon
        // selection
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        em.setRootContext(new LdapEntryNode(new LdapEntryChildren()));
        setName(bundle.getString("CTL_ExplorerTopComponent"));
        setIcon(ImageUtilities.loadImage(bundle.getString(
                "ICON_ExplorerTopComponent"), true));

        initComponents();
        this.addPropertyChangeListener("inQuery", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Boolean newValue = (Boolean) pce.getNewValue();
                assert (newValue != null);
                txtFilter.setEnabled(!newValue);
                btnFilter.setEnabled(!newValue);
                btnReset.setEnabled(!newValue);
                treePane.setEnabled(!newValue);
            }
        });
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
        if (this.searchContext != null) {
            try {
                this.searchContext.cancel();
            } catch (RuntimeException ex) {
                LOG.log(Level.WARNING, "Failed to cancel search", ex);
            }
        }
        if (this.server != null) {
            try {
                this.server.disconnect();
            } catch (ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "An error occurred", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    /**
     * Event handler for when an {@link LdapEntryNode} is selected.
     *
     * @param ev Event that invoked the handler
     */
    @Override
    public void resultChanged(LookupEvent ev) {

        Collection c = result.allInstances();

        // Get the model of the attribute table
        DefaultTableModel model =
                (DefaultTableModel) tblAttributes.getModel();
        model.setRowCount(0);

        txtLdif.setText("");

        if (!c.isEmpty()) {
            LdapEntryNode e = (LdapEntryNode) c.iterator().next();

            try {
            // Get the LdapEntry associated with this node
            LdapEntry entry = e.getLookup().lookup(LdapEntry.class);

            // Show LdapEntry if it was found for this node
            // Note: On the root node there is no LdapEntry, hence the check
            if (entry != null) {
                    entry = server.getEntry(entry.getDn());
                    ldifPane.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
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

        this.searchContext.cancel();
        this.searchContext = new SearchContext();
        LdapEntryChildren children = new LdapEntryChildren();
        children.setLdapServer(server);
        children.setParent(this.server.getBaseDN());
        children.setCtx(searchContext);
        em.setRootContext(new LdapEntryNode(children));
        em.getRootContext().setDisplayName(this.server.getBaseDN());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

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

        setLayout(new java.awt.BorderLayout());

        splitPane.setDividerLocation(170);

        pnlAttributes.setLayout(new java.awt.BorderLayout());

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
        tblAttributes.setShowGrid(true);
        tblAttributes.setShowHorizontalLines(true);
        tblAttributes.setShowVerticalLines(true);
        attributePane.setViewportView(tblAttributes);
        if (tblAttributes.getColumnModel().getColumnCount() > 0) {
            tblAttributes.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblAttributes.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title0")); // NOI18N
            tblAttributes.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblAttributes.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title2")); // NOI18N
            tblAttributes.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblAttributes.getColumnModel().getColumn(2).setHeaderValue(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.tblAttributes.columnModel.title1")); // NOI18N
        }

        pnlAttributes.add(attributePane, java.awt.BorderLayout.CENTER);

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlAttributes.TabConstraints.tabTitle"), pnlAttributes); // NOI18N

        pnlLdif.setLayout(new java.awt.BorderLayout());

        txtLdif.setEditable(false);
        txtLdif.setEditorKit(CloneableEditorSupport.getEditorKit("text/ldif"));
        ldifPane.setViewportView(txtLdif);

        pnlLdif.add(ldifPane, java.awt.BorderLayout.CENTER);

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlLdif.TabConstraints.tabTitle"), pnlLdif); // NOI18N

        splitPane.setRightComponent(tabbedDetails);
        splitPane.setLeftComponent(treePane);

        add(splitPane, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        txtFilter.setText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtFilter.text")); // NOI18N
        txtFilter.setToolTipText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.txtFilter.toolTipText")); // NOI18N
        txtFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel1.add(txtFilter, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnFilter, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnFilter.text")); // NOI18N
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel1.add(btnFilter, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnReset, org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnReset.text")); // NOI18N
        btnReset.setToolTipText(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.btnReset.toolTipText")); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel1.add(btnReset, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void executeFilter() {
        final String filterText = txtFilter.getText().trim();
        if (filterText.isEmpty()) {
            prepareBrowsing();
        } else {
            this.searchContext.cancel();
            this.searchContext = new SearchContext();
            LdapSearchEntryChildren children = new LdapSearchEntryChildren();
            children.setLdapServer(server);
            children.setSearch(filterText);
            children.setCtx(searchContext);
            em.setRootContext(new LdapSearchEntryNode(children));
        }
    }

    public boolean isInQuery() {
        return inQuery;
    }

    public void setInQuery(boolean inQuery) {
        boolean oldValue = this.inQuery;
        this.inQuery = inQuery;
        firePropertyChange("inQuery", oldValue, this.inQuery);
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
