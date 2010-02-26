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

import java.awt.Image;
import java.util.ResourceBundle;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;

/**
 * Node representing an {@link LdapEntry}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapEntryNode extends AbstractNode {

    private boolean root = false;
    private static ResourceBundle bundle = NbBundle.getBundle(
            LdapEntryNode.class);

    public LdapEntryNode(Children c, LdapEntry ldapEntry) {
        super(c, Lookups.singleton(ldapEntry));

        setDisplayName(ldapEntry.toString());
        setShortDescription(bundle.getString("HINT_LdapEntryNode"));
        root = false;
    }

    public LdapEntryNode(Children c) {
        super(c);
        setDisplayName("");
        root = true;
    }

    @Override
    public Image getIcon(int type) {

        if (root) {
            return ImageUtilities.loadImage(
                    bundle.getString("ICON_LdapRootNode"));
        } else {

            LdapEntry entry = getLookup().lookup(LdapEntry.class);
            Image img;

            switch (entry.getEntryType()) {

                case PERSON:
                    img = ImageUtilities.loadImage(bundle.getString(
                            "ICON_LdapEntryPersonNode"));
                    break;

                case ORGANISATION:
                    img = ImageUtilities.loadImage(bundle.getString(
                            "ICON_LdapEntryOrganizationNode"));
                    break;

                case GROUP:
                    img = ImageUtilities.loadImage(bundle.getString(
                            "ICON_LdapEntryGroupNode"));
                    break;


                default:
                    img = ImageUtilities.loadImage(bundle.getString(
                            "ICON_LdapEntryNode"));

            }
            return img;
        }
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    /**
     * No right click actions available for {@link LdapEntryNode}s.
     *
     * @param context
     * @return
     */
    @Override
    public Action[] getActions(boolean context) {
        Action[] result = new Action[]{};
        return result;
    }
}
