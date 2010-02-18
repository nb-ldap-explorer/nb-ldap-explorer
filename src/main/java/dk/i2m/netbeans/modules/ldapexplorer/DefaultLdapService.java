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

import dk.i2m.netbeans.modules.ldapexplorer.model.Authentication;
import dk.i2m.netbeans.modules.ldapexplorer.model.LdapServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * Default implementation of the {@link LdapService}.
 *
 * @author Allan Lykke Christensen
 */
public class DefaultLdapService extends LdapService {

    /** {@inheritDoc} */
    @Override
    public List<LdapServer> getRegisteredServers() {
        List<LdapServer> servers = new ArrayList<LdapServer>();

        FileObject cfg = FileUtil.getConfigRoot();
        FileObject fobs = cfg.getFileObject(LdapService.SERVER_FOLDER);

        for (FileObject fob : fobs.getChildren()) {
            if (fob.isData()) {
                servers.add(generateLdapServer(fob));
            }
        }

        return servers;
    }

    /** {@inheritDoc } */
    public LdapServer save(LdapServer ldapServer) throws IOException {
        FileObject servers = FileUtil.getConfigRoot().
                getFileObject(LdapService.SERVER_FOLDER);
        FileObject server;
        if (ldapServer.isNew()) {
            String filename = "i2m-ldapserver-" + Calendar.getInstance().
                    getTimeInMillis();
            server = servers.createData(filename);
            ldapServer.setIdentifier(filename);
        } else {
            server = servers.getFileObject(ldapServer.getIdentifier());
        }

        server.setAttribute("host", ldapServer.getHost());
        server.setAttribute("port", ldapServer.getPort());
        server.setAttribute("base-dn", ldapServer.getBaseDN());
        server.setAttribute("authentication", ldapServer.getAuthentication().
                name());
        server.setAttribute("bind", ldapServer.getBinding());
        server.setAttribute("password", ldapServer.getPassword());
        server.setAttribute("ssl", ldapServer.isSecure());

        return ldapServer;

    }

    /** {@inheritDoc} */
    @Override
    public void delete(LdapServer server) throws IOException {
        FileObject fo = FileUtil.getConfigRoot().getFileObject(
                LdapService.SERVER_FOLDER);
        for (FileObject ldapServer : fo.getChildren()) {
            if (ldapServer.isData() && ldapServer.getName().equalsIgnoreCase(
                    server.getIdentifier())) {
                ldapServer.delete();
            }
        }

    }

    /**
     * Generates an {@link LdapServer} from a {@link FileObject}.
     *
     * @param fo
     *          {@link FileObject} to use for generating the {@link LdapServer}
     * @return {@link LdapServer} based on the given {@link FileObject}
     */
    private LdapServer generateLdapServer(FileObject fo) {
        String host = (String) fo.getAttribute("host");
        int port = (Integer) fo.getAttribute("port");
        String baseDn = (String) fo.getAttribute("base-dn");
        String authentication = (String) fo.getAttribute("authentication");
        String bind = (String) fo.getAttribute("bind");
        String password = (String) fo.getAttribute("password");
        Boolean secure = (Boolean) fo.getAttribute("ssl");

        LdapServer server = new LdapServer(host, port, baseDn);
        server.setIdentifier(fo.getName());
        server.setAuthentication(Authentication.valueOf(authentication));
        server.setBinding(bind);
        server.setPassword(password);
        server.setSecure(secure);
        return server;
    }
}
