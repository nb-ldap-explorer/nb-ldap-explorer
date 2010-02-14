/*
 *  Copyright 2010 Interactive Media Management.
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
package dk.i2m.netbeans.modules.ldapexplorer;

import org.openide.util.Lookup;

/**
 * Interface for the {@link LdapService}. This service provides a simplified
 * API for communicating with LDAP services.
 *
 * @author Allan Lykke Christensen
 */
public abstract class LdapService {

    /**
     * Obtains the attributes and values of an object in a given directory.
     *
     * @param ldapUrl
     *          URL of the directory service
     * @param dn
     *          Distinguished name of the object to fetch
     * @return {@link String} containing LDIF of the specified object
     * @throws NotFoundException
     *          If an object could not be found with the given <code>dn</code>
     */
    public abstract String getAttributes(final String ldapUrl, final String dn) throws
            NotFoundException;

    /**
     * Gets the children nodes of an object in a given directory.
     *
     * @param ldapUrl
     *          URL of the directory service
     * @param dn
     *          distinguished name of the childrens parent
     * @return Array of {@link String}s containing the distinguished names of
     *         the children
     * @throws NotFoundException
     *          If an object could not be found with the given <code>dn</code>
     */
    public abstract String[] getChildren(final String ldapUrl, final String dn) throws
            NotFoundException;

    /**
     * Obtains default implementation of the {@link LdapService}.
     *
     * @return Default implementation of the {@link LdapService}
     */
    public static LdapService getDefault() {
        LdapService service = Lookup.getDefault().lookup(LdapService.class);
        if (service == null) {
            service = new DefaultLdapService();
        }
        return (service);
    }
}
