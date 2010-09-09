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

import dk.i2m.netbeans.modules.ldapexplorer.ui.PasswordDialog;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * Connection to an {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServer extends BaseLdapServer {
    private static Logger l = Logger.getLogger(LdapServer.class.getName());

    private String krb5username = "";
    private String krb5password = "";
    private String[] loginConf = new String[0];
    private Subject identity = null;

    /**
     * Creates a new {@link LdapServer}.
     */
    public LdapServer() {
        this("", 389, "");
    }

    /**
     * Creates a new {@link LdapServer}.
     *
     * @param host
     *          Hostname or IP of the {@link LdapServer}
     * @param port
     *          Port of the {@link LdapServer}
     * @param baseDN
     *          Base distinguished name
     */
    public LdapServer(String host, int port, String baseDN) {
        super(host, port, baseDN);
    }

    protected Hashtable<String, String> getConnectionEnvironment() {
        Hashtable<String, String> env = super.getConnectionEnvironment();
        env.put(Context.REFERRAL, "follow");
        if(this.getAuthentication() == Authentication.KERBEROS5) {
            env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
        }
        return env;
    }

    /**
     * Equality of an {@link LdapServer} is based on
     * {@link LdapServer#getIdentifier()}. Note that {@link LdapServer}s
     * without an identifier will all be equal.
     *
     * @param obj
     *          {@link LdapServer} to compare
     * @return <code>true</code> if the {@link LdapServer} is equal, otherwise
     *         <code>false</code>
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LdapServer other = (LdapServer) obj;
        if ((this.getIdentifier() == null) ? (other.getIdentifier() != null)
                : !this.getIdentifier().equals(other.getIdentifier())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash =
                41 * hash + (this.getIdentifier() != null
                ? this.getIdentifier().hashCode() : 0);
        return hash;
    }

    @Override
    public void connect() throws ConnectionException {
        if (this.getAuthentication() == Authentication.KERBEROS5) {
            try {
                javax.security.auth.Subject.doAs(
                        getIdentity(),
                        new PrivilegedAction<Void>() {

                            public Void run() {
                                try {
                                    LdapServer.super.connect();
                                } catch (ConnectionException ex) {
                                    throw new RuntimeException(ex);
                                }
                                return null;
                            }
                        });
            } catch (LoginException ex) {
                throw new ConnectionException(ex);
            } catch (RuntimeException ex) {
                if (ex.getCause() instanceof ConnectionException) {
                    throw (ConnectionException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        } else {
           super.connect();
        }
    }

    @Override
    public List<LdapEntry> getTree(final String path)  throws QueryException {
        if (this.getAuthentication() == Authentication.KERBEROS5) {
            try {
                return javax.security.auth.Subject.doAs(
                        getIdentity(),
                        new PrivilegedAction<List<LdapEntry>>() {

                            public List<LdapEntry> run() {
                                try {
                                    return LdapServer.super.getTree(path);
                                } catch (QueryException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
            } catch (LoginException ex) {
                throw new QueryException(ex);
            } catch (RuntimeException ex) {
                if (ex.getCause() instanceof ConnectionException) {
                    throw (QueryException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        } else {
            return LdapServer.super.getTree(path);
        }
    }

    @Override
    public List<LdapEntry> search(final String filter)  throws QueryException {
        if (this.getAuthentication() == Authentication.KERBEROS5) {
            try {
                return javax.security.auth.Subject.doAs(
                        getIdentity(),
                        new PrivilegedAction<List<LdapEntry>>() {

                            public List<LdapEntry> run() {
                                try {
                                    return LdapServer.super.search(filter);
                                } catch (QueryException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
            } catch (LoginException ex) {
                throw new QueryException(ex);
            } catch (RuntimeException ex) {
                if (ex.getCause() instanceof ConnectionException) {
                    throw (QueryException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        } else {
            return LdapServer.super.search(filter);
        }
    }

    @Override
    public LdapEntry getEntry(final String dn) throws QueryException {
        if (this.getAuthentication() == Authentication.KERBEROS5) {
            try {
                return javax.security.auth.Subject.doAs(
                        getIdentity(),
                        new PrivilegedAction<LdapEntry>() {
                            public LdapEntry run() {
                                try {
                                    return LdapServer.super.getEntry( dn );
                                } catch (QueryException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
            } catch (LoginException ex) {
                throw new QueryException(ex);
            } catch (RuntimeException ex) {
                if (ex.getCause() instanceof ConnectionException) {
                    throw (QueryException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        } else {
            return LdapServer.super.getEntry( dn );
        }
    }

    public String[] getLoginConf() {
        return loginConf;
    }

    public void setLoginConf(String[] loginConf) {
        String[] old = this.loginConf;
        if( loginConf == null ) {
            this.loginConf = new String[0];
        } else {
            this.loginConf = loginConf;
        }
        this.identity = null;
        fire("loginConf", old, this.loginConf);
    }

    private Subject getIdentity() throws LoginException {
        // Benutze zunächst bestehenden Login Context
        if(this.identity != null) {
            l.info("Service existing identity: " + this.identity.toString());
            return identity;
        }
        if ( ! this.getAuthentication().equals( Authentication.KERBEROS5 ) ) {
            throw new IllegalStateException(
                    "LoginContext only allowed + used for Kerberos5!");
        }
        StringBuilder sb = new StringBuilder();
        final java.util.HashMap<String, String> options = new java.util.HashMap<String, String>();
        for(String line: this.getLoginConf()) {
            sb.append(sb);
            if(line.trim().length() == 0) continue;
            String[] parts = line.split("=", 2);
            if(parts.length > 1) {
                options.put(parts[0], parts[1]);
            } else {
                options.put(parts[0], "");
            }
        }
        l.info("LoginConf: " + sb.toString());
        final javax.security.auth.login.Configuration c =
                new javax.security.auth.login.Configuration() {
            @Override
            public javax.security.auth.login.AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new javax.security.auth.login.AppConfigurationEntry[]{
                            new javax.security.auth.login.AppConfigurationEntry(
                            "com.sun.security.auth.module.Krb5LoginModule",
                            javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                            options)
                        };
            }
        };
        LoginContext lc = new LoginContext("generated", null, new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException,
                    UnsupportedCallbackException {
                String username = getKrb5username().trim();
                String password = getKrb5password().trim();
                l.info("Password/Username Callbackhandler called");
                if ( username.length() == 0 || password.length() == 0 ) {
                    PasswordDialog pd = new PasswordDialog(null);
                    pd.setUsername(username);
                    pd.setPassword(password);
                    pd.setVisible(true);
                    // The dialog is modal ...
                    if(! pd.isCanceled()) {
                        username = pd.getUsername();
                        password = pd.getPassword();
                    }
                }
                for (Callback callback : callbacks) {
                    if (callback instanceof NameCallback) {
                        ((NameCallback) callback).setName(username);
                    } else if (callback instanceof PasswordCallback) {
                        ((PasswordCallback) callback).setPassword(password.toCharArray());
                    }
                }
            }
        }, c);
        l.info("Logging in");
        lc.login();
        l.info("Logged in");
        l.info("Subject: " + lc.getSubject().toString());
        this.identity = lc.getSubject();
        return lc.getSubject();
    }

    public String getKrb5password() {
        return krb5password;
    }

    public void setKrb5password(String krb5password) {
        String old = this.krb5password;
        this.krb5password = krb5password;
        this.identity = null;
        fire(krb5username, old, this.krb5password);
    }

    public String getKrb5username() {
        return krb5username;
    }

    public void setKrb5username(String krb5username) {
        String old = this.krb5username;
        this.krb5username = krb5username;
        this.identity = null;
        fire(krb5username, old, this.krb5username);
    }
}
