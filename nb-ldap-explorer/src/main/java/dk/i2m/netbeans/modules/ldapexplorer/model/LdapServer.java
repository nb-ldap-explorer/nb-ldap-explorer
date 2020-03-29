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

import java.io.File;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
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
    private static final Logger LOG = Logger.getLogger(LdapServer.class.getName());

    private String krb5username = "";
    private String krb5password = "";
    private File krb5keytab = null;
    private Krb5LoginConf krb5loginConf = Krb5LoginConf.SYSTEM_ACCOUNT;
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

    @Override
    @SuppressWarnings("UseOfObsoleteCollectionType")
    protected Hashtable<String, String> getConnectionEnvironment() {
        Hashtable<String, String> env = super.getConnectionEnvironment();
        env.put(Context.REFERRAL, "follow");
        if(this.getAuthentication() == Authentication.KERBEROS5) {
            env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
        }
        LOG.info(env.toString());
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
                            @Override
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
                            @Override
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
                if (ex.getCause() instanceof QueryException) {
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
                            @Override
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
                if (ex.getCause() instanceof QueryException) {
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
                            @Override
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
                if (ex.getCause() instanceof QueryException) {
                    throw (QueryException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        } else {
            return LdapServer.super.getEntry( dn );
        }
    }

    public Krb5LoginConf getKrb5LoginConf() {
        return krb5loginConf;
    }

    public void setKrb5LoginConf(Krb5LoginConf loginConf) {
        if(loginConf == null) {
            throw new IllegalArgumentException("The Krb5Login Configuration must not be NULL");
        }
        this.identity = null;
        Krb5LoginConf old = this.krb5loginConf;
        this.krb5loginConf = loginConf;
        fire("krb5LoginConf", old, this.krb5loginConf);
    }

    private Subject getIdentity() throws LoginException {
        // Benutze zunächst bestehenden Login Context
        if(this.identity != null) {
            LOG.log(Level.INFO, "Service existing identity: {0}", this.identity.toString());
            return identity;
        }
        if ( ! this.getAuthentication().equals( Authentication.KERBEROS5 ) ) {
            throw new IllegalStateException(
                    "LoginContext only allowed + used for Kerberos5!");
        }

        LoginContext lc = new LoginContext("generated", null, (Callback[] callbacks) -> {
            String username = getKrb5username().trim();
            String password = getKrb5password().trim();
            LOG.info("Password/Username Callbackhandler called");
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(username);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password.toCharArray());
                }
            }
        }, krb5loginConf.getLoginConfiguration(getKrb5username(), getKrb5keytab()));
        LOG.info("Logging in");
        lc.login();
        LOG.info("Logged in");
        LOG.log(Level.INFO, "Subject: %s", new Object[]{lc.getSubject().toString()});
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

    public File getKrb5keytab() {
        return krb5keytab;
    }

    public void setKrb5keytab(File krb5keytab) {
        this.krb5keytab = krb5keytab;
    }
}
