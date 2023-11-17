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
package dk.i2m.netbeans.modules.ldapexplorer.ui.actions;

import dk.i2m.netbeans.modules.ldapexplorer.model.ConnectionException;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServerNode;
import dk.i2m.netbeans.modules.ldapexplorer.ui.ExplorerTopComponent;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Explorer component.
 */
public class ExplorerAction extends AbstractAction {

    private static final Logger LOG
            = Logger.getLogger(ExplorerAction.class.getName());

    private static ResourceBundle bundle = NbBundle.getBundle(
            ExplorerAction.class);

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ExplorerAction() {
        super(bundle.getString("CTL_ExplorerAction"));
        putValue(SMALL_ICON, ImageUtilities.loadImageIcon(bundle.getString(
                "ICON_ExplorerAction"), true));
    }

    /**
     * Event handler that connects to the selected server and opens the
     * explorer window.
     *
     * @param evt
     *          Event that invoked the handler
     */
    @Override
    public void actionPerformed(ActionEvent evt) {

        // Fetch currently selected LdapServerNode
        LdapServerNode node = Utilities.actionsGlobalContext().lookup(
                LdapServerNode.class);

        // Ensure that an LdapServerNode is indeed selected or found
        if (node != null) {
            try {

                // Connect to the server contained in the selected node
                node.getServer().connect();

                // Open the explorer window for the server connection
                TopComponent win = new ExplorerTopComponent();
                win.open();
                win.requestActive();
            } catch (ConnectionException ex) {
                LOG.log(Level.INFO, "Failed to establish connection", ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Could not connect", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
