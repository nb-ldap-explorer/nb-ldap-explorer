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
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import dk.i2m.netbeans.modules.ldapexplorer.model.QueryException;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Hex;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.util.Utilities;

/**
 * {@link TopComponent} implementing an explorer view of an LDAP service.
 */
public final class ExplorerTopComponent extends TopComponent implements
        ExplorerManager.Provider, LookupListener {

    private ExplorerManager em = new ExplorerManager();
    private ResourceBundle bundle = NbBundle.getBundle(
            ExplorerTopComponent.class);
    private static final String PREFERRED_ID = "ExplorerTopComponent";
    private LdapServer server;
    private Lookup.Result result = null;

    public ExplorerTopComponent() {
        initComponents();

        // Omit LookUps when a change is made in the explorermanager
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        em.setRootContext(new LdapEntryNode(new LdapEntryChildren()));

        setName(bundle.getString("CTL_ExplorerTopComponent"));
        setToolTipText(bundle.getString("HINT_ExplorerTopComponent"));
        setIcon(ImageUtilities.loadImage(bundle.getString(
                "ICON_ExplorerTopComponent"), true));
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
        // Find the current LdapServer that was selected
        LdapServerNode node = Utilities.actionsGlobalContext().lookup(
                LdapServerNode.class);

        // Listen for changes in the selection of LdapEntryNodes
        Lookup.Template tpl = new Lookup.Template(LdapEntryNode.class);
        result = Utilities.actionsGlobalContext().lookup(tpl);
        result.addLookupListener(this);
        resultChanged(null);

        if (node != null) {
            this.server = node.getServer();
            setDisplayName(this.server.toString());
            em.getRootContext().setDisplayName(this.server.getBaseDN());
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
        if (bundle.containsKey(LBL_PREFIX + att)) {
            attName = bundle.getString(LBL_PREFIX + att);
        }

        if (val instanceof byte[]) {
            // Field is binary - convert it to hexadecimals
            model.addRow(
                    new Object[]{attName, Hex.encodeHexString((byte[]) val)});
        } else {
            model.addRow(new Object[]{attName, val});
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAttributes = new javax.swing.JTable();
        pnlLdif = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLdif = new javax.swing.JTextPane();
        jScrollPane3 = new BeanTreeView();

        splitPane.setDividerLocation(170);
        splitPane.setDividerSize(5);

        tblAttributes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Attribute", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAttributes);

        org.jdesktop.layout.GroupLayout pnlAttributesLayout = new org.jdesktop.layout.GroupLayout(pnlAttributes);
        pnlAttributes.setLayout(pnlAttributesLayout);
        pnlAttributesLayout.setHorizontalGroup(
            pnlAttributesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnlAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAttributesLayout.setVerticalGroup(
            pnlAttributesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlAttributesLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlAttributes.TabConstraints.tabTitle"), pnlAttributes); // NOI18N

        txtLdif.setEditable(false);
        txtLdif.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jScrollPane2.setViewportView(txtLdif);

        org.jdesktop.layout.GroupLayout pnlLdifLayout = new org.jdesktop.layout.GroupLayout(pnlLdif);
        pnlLdif.setLayout(pnlLdifLayout);
        pnlLdifLayout.setHorizontalGroup(
            pnlLdifLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlLdifLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlLdifLayout.setVerticalGroup(
            pnlLdifLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlLdifLayout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedDetails.addTab(org.openide.util.NbBundle.getMessage(ExplorerTopComponent.class, "ExplorerTopComponent.pnlLdif.TabConstraints.tabTitle"), pnlLdif); // NOI18N

        splitPane.setRightComponent(tabbedDetails);
        splitPane.setLeftComponent(jScrollPane3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(27, 27, 27)
                .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel pnlAttributes;
    private javax.swing.JPanel pnlLdif;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedDetails;
    private javax.swing.JTable tblAttributes;
    private javax.swing.JTextPane txtLdif;
    // End of variables declaration//GEN-END:variables
}
