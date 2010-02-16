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
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
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
    public Map<String, String> getAttributeMap(String ldapUrl, String username,
            String password, String dn) throws NotFoundException {
        Map<String, String> attributeMap = new LinkedHashMap<String, String>();

        StringBuffer sb = new StringBuffer();
        Hashtable<String, String> env;

        if (username != null && !username.isEmpty()) {
            env = getEnvironment(ldapUrl, AUTHENTICATION_SIMPLE,
                    username, password);
        } else {
            env = getEnvironment(ldapUrl);
        }

        List<String> children = new ArrayList<String>();
        DirContext ctx = null;
        NotFoundException exception = null;
        try {
            ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(dn.toString());

            for (NamingEnumeration<? extends Attribute> ae = attrs.getAll(); ae.
                    hasMore();) {
                Attribute attr = ae.next();

                for (NamingEnumeration ne = attr.getAll(); ne.hasMore();) {
                    attributeMap.put(attr.getID(), ne.next().toString());
                }
            }
        } catch (Exception ex) {
            exception = new NotFoundException(ex);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        if (exception != null) {
            throw exception;
        }

        return attributeMap;
    }

    /** {@inheritDoc} */
    @Override
    public String getAttributes(String ldapUrl, String username, String password,
            String dn) throws NotFoundException {
        StringBuffer sb = new StringBuffer();
        Hashtable<String, String> env;

        if (username != null && !username.isEmpty()) {
            env = getEnvironment(ldapUrl, AUTHENTICATION_SIMPLE,
                    username, password);
        } else {
            env = getEnvironment(ldapUrl);
        }

        List<String> children = new ArrayList<String>();
        DirContext ctx = null;
        NotFoundException exception = null;
        try {
            ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(dn.toString());

            for (NamingEnumeration<? extends Attribute> ae = attrs.getAll(); ae.
                    hasMore();) {
                Attribute attr = ae.next();

                /* Print each value */
                for (NamingEnumeration ne = attr.getAll(); ne.hasMore();) {
                    sb.append(attr.getID());
                    sb.append(": ");
                    sb.append(ne.next());
                    sb.append(System.getProperty("line.separator"));
                }
            }
        } catch (Exception ex) {
            exception = new NotFoundException(ex);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        if (exception != null) {
            throw exception;
        }

        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String getAttributes(String ldapUrl, String dn) throws
            NotFoundException {
        return getAttributes(ldapUrl, "", "", dn);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getChildren(String ldapUrl, String username, String password,
            String dn) throws NotFoundException {
        Hashtable<String, String> env;

        if (username != null && !username.isEmpty()) {
            env = getEnvironment(ldapUrl, AUTHENTICATION_SIMPLE,
                    username, password);
        } else {
            env = getEnvironment(ldapUrl);
        }

        List<String> children = new ArrayList<String>();
        DirContext ctx = null;
        NotFoundException exception = null;
        try {
            ctx = new InitialDirContext(env);
            NamingEnumeration<NameClassPair> list = ctx.list(dn);
            while (list.hasMore()) {
                NameClassPair nc = list.next();
                children.add(nc.getName());
            }
        } catch (Exception ex) {
            exception = new NotFoundException(ex);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        if (exception != null) {
            throw exception;
        }

        return children.toArray(new String[]{});
    }

    /** {@inheritDoc} */
    @Override
    public String[] getChildren(String ldapUrl, String dn) throws
            NotFoundException {
        return getChildren(ldapUrl, "", "", dn);
    }

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

        return ldapServer;

    }

    /**
     * Generates a {@link Hashtable} with a configured LDAP environment.
     *
     * @param ldapUrl
     *          URL of the LDAP directory service
     * @param authentication
     *          Type of authentication to use. Only <code>simple</code> and
     *          <code>none</code> are supported
     * @param username
     *          If <code>simple</code> authentication is used, a username must
     *          be provided
     * @param password
     *          If <code>simple</code> authentication is used, a password must
     *          be provided
     * @return {@link Hashtable} containing a configured LDAP environment
     */
    private Hashtable<String, String> getEnvironment(String ldapUrl,
            String authentication, String username, String password) {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put("com.sun.jndi.ldap.connect.timeout", CONNECT_TIMEOUT_IN_MS);

        if (authentication.equalsIgnoreCase(AUTHENTICATION_SIMPLE)) {
            env.put(Context.SECURITY_AUTHENTICATION, authentication);
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
        }
        return env;
    }

    /**
     * Generates a {@link Hashtable} with a configured LDAP environment.
     *
     * @param ldapUrl
     *          URL of the LDAP directory service
     * @return {@link Hashtable} containing a configured LDAP environment
     */
    private Hashtable<String, String> getEnvironment(String ldapUrl) {
        return getEnvironment(ldapUrl, "none", "", "");
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

        LdapServer server = new LdapServer(host, port, baseDn);
        server.setAuthentication(Authentication.valueOf(authentication));
        server.setBinding(bind);
        server.setPassword(password);
        server.setIdentifier(fo.getName());
        return server;
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
}
