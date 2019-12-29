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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HostnameCertificateStore {

    private KeyStore keystore;
    private Map<String, ArrayList<String>> knownHosts = new HashMap<String, ArrayList<String>>();

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
                knownHosts.put(hostname, knownAliases);
            }
            if (!knownAliases.contains(alias)) {
                knownAliases.add(alias);
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

    public void readFromInputStream(InputStream is) throws IOException {
        this.knownHosts.clear();
        if (is == null) {
            return;
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            NodeList knownHosts = doc.getElementsByTagName("knownHosts");
            for(int i = 0; i < knownHosts.getLength(); i++) {
                Element knownHost = (Element) knownHosts.item(i);
                NodeList items = knownHost.getElementsByTagName("item");
                for(int j = 0; j < items.getLength(); j++) {
                    Element item = (Element) items.item(j);
                    NodeList aliases = item.getElementsByTagName("aliases");
                    NodeList hostnames = item.getElementsByTagName("hostname");
                    if(aliases.getLength() > 0 && hostnames.getLength() == 1) {
                        ArrayList<String> aliasStrings = new ArrayList<>();
                        for(int k = 0; k < aliases.getLength(); k++) {
                            aliasStrings.add(aliases.item(k).getTextContent());
                        }
                        this.knownHosts.put(((Element) hostnames.item(0)).getTextContent(), aliasStrings);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException(ex);
        }
    }

    public void writeToOutputStream(OutputStream os) throws IOException {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            doc.setXmlStandalone(true);
            Element rootElement = doc.createElement("hostnameCertificateStore");
            Element knownHostsElement = doc.createElement("knownHosts");
            doc.appendChild(rootElement);
            rootElement.appendChild(knownHostsElement);
            for (Entry<String, ArrayList<String>> e : this.knownHosts.entrySet()) {
                Element item = doc.createElement("item");
                for (String alias : e.getValue()) {
                    Element aliases = doc.createElement("aliases");
                    aliases.setTextContent(alias);
                    item.appendChild(aliases);
                }
                Element hostname = doc.createElement("hostname");
                hostname.setTextContent(e.getKey());
                item.appendChild(hostname);
                knownHostsElement.appendChild(item);
            }
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(os));
        } catch (ParserConfigurationException | TransformerException ex) {
            throw new IOException(ex);
        }
    }
}
