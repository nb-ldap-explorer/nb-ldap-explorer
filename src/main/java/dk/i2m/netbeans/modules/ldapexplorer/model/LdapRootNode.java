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

import java.awt.Image;
import java.util.ResourceBundle;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 * {@link Node} representing the root of an LDAP entry tree.
 *
 * @author Allan Lykke Christensen
 */
public class LdapRootNode extends AbstractNode {

    private static ResourceBundle msgs = NbBundle.getBundle(LdapRootNode.class);

    /**
     * Creates a new instance of {@link LdapRootNode}.
     *
     * @param children
     *          Children to display under this node
     * @param baseDn
     *          Base DN - used as the {@link LdapRootNode#getDisplayName()}
     *          of the node
     */
    public LdapRootNode(Children children, String baseDn) {
        super(children);
        setDisplayName(baseDn);
    }

    /**
     * Shows a special icon (globe) for the LDAP root node.
     *
     * @param type
     *          Type of icon to display
     * @return {@link Image} containin the icon
     */
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(msgs.getString("ICON_LdapRootNode"));
    }

    /**
     * Shows the same icon as specified in {@link LdapRootNode#getIcon(int)}.
     *
     * @param type
     *          Type of icon to display
     * @return {@link Image} containing the icon
     */
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
}
