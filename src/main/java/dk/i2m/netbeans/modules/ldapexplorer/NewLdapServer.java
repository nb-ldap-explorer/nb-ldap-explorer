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
package dk.i2m.netbeans.modules.ldapexplorer;

import dk.i2m.netbeans.modules.ldapexplorer.model.Authentication;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import dk.i2m.netbeans.modules.ldapexplorer.ui.LdapServerPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class NewLdapServer extends CallableSystemAction {

    LdapServerPanel panel;

    public void performAction() {
        panel = new LdapServerPanel();
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.CANCEL_OPTION) {
                    return;
                }

                LdapServer ldapServer = new LdapServer();
                ldapServer.setHost(panel.getHostname());
                ldapServer.setPort(panel.getPort());
                ldapServer.setBaseDN(panel.getBaseDN());
                ldapServer.setAuthentication(Authentication.valueOf(panel.
                        getAuthentication()));
                ldapServer.setBinding(panel.getBind());
                ldapServer.setPassword(panel.getPassword());
                ldapServer.setSecure(panel.isSecureSocketLayerEnabled());

                try {
                    LdapService.getDefault().save(ldapServer);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                LdapServersNotifier.changed();
            }
        };

        DialogDescriptor d = new DialogDescriptor(panel, "New LDAP Server", true,
                listener);
        DialogDisplayer.getDefault().notifyLater(d);
    }

    public String getName() {
        return NbBundle.getMessage(NewLdapServer.class, "CTL_NewLdapServer");
    }

    @Override
    protected String iconResource() {
        return "dk/i2m/netbeans/modules/ldapexplorer/resources/server_new.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
