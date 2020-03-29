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

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * LDAP entry {@link Children} for displaying search result nodes on the UI.
 *
 * @author Allan Lykke Christensen
 */
public class LdapSearchEntryChildren extends Children.Keys<LdapEntry> {

    private List<LdapEntry> entries = new ArrayList<>();
    private LdapServer ldapServer = null;

    public LdapSearchEntryChildren(List<LdapEntry> entries) {
        this.entries = entries;
    }

    public void setEntries(List<LdapEntry> entries) {
        this.entries = entries;
    }
    
    public LdapServer getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(LdapServer ldapServer) {
        this.ldapServer = ldapServer;
    }

    @Override
    protected void addNotify() {
        setKeys(entries);
    }

    @Override
    protected Node[] createNodes(LdapEntry key) {
        LdapEntryChildren children = new LdapEntryChildren();
        children.setParent(key.getDn());
        children.setLdapServer(ldapServer);
        LdapEntryNode node = new LdapEntryNode(children, key);
        node.setDisplayName(key.getLabel());
        return new Node[]{node};
    }
}
