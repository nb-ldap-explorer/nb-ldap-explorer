/*
 *  Copyright 2010 Allan Lykke Christensen.
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
package dk.i2m.netbeans.modules.ldapexplorer.model;

import dk.i2m.netbeans.modules.ldapexplorer.LdapServersNotifier;
import dk.i2m.netbeans.modules.ldapexplorer.LdapService;
import dk.i2m.netbeans.modules.ldapexplorer.ui.LdapServerPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 * Node representing a single registered {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServerNode extends AbstractNode implements
        PropertyChangeListener {

    private LdapServer server = null;
    private static ResourceBundle bundle = NbBundle.getBundle(
            LdapServerNode.class);

    public LdapServerNode(LdapServer server) {
        super(Children.LEAF, Lookups.singleton(server));
        this.server = server;
        setIconBaseWithExtension(bundle.getString("ICON_LdapServerNode"));
        setDisplayName(server.toString());
        setShortDescription(bundle.getString("HINT_LdapServerNode"));
        server.addPropertyChangeListener(WeakListeners.propertyChange(this,
                server));
    }

    public LdapServer getServer() {
        return server;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set connectionDetails = Sheet.createPropertiesSet();
        connectionDetails.setDisplayName(bundle.getString(
                "PROPSET_NAME_Connection"));
        connectionDetails.setShortDescription(bundle.getString(
                "PROPSET_DESC_Connection"));
        connectionDetails.setName("connection");
        Sheet.Set securityDetails = Sheet.createPropertiesSet();
        securityDetails.setName("security");
        securityDetails.setDisplayName(bundle.getString("PROPSET_NAME_Security"));
        securityDetails.setShortDescription(bundle.getString(
                "PROPSET_DESC_Security"));

        LdapServer srv = getLookup().lookup(LdapServer.class);

        if (srv != null) {

            try {
                Property host = new PropertySupport.Reflection<String>(srv,
                        String.class, "host");
                host.setName(bundle.getString("PROP_NAME_Hostname"));
                host.setShortDescription(bundle.getString("PROP_DESC_Hostname"));
                connectionDetails.put(host);

                Property portProp = new PropertySupport.Reflection<Integer>(srv,
                        Integer.class, "port");
                portProp.setName(bundle.getString("PROP_NAME_Port"));
                portProp.setShortDescription(bundle.getString("PROP_DESC_Port"));
                connectionDetails.put(portProp);

                Property baseDnProp = new PropertySupport.Reflection<String>(srv,
                        String.class, "baseDN");
                baseDnProp.setName(bundle.getString("PROP_NAME_BaseDN"));
                baseDnProp.setShortDescription(bundle.getString(
                        "PROP_DESC_BaseDN"));
                connectionDetails.put(baseDnProp);

                Property sslProp = new PropertySupport.Reflection<Boolean>(
                        srv, Boolean.class, "secure");
                sslProp.setName(bundle.getString("PROP_NAME_SSL"));
                sslProp.setShortDescription(bundle.getString("PROP_DESC_SSL"));
                securityDetails.put(sslProp);

                Property authProp = new PropertySupport.Reflection<Authentication>(
                        srv, Authentication.class, "authentication");
                authProp.setName(bundle.getString("PROP_NAME_Authentication"));
                authProp.setShortDescription(bundle.getString(
                        "PROP_DESC_Authentication"));
                securityDetails.put(authProp);

                Property bindProp = new PropertySupport.Reflection<String>(srv,
                        String.class, "binding");
                bindProp.setName(bundle.getString("PROP_NAME_Bind"));
                bindProp.setShortDescription(bundle.getString("PROP_DESC_Bind"));
                securityDetails.put(bindProp);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }


        sheet.put(connectionDetails);
        sheet.put(securityDetails);


        return sheet;

    }

    @Override
    public Action getPreferredAction() {
        return new ModifyLdapServer();
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{
            new ExplorerAction(),
            null,
            SystemAction.get(DeleteAction.class),
            null,
            new ModifyLdapServer()
        };
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        LdapService.getDefault().delete(server);
        LdapServersNotifier.changed();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        LdapServer srv = getLookup().lookup(LdapServer.class);
        setDisplayName(srv.toString());
        try {
            LdapService.getDefault().save(srv);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private class ModifyLdapServer extends AbstractAction {

        LdapServerPanel panel;

        public ModifyLdapServer() {
            putValue(NAME, bundle.getString("CTL_ModifyLdapServer"));
        }

        public void actionPerformed(ActionEvent e) {

            panel = new LdapServerPanel();
            panel.setHostname(server.getHost());
            panel.setPort(server.getPort());
            panel.setBaseDN(server.getBaseDN());
            panel.setAuthentication(server.getAuthentication().name());
            panel.setBind(server.getBinding());
            panel.setPassword(server.getPassword());
            panel.setSecureSocketLayerEnabled(server.isSecure());

            ActionListener listener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == DialogDescriptor.CANCEL_OPTION) {
                        return;
                    }
                    server.setHost(panel.getHostname());
                    server.setPort(panel.getPort());
                    server.setBaseDN(panel.getBaseDN());
                    server.setAuthentication(Authentication.valueOf(panel.
                            getAuthentication()));
                    server.setBinding(panel.getBind());
                    server.setPassword(panel.getPassword());
                    server.setSecure(panel.isSecureSocketLayerEnabled());

                    try {
                        LdapService.getDefault().save(server);
                        setName(server.toString());
                        LdapServersNotifier.changed();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            };

            DialogDescriptor d = new DialogDescriptor(panel, NbBundle.getMessage(
                    ModifyLdapServer.class, "CTL_ModifyLdapServer"),
                    true, listener);
            DialogDisplayer.getDefault().notifyLater(d);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LdapServerNode other = (LdapServerNode) obj;
        if (this.server != other.server && (this.server == null || !this.server.
                equals(other.server))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.server != null ? this.server.hashCode() : 0);
        return hash;
    }
}
