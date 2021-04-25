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

import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServersNotifier;
import dk.i2m.netbeans.modules.ldapexplorer.services.LdapService;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import dk.i2m.netbeans.modules.ldapexplorer.ui.LdapServerPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class NewLdapServer extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger(NewLdapServer.class.getName());

    LdapServerPanel panel;

    @Override
    public void performAction() {
        panel = new LdapServerPanel();
        panel.setTimeout(Integer.valueOf(NbBundle.getMessage(
                NewLdapServer.class, "txtTimeoutDefault")));
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == DialogDescriptor.CANCEL_OPTION) {
                    return;
                }

                LdapServer ldapServer = new LdapServer();
                ldapServer.setLabel(panel.getLabel());
                ldapServer.setHost(panel.getHostname());
                ldapServer.setPort(panel.getPort());
                ldapServer.setTimeout(panel.getTimeout());
                ldapServer.setBaseDN(panel.getBaseDN());
                ldapServer.setAuthentication(panel.getAuthentication());
                ldapServer.setBinding(panel.getBind());
                ldapServer.setPassword(panel.getPassword());
                ldapServer.setSecure(panel.isSecureSocketLayerEnabled());
                ldapServer.setKrb5LoginConf(panel.getKrb5LoginConf());
                ldapServer.setKrb5keytab(panel.getKrb5Keytab());
                ldapServer.setKrb5username(panel.getKrb5Username());
                ldapServer.setKrb5password(panel.getKrb5Password());

                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        LdapService.getDefault().save(ldapServer);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                        } catch (ExecutionException ex) {
                            LOG.log(Level.WARNING, null, ex);
                            JOptionPane.showMessageDialog(null, ex.getCause().getMessage());
                        } catch (InterruptedException ex) {
                            LOG.log(Level.WARNING, null, ex);
                        }
                        LdapServersNotifier.changed();
                    }
                }.execute();
            }
        };

        DialogDescriptor d = new DialogDescriptor(panel, NbBundle.getMessage(
                NewLdapServer.class, "CTL_NewLdapServer"), true, listener);
        DialogDisplayer.getDefault().notifyLater(d);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewLdapServer.class, "CTL_NewLdapServer");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(NewLdapServer.class, "ICON_NewLdapServer");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
