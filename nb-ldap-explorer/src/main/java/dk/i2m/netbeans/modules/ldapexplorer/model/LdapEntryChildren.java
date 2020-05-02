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
import java.util.function.Consumer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.*;

/**
 * LDAP entry {@link Children} for displaying nodes on the UI.
 *
 * @author Allan Lykke Christensen
 */
public class LdapEntryChildren extends Children.Keys<LdapEntry> {
    private String parent = "";
    private LdapServer ldapServer = null;
    private TreeLoader tl;
    private SearchContext ctx;
    private Consumer<SearchContext> cancelHandler = (sc) -> this.cancelLoading();

    public SearchContext getCtx() {
        return ctx;
    }

    public void setCtx(SearchContext ctx) {
        this.ctx = ctx;
    }

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
        if (ldapServer != null) {
            tl = new TreeLoader();
            if(ctx != null) {
                ctx.registerCancelListener(cancelHandler);
            }
            final ProgressHandle ph = ProgressHandleFactory.createHandle(
                    NbBundle.getMessage(LdapEntryChildren.class, "FetchingLDAPEntries"), tl);
            RequestProcessor.Task t = UIHelper.getRequestProcessor().create(() -> {
                ph.start();
                ph.switchToIndeterminate();
                try {
                    ldapServer.getTree(parent, tl);
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
        cancelLoading();
    }

    private void cancelLoading() {
        TreeLoader wtl = tl;
        tl = null;
        if(wtl != null) {
            tl.cancel();
        }
        if(ctx != null) {
            ctx.removeCancelListener(cancelHandler);
        }
    }

    @Override
    protected Node[] createNodes(LdapEntry key) {
        // Prepare for rendering the children of the node
        LdapEntryChildren children = new LdapEntryChildren();
        children.setParent(key.getDn());
        children.setLdapServer(ldapServer);
        children.setCtx(ctx);

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
