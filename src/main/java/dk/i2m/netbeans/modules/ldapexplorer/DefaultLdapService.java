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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Default implementation of the {@link LdapService}.
 *
 * @author Allan Lykke Christensen
 */
public class DefaultLdapService extends LdapService {

    private static final String CONNECT_TIMEOUT_IN_MS = "5000";

    /** {@inheritDoc} */
    public String getAttributes(String ldapUrl, String dn) throws
            NotFoundException {
        StringBuffer sb = new StringBuffer();
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put("com.sun.jndi.ldap.connect.timeout", CONNECT_TIMEOUT_IN_MS);

        List<String> children = new ArrayList<String>();
        DirContext ctx = null;
        NotFoundException exception = null;
        try {
            ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(dn.toString());

            for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                Attribute attr = (Attribute) ae.next();

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
    public String[] getChildren(String ldapUrl, String dn) throws
            NotFoundException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put("com.sun.jndi.ldap.connect.timeout", CONNECT_TIMEOUT_IN_MS);

        List<String> children = new ArrayList<String>();
        DirContext ctx = null;
        NotFoundException exception = null;
        try {
            ctx = new InitialDirContext(env);
            NamingEnumeration list = ctx.list(dn);
            while (list.hasMore()) {
                NameClassPair nc = (NameClassPair) list.next();
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
}
