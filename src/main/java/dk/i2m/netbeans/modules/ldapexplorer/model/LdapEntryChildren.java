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

import dk.i2m.netbeans.modules.ldapexplorer.ui.LdapServerNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 * LDAP entry {@link Children} for displaying nodes on the UI.
 *
 * @author Allan Lykke Christensen
 */
public class LdapEntryChildren extends Children.Keys<LdapEntry> {

    private String parent = "";
    private LdapServer ldapServer = null;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public LdapServer getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(LdapServer ldapServer) {
        this.ldapServer = ldapServer;
    }

    @Override
    protected void addNotify() {
        if (ldapServer == null) {
            LdapServerNode node = Utilities.actionsGlobalContext().lookup(
                    LdapServerNode.class);
            if (node != null) {
                ldapServer = node.getServer();
            }
        }
        if (ldapServer != null) {
            try {
                setKeys(ldapServer.getTree(parent));
            } catch (QueryException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    protected Node[] createNodes(LdapEntry key) {

        // Prepare for rendering the children of the node
        LdapEntryChildren children = new LdapEntryChildren();
        children.setParent(key.getDn());
        children.setLdapServer(ldapServer);

        AbstractNode node = new AbstractNode(children);
        node.setDisplayName(key.getLabel());
        return new Node[]{node};
    }
}
