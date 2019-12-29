/*
 *  Copyright 2012 Matthias Bläsing.
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

package com.google.code.nb_ldap_explorer.ssl_certificate_exception;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Exceptions;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class HostnameCertificateStore {

    private KeyStore keystore;
    @XmlElement
    @XmlJavaTypeAdapter(HostnameEntryAdapter.class)
    private Map<String, ArrayList<String>> knownHosts =
            new HashMap<String, ArrayList<String>>();

    public HostnameCertificateStore() {
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    public void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }

    public void setTrusted(String hostname, Certificate cert) {
        try {
            String alias = keystore.getCertificateAlias(cert);
            if (alias == null) {
                alias = addCertToTrustStore(keystore, (X509Certificate) cert);
            }
            ArrayList<String> knownAliases = knownHosts.get(hostname);
            if (knownAliases == null) {
                knownAliases = new ArrayList<String>();
            }
            if (!knownAliases.contains(alias)) {
                knownAliases.add(alias);
                knownHosts.put(hostname, knownAliases);
            }
        } catch (KeyStoreException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String addCertToTrustStore(KeyStore userkeystore,
            X509Certificate... certs) {
        if (userkeystore != null && certs != null && certs.length > 0) {
            try {
                X509Certificate lastCert = certs[certs.length - 1];
                String dn = lastCert.getSubjectX500Principal().getName();
                BigInteger serial = lastCert.getSerialNumber();
                String proposedName = dn + "#" + serial.toString(16);
                String realName = proposedName;
                if (userkeystore.containsAlias(realName)) {
                    Integer count = 0;
                    while (userkeystore.containsAlias(realName)) {
                        realName = proposedName + "#" + Integer.toString(count);
                    }
                }
                userkeystore.setCertificateEntry(realName, lastCert);
                return userkeystore.getCertificateAlias(lastCert);
            } catch (KeyStoreException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return null;
    }

    public boolean checkTrusted(String hostname, Certificate cert) {
        try {
            String alias = keystore.getCertificateAlias(cert);
            if (alias != null) {
                List<String> knownAliases = knownHosts.get(hostname);
                if (knownAliases != null && knownAliases.contains(alias)) {
                    return true;
                }
            }
        } catch (KeyStoreException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }
}

class HostnameEntryAdapter extends XmlAdapter<HostnameEntry[], Map<String, ArrayList<String>>> {

    @Override
    public HostnameEntry[] marshal(Map<String, ArrayList<String>> v) throws
            Exception {
        HostnameEntry[] result = new HostnameEntry[v.size()];
        int index = 0;
        for (Entry<String, ArrayList<String>> e : v.entrySet()) {
            HostnameEntry he = new HostnameEntry();
            he.setHostname(e.getKey());
            he.setAliases(e.getValue());
            result[index] = he;
            index++;
        }
        return result;
    }

    @Override
    public Map<String, ArrayList<String>> unmarshal(HostnameEntry[] v) throws
            Exception {
        Map<String, ArrayList<String>> result =
                new HashMap<String, ArrayList<String>>();
        for (HostnameEntry he : v) {
            result.put(he.getHostname(), he.getAliases());
        }
        return result;
    }
}

@XmlRootElement
class HostnameEntry {

    private String hostname;
    private ArrayList<String> aliases;

    public HostnameEntry() {
    }

    public HostnameEntry(String hostname, ArrayList<String> aliases) {
        this.hostname = hostname;
        this.aliases = aliases;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<String> aliases) {
        this.aliases = aliases;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HostnameEntry other = (HostnameEntry) obj;
        if ((this.hostname == null) ? (other.hostname != null) : !this.hostname.
                equals(other.hostname)) {
            return false;
        }
        if (this.aliases != other.aliases && (this.aliases == null
                || !this.aliases.equals(other.aliases))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash
                + (this.hostname != null ? this.hostname.hashCode() : 0);
        hash = 97 * hash + (this.aliases != null ? this.aliases.hashCode() : 0);
        return hash;
    }
}