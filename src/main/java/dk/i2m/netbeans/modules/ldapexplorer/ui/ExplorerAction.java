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

import dk.i2m.netbeans.modules.ldapexplorer.model.ConnectionException;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
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

    private static ResourceBundle bundle = NbBundle.getBundle(
            ExplorerAction.class);

    public ExplorerAction() {
        super(bundle.getString("CTL_ExplorerAction"));
        putValue(SMALL_ICON, ImageUtilities.loadImageIcon(bundle.getString(
                "ICON_ExplorerTopComponent"), true));
    }

    /**
     * Event handler that connects to the selected server and opens the
     * explorer window.
     *
     * @param evt
     *          Event that invoked the handler
     */
    public void actionPerformed(ActionEvent evt) {
        LdapServerNode node = Utilities.actionsGlobalContext().lookup(
                LdapServerNode.class);
        if (node != null) {
            try {
                node.getServer().connect();

                TopComponent win = new ExplorerTopComponent();
                win.open();
                win.requestActive();
            } catch (ConnectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Could not connect", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
