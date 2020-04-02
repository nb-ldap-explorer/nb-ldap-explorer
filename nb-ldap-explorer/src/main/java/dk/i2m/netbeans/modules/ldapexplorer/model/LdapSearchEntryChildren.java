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

import dk.i2m.netbeans.modules.ldapexplorer.ui.UIHelper;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;

/**
 * LDAP entry {@link Children} for displaying search result nodes on the UI.
 *
 * @author Allan Lykke Christensen
 */
public class LdapSearchEntryChildren extends Children.Keys<LdapEntry> {

    private LdapServer ldapServer = null;
    private String search;
    private TreeLoader tl;

    public LdapServer getLdapServer() {
        return ldapServer;
    }

    public void setLdapServer(LdapServer ldapServer) {
        this.ldapServer = ldapServer;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    protected void addNotify() {
       if (ldapServer != null) {
            tl = new TreeLoader();
            final ProgressHandle ph = ProgressHandleFactory.createHandle(
                    NbBundle.getMessage(LdapEntryChildren.class, "FetchingLDAPEntries"), tl);
            RequestProcessor.Task t = UIHelper.getRequestProcessor().create(() -> {
                ph.start();
                ph.switchToIndeterminate();
                try {
                    ldapServer.search(search, tl);
                } catch (QueryException ex) {
                    Exceptions.printStackTrace(ex);
                }

                tl = null;
            });

            t.addTaskListener( (Task task) -> ph.finish());

            t.schedule(0);
        }
    }

    @Override
    protected void removeNotify() {
        if(tl != null) {
            tl.cancel();
            tl = null;
        }
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

    class TreeLoader extends LdapResultProcessor {
        private final ArrayList<LdapEntry> entries = new ArrayList<>();

        @Override
        public void addEntry(LdapEntry entry) {
            synchronized (entries) {
                entries.add(entry);
                setKeys(entries);
            }
        }

        @Override
        public void reset() {
            synchronized (entries) {
                entries.clear();
                setKeys(entries);
            }
        }

    }
}
