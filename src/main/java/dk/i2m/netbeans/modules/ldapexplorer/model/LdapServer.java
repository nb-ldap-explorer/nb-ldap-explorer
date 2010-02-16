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

/**
 * Connection to an {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServer {

    private String identifier = null;
    private String host;
    private int port;
    private String baseDN;
    private Authentication authentication = Authentication.NONE;
    private String binding = "";
    private String password = "";

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
        this.host = host;
        this.port = port;
        this.baseDN = baseDN;
    }

    /**
     * Gets the unique identifier of the {@link LdapServer}. The identifier
     * is the same as the name of the {@link FileObject} storing the
     * {@link LdapServer}.
     *
     * @return Unique identifier of the {@link LdapServer}
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the unique identifier of the {@link LdapServer}. The identifier
     * should not be changed after the {@link LdapServer} is stored as it the
     * same as the name of the {@link FileObject} storing the
     * {@link LdapServer}.
     *
     * @param identifier
     *          Unique identifier of the {@link LdapServer}.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Determines if the {@link LdapServer} is new. The {@link LdapServer} is
     * new if the {@link LdapServer#identifier} has not been set.
     *
     * @return <code>true</code> if the {@link LdapServer} is new, otherwise
     *         <code>false</code>
     */
    public boolean isNew() {
        if (this.identifier == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LdapServer other = (LdapServer) obj;
        if ((this.identifier == null) ? (other.identifier != null)
                : !this.identifier.equals(other.identifier)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash =
                41 * hash + (this.identifier != null
                ? this.identifier.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ldap://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/");
        sb.append(baseDN);
        return sb.toString();
    }
}
