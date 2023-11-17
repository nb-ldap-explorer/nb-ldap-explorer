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
package dk.i2m.netbeans.modules.ldapexplorer.model;

import dk.i2m.netbeans.modules.ldapexplorer.ui.actions.ExplorerAction;
import dk.i2m.netbeans.modules.ldapexplorer.services.LdapService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.openide.actions.DeleteAction;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Node representing a single registered {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServerNode extends AbstractNode implements
        PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(LdapServerNode.class.getName());

    private LdapServer server = null;
    private static ResourceBundle bundle = NbBundle.getBundle(
            LdapServerNode.class);

    public LdapServerNode(LdapServer server) {
        this(server, new InstanceContent());
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private LdapServerNode(LdapServer server, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(this);
        content.add(server);
        this.server = server;
        setIconBaseWithExtension(bundle.getString("ICON_LdapServerNode"));
        if (server.isLabelSet()) {
            setDisplayName(server.getLabel());
        } else {
            setDisplayName(server.toString());
        }
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
        Sheet.Set krb5securityDetails = Sheet.createPropertiesSet();
        krb5securityDetails.setName("krb5security");
        krb5securityDetails.setDisplayName(bundle.getString("PROPSET_NAME_KRB5Security"));
        krb5securityDetails.setShortDescription(bundle.getString(
                "PROPSET_DESC_KRB5Security"));

        LdapServer srv = getLookup().lookup(LdapServer.class);

        if (srv != null) {

            try {
                Property labelProp = new PropertySupport.Reflection<>(srv,
                        String.class, "label");
                labelProp.setName(bundle.getString("PROP_NAME_Label"));
                labelProp.setShortDescription(
                        bundle.getString("PROP_DESC_Label"));
                connectionDetails.put(labelProp);

                Property host = new PropertySupport.Reflection<>(srv,
                        String.class, "host");
                host.setName(bundle.getString("PROP_NAME_Hostname"));
                host.setShortDescription(bundle.getString("PROP_DESC_Hostname"));
                connectionDetails.put(host);

                Property portProp = new PropertySupport.Reflection<>(srv,
                        Integer.class, "port");
                portProp.setName(bundle.getString("PROP_NAME_Port"));
                portProp.setShortDescription(bundle.getString("PROP_DESC_Port"));
                connectionDetails.put(portProp);

                Property timeoutProp = new PropertySupport.Reflection<>(
                        srv,
                        Integer.class, "timeout");
                timeoutProp.setName(bundle.getString("PROP_NAME_Timeout"));
                timeoutProp.setShortDescription(
                        bundle.getString("PROP_DESC_Timeout"));
                connectionDetails.put(timeoutProp);

                Property baseDnProp = new PropertySupport.Reflection<>(srv,
                        String.class, "baseDN");
                baseDnProp.setName(bundle.getString("PROP_NAME_BaseDN"));
                baseDnProp.setShortDescription(bundle.getString(
                        "PROP_DESC_BaseDN"));
                connectionDetails.put(baseDnProp);

                Property sslProp = new PropertySupport.Reflection<>(
                        srv, Boolean.class, "secure");
                sslProp.setName(bundle.getString("PROP_NAME_SSL"));
                sslProp.setShortDescription(bundle.getString("PROP_DESC_SSL"));
                securityDetails.put(sslProp);

                Property ignoreTlsErrors = new PropertySupport.Reflection<>(
                        srv, boolean.class, "ignoreTlsErrors");
                ignoreTlsErrors.setName(bundle.getString("PROP_NAME_IgnoreTlsErrors"));
                ignoreTlsErrors.setShortDescription(bundle.getString("PROP_DESC_IgnoreTlsErrors"));
                securityDetails.put(ignoreTlsErrors);

                Property authProp = new PropertySupport.Reflection<>(
                        srv, Authentication.class, "authentication");
                authProp.setName(bundle.getString("PROP_NAME_Authentication"));
                authProp.setShortDescription(bundle.getString(
                        "PROP_DESC_Authentication"));
                securityDetails.put(authProp);

                Property bindProp = new PropertySupport.Reflection<>(srv,
                        String.class, "binding");
                bindProp.setName(bundle.getString("PROP_NAME_Bind"));
                bindProp.setShortDescription(bundle.getString("PROP_DESC_Bind"));
                securityDetails.put(bindProp);

                Property passwordProp = new PropertySupport.Reflection<String>(
                        srv, String.class, "password") {
                    @Override
                    public String getValue() throws IllegalAccessException,
                            IllegalArgumentException, InvocationTargetException {
                        return super.getValue().replaceAll(".", "*");
                    }
                };
                passwordProp.setName(bundle.getString("PROP_NAME_Password"));
                passwordProp.setShortDescription(bundle.getString("PROP_DESC_Password"));
                securityDetails.put(passwordProp);

                Property loginConfProp = new PropertySupport.Reflection<>(srv,
                        Krb5LoginConf.class, "krb5LoginConf");
                loginConfProp.setName(bundle.getString("PROP_NAME_loginConf"));
                loginConfProp.setShortDescription(bundle.getString("PROP_DESC_loginConf"));
                krb5securityDetails.put(loginConfProp);

                Property krb5usernameProp = new PropertySupport.Reflection<>(srv,
                        String.class, "krb5username");
                krb5usernameProp.setName(bundle.getString("PROP_NAME_krb5username"));
                krb5usernameProp.setShortDescription(bundle.getString("PROP_DESC_krb5username"));
                krb5securityDetails.put(krb5usernameProp);

                Property krb5passwordProp = new PropertySupport.Reflection<String>(
                        srv, String.class, "krb5password") {
                    @Override
                    public String getValue() throws IllegalAccessException,
                            IllegalArgumentException, InvocationTargetException {
                        return super.getValue().replaceAll(".", "*");
                    }
                };
                krb5passwordProp.setName(bundle.getString("PROP_NAME_krb5password"));
                krb5passwordProp.setShortDescription(bundle.getString("PROP_DESC_krb5password"));
                krb5securityDetails.put(krb5passwordProp);

                Property krb5keytabProp = new PropertySupport.Reflection<>(srv,
                        File.class, "krb5keytab");
                krb5keytabProp.setName(bundle.getString("PROP_NAME_krb5keytab"));
                krb5keytabProp.setShortDescription(bundle.getString("PROP_DESC_krb5keytab"));
                krb5securityDetails.put(krb5keytabProp);
            } catch (RuntimeException | NoSuchMethodException ex) {
                LOG.log(Level.WARNING, "Failed to create property sheet for server", ex);
            }
        }


        sheet.put(connectionDetails);
        sheet.put(securityDetails);
        sheet.put(krb5securityDetails);

        return sheet;

    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{
            new ExplorerAction(),
            null,
            SystemAction.get(DeleteAction.class),
            null,
            SystemAction.get(PropertiesAction.class),};
        return result;
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LdapServer srv = getLookup().lookup(LdapServer.class);
        if (srv.isLabelSet()) {
            setDisplayName(srv.getLabel());
        } else {
            setDisplayName(srv.toString());
        }
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                LdapService.getDefault().save(srv);
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
            }
        }.execute();
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
