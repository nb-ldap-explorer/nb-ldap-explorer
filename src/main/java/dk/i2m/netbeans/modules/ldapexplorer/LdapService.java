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

import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import java.io.IOException;
import java.util.List;
import org.openide.util.Lookup;

/**
 * Interface for the {@link LdapService}. This service provides a simplified
 * API for communicating with LDAP services.
 *
 * @author Allan Lykke Christensen
 */
public abstract class LdapService {

    /** Connection timeout in MS. */
    public static final String CONNECT_TIMEOUT_IN_MS = "5000";
    /** Identifier for simple authentication. */
    public static final String AUTHENTICATION_SIMPLE = "simple";
    /** Identifier for anonymous connections. */
    public static final String AUTHENTICATION_NONE = "none";
    /** Folder, in the NetBeans virtual file system, containing module settings. */
    public static final String SERVER_FOLDER = "i2m-ldapbrowser/servers";

    /**
     * Gets a {@link List} of registered {@link LdapServer}s.
     *
     * @return {@link List} of registered {@link LdapServer}s
     */
    public abstract List<LdapServer> getRegisteredServers();

    /**
     * Saves a new or existing {@link LdapServer}.
     *
     * @param ldapServer
     *          {@link LdapServer} to save
     * @return Saved {@link LdapServer}
     * @throws IOException
     *          If the {@link LdapServer} could not be saved
     */
    public abstract LdapServer save(LdapServer ldapServer) throws IOException;

    /**
     * Deletes an {@link LdapServer} from the registry.
     *
     * @param server
     *          {@link LdapServer} to delete from the registry
     * @throws IOException
     *          If the {@link LdapServer} could not be deleted
     */
    public abstract void delete(LdapServer server) throws IOException;

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
