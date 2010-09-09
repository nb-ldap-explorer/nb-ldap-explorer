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
package dk.i2m.netbeans.modules.ldapexplorer.services;

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

    /** FileObject attribute containing the hostname of the LdapServer. */
    public static final String FO_ATTR_HOST = "host";
    /** FileObject attribute containing the port number of the LdapServer. */
    public static final String FO_ATTR_PORT = "port";
    /** FileObject attribute containing the base DN of the LdapServer. */
    public static final String FO_ATTR_BASEDN = "base-dn";
    /** FileObject attribute containing the authentication of the LdapServer. */
    public static final String FO_ATTR_AUTHENTICATION = "authentication";
    /** FileObject attribute containing the bind of the LdapServer. */
    public static final String FO_ATTR_BIND = "bind";
    /** FileObject attribute containing the password of the LdapServer. */
    public static final String FO_ATTR_PASSWORD = "password";
    /** FileObject attribute containing the SSL setting of the LdapServer. */
    public static final String FO_ATTR_SSL = "ssl";
    /** FileObject attribute containing the label of the LdapServer. */
    public static final String FO_ATTR_LABEL = "label";
    /** FileObject attribute containing the connection timeout of the LdapServer. */
    public static final String FO_ATTR_TIMEOUT = "timeout";
    /** FileObject attribute containing the loginConfiguration for the LdapServer */
    public static final String FO_ATTR_LOGINCONF = "loginconf";
    /** FileObject attribute containing the loginConfiguration for the LdapServer */
    public static final String FO_ATTR_KRB5USERNAME = "krb5username";
    /** FileObject attribute containing the loginConfiguration for the LdapServer */
    public static final String FO_ATTR_KRB5PASSWORD = "krb5password";
    /** Prefix of FileObjects containing LdapServers. */
    public static final String FO_PREFIX = "i2m-ldapserver-";

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
            String filename = FO_PREFIX + Calendar.getInstance().
                    getTimeInMillis();
            server = servers.createData(filename);
            ldapServer.setIdentifier(filename);
        } else {
            server = servers.getFileObject(ldapServer.getIdentifier());
        }

        server.setAttribute(FO_ATTR_LABEL, ldapServer.getLabel());
        server.setAttribute(FO_ATTR_HOST, ldapServer.getHost());
        server.setAttribute(FO_ATTR_PORT, ldapServer.getPort());
        server.setAttribute(FO_ATTR_TIMEOUT, ldapServer.getTimeout());
        server.setAttribute(FO_ATTR_BASEDN, ldapServer.getBaseDN());
        server.setAttribute(FO_ATTR_AUTHENTICATION, ldapServer.getAuthentication().
                name());
        server.setAttribute(FO_ATTR_BIND, ldapServer.getBinding());
        server.setAttribute(FO_ATTR_PASSWORD, ldapServer.getPassword());
        server.setAttribute(FO_ATTR_SSL, ldapServer.isSecure());
        server.setAttribute(FO_ATTR_LOGINCONF, ldapServer.getLoginConf());
        server.setAttribute(FO_ATTR_KRB5USERNAME, ldapServer.getKrb5username());
        server.setAttribute(FO_ATTR_KRB5PASSWORD, ldapServer.getKrb5password());
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
        String label = getAttributeAsString(fo, FO_ATTR_LABEL, "");
        String host = getAttributeAsString(fo, FO_ATTR_HOST, "");
        int port = getAttributeAsInteger(fo, FO_ATTR_PORT, 0);
        int timeout = getAttributeAsInteger(fo, FO_ATTR_TIMEOUT, 5000);
        String baseDn = getAttributeAsString(fo, FO_ATTR_BASEDN, "");
        String authentication = getAttributeAsString(fo, FO_ATTR_AUTHENTICATION, Authentication.NONE.
                name());
        String bind = getAttributeAsString(fo, FO_ATTR_BIND, "");
        String password = getAttributeAsString(fo, FO_ATTR_PASSWORD, "");
        Boolean secure = getAttributeAsBoolean(fo, FO_ATTR_SSL, false);
        String[] loginConfig = getAttributeAsStringArray(fo, FO_ATTR_LOGINCONF, new String[0]);
        String krb5username = getAttributeAsString(fo, FO_ATTR_KRB5USERNAME, "");
        String krb5password = getAttributeAsString(fo, FO_ATTR_KRB5PASSWORD, "");

        LdapServer server = new LdapServer(host, port, baseDn);
        server.setLabel(label);
        server.setTimeout(timeout);
        server.setIdentifier(fo.getName());
        server.setAuthentication(Authentication.valueOf(authentication));
        server.setBinding(bind);
        server.setPassword(password);
        server.setSecure(secure);
        server.setLoginConf(loginConfig);
        server.setKrb5username(krb5username);
        server.setKrb5password(krb5password);
        return server;
    }

    private String[] getAttributeAsStringArray(FileObject fo, String key,
            String[] defaultValue) {
        Object obj = fo.getAttribute(key);
        if (obj == null) {
            return defaultValue;
        } else {
            return (String[]) obj;
        }
    }

    private String getAttributeAsString(FileObject fo, String key,
            String defaultValue) {
        Object obj = fo.getAttribute(key);
        if (obj == null) {
            return defaultValue;
        } else {
            return (String) obj;
        }
    }

    private Integer getAttributeAsInteger(FileObject fo, String key,
            Integer defaultValue) {
        Object obj = fo.getAttribute(key);
        if (obj == null) {
            return defaultValue;
        } else {
            return (Integer) obj;
        }
    }

    private Boolean getAttributeAsBoolean(FileObject fo, String key,
            Boolean defaultValue) {
        Object obj = fo.getAttribute(key);
        if (obj == null) {
            return defaultValue;
        } else {
            return (Boolean) obj;
        }
    }
}
