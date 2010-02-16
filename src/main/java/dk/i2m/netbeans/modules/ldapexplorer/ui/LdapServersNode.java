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

import dk.i2m.netbeans.modules.ldapexplorer.NewLdapServer;
import java.util.ResourceBundle;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 * Node representing the root node containing all the LDAP servers.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServersNode extends AbstractNode {

    private static ResourceBundle bundle = NbBundle.getBundle(
            LdapServersNode.class);

    /**
     * Creates a new instance of {@link LdapServersNode}.
     */
    public LdapServersNode() {
        super(new LdapServersChildren());
        setIconBaseWithExtension(bundle.getString("ICON_LdapServersNode"));
        setName("LdapServersNode");
        setDisplayName(bundle.getString("LBL_LdapServersNode"));
        setShortDescription(bundle.getString("HINT_LdapServersNode"));
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{
            SystemAction.get(NewLdapServer.class)};
        return result;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

//            Property enabledProp = new PropertySupport.Reflection(app,
//                    Boolean.class, "isSaveEnabled", "setSaveEnabled");
//            Property saveFolderProp = new PropertySupport.Reflection(app,
//                    String.class, "getSaveFolder", "setSaveFolder");
//
//            enabledProp.setName(bundle.getString("LBL_SaveEnabledProperty"));
//            saveFolderProp.setName(bundle.getString("LBL_SaveFolderProperty"));
//            set.put(enabledProp);
//            set.put(saveFolderProp);
        sheet.put(set);

        return sheet;
    }
}
