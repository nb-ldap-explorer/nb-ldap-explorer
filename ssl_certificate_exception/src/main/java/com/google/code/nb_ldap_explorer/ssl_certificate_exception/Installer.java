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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    private static final Logger LOG = Logger.getLogger(Installer.class.getName());

    private static FileObject configBase = null;
    private static FileObject keystoreFile = null;
    private static FileObject hostnameAliasMapFile = null;
    private static KeyStore userkeystore = null;
    private static HostnameCertificateStore hostnameAliasMap = null;

    @Override
    public void restored() {
        try {
            super.restored();
            configBase = FileUtil.createFolder(FileUtil.getConfigRoot(), "SSLVerifier");
            keystoreFile = FileUtil.createData(configBase, "user.jks");
            hostnameAliasMapFile = FileUtil.createData(configBase, "hostnameAliasMap.xml");
            if (userkeystore == null) {
                userkeystore = KeyStore.getInstance("JKS");
            }
            try {
                if (keystoreFile.getSize() > 0) {
                    try (InputStream is = keystoreFile.getInputStream()) {
                        userkeystore.load(is, "".toCharArray());
                    } catch (CertificateException | NoSuchAlgorithmException | IOException ex) {
                        LOG.log(Level.INFO,
                                "Failed to load certificate store - going on with empty store",
                                ex);
                        userkeystore.load(null, "".toCharArray());
                    }
                } else {
                    userkeystore.load(null, "".toCharArray());
                }

            } catch (CertificateException | NoSuchAlgorithmException | IOException ex) {
                LOG.log(Level.WARNING, "Failed to initialize certificate store",
                        ex);
            }
            if (hostnameAliasMap == null) {
                if (hostnameAliasMapFile.getSize() > 0) {
                    JAXBContext jc = JAXBContext.newInstance(
                            "com.google.code.nb_ldap_explorer.ssl_certificate_exception",
                            Installer.class.getClassLoader());
                    Unmarshaller m = jc.createUnmarshaller();
                    try (InputStream is = hostnameAliasMapFile.getInputStream()) {
                        hostnameAliasMap = (HostnameCertificateStore) m.unmarshal(is);
                    } catch (JAXBException ex) {
                        LOG.
                                log(Level.INFO, "Failed to host alias map - going on with empty map",
                                        ex);
                        hostnameAliasMap = new HostnameCertificateStore();
                    }
                } else {
                    hostnameAliasMap = new HostnameCertificateStore();
                }
                hostnameAliasMap.setKeystore(userkeystore);
            }

            TrustProviderImpl.install(userkeystore);
            HostnameVerifierImpl.install(hostnameAliasMap);
        } catch (JAXBException ex) {
            LOG.log(Level.WARNING,
                    "Failed to create XML Unmarshaller - please open a bug report, this should not happen",
                    ex);
        } catch (KeyStoreException ex) {
            LOG.log(Level.WARNING, "Failed to create keystore for certificates (type: JKS)", ex);
        } catch (IOException ex) {
            LOG.log(Level.WARNING,
                    "Failed to create config files - please check, that the userdir is writeable, if it is please open a bug report, this should not happen.",
                    ex);
        }
    }

    private void saveState() {
        if (userkeystore != null && keystoreFile != null) {
            try (OutputStream os = keystoreFile.getOutputStream()) {
                userkeystore.store(os, "".toCharArray());
            } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException ex) {
                LOG.log(Level.INFO, "Failed to save hostname map", ex);
            }
        }
        try {
            if (hostnameAliasMap != null && hostnameAliasMapFile != null) {
                JAXBContext jc = JAXBContext.newInstance(
                        "com.google.code.nb_ldap_explorer.ssl_certificate_exception",
                        Installer.class.getClassLoader());
                Marshaller m = jc.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                try (OutputStream os = hostnameAliasMapFile.getOutputStream()) {
                    m.marshal(hostnameAliasMap, os);
                }
            }
            hostnameAliasMap = null;
            userkeystore = null;
        } catch (JAXBException | IOException ex) {
            LOG.log(Level.WARNING,
                    "Failed to create marshal hostname alias map",
                    ex);
        }
    }

    @Override
    public void close() {
        saveState();
        super.close();
    }

    @Override
    public void uninstalled() {
        saveState();
        super.uninstalled();
    }
}
