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
package dk.i2m.netbeans.modules.ldapexplorer.ui;

import dk.i2m.netbeans.modules.ldapexplorer.LdapServersNotifier;
import dk.i2m.netbeans.modules.ldapexplorer.LdapService;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import java.util.Collections;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Collection of LDAP servers available.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServersChildren extends Children.Keys<LdapServer> {

    private ChangeListener listener;

    /**
     * Creates a new instance of {@link LdapServersChildren}.
     */
    public LdapServersChildren() {
    }

    @Override
    protected void addNotify() {
        refreshList();
        LdapServersNotifier.addChangeListener(listener = new ChangeListener() {

            public void stateChanged(ChangeEvent ev) {
                refreshList();
            }
        });
    }

    @Override
    protected void removeNotify() {
        if (listener != null) {
            LdapServersNotifier.removeChangeListener(listener);
            listener = null;
        }
        setKeys(Collections.EMPTY_SET);
    }

    /**
     * Refreshes the list of available {@link LdapServer}s.
     */
    private void refreshList() {
        setKeys(LdapService.getDefault().getRegisteredServers());
    }

    @Override
    protected Node[] createNodes(LdapServer key) {
        return new Node[]{new LdapServerNode(key)};
    }
}
