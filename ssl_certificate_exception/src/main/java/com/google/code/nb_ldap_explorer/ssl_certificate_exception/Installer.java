/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.code.nb_ldap_explorer.ssl_certificate_exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

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
                if (keystoreFile.getSize() > 0) {
                    InputStream is = keystoreFile.getInputStream();
                    userkeystore.load(is, "".toCharArray());
                    is.close();
                } else {
                    userkeystore.load(null, "".toCharArray());
                }
            }
            if (hostnameAliasMap == null) {
                if (hostnameAliasMapFile.getSize() > 0) {
                    JAXBContext jc = JAXBContext.newInstance(HostnameCertificateStore.class);
                    Unmarshaller m = jc.createUnmarshaller();
                    InputStream is = hostnameAliasMapFile.getInputStream();
                    hostnameAliasMap = (HostnameCertificateStore) m.unmarshal(is);
                    is.close();
                } else {
                    hostnameAliasMap = new HostnameCertificateStore();
                }
                hostnameAliasMap.setKeystore(userkeystore);
            }

            TrustProviderImpl.install(userkeystore);
            HostnameVerifierImpl.install(hostnameAliasMap);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (KeyStoreException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveState() {
        try {
            if (userkeystore != null && keystoreFile != null) {
                OutputStream os = keystoreFile.getOutputStream();
                userkeystore.store(os, "".toCharArray());
                os.close();
            }
            if (hostnameAliasMap != null && hostnameAliasMapFile != null) {
                JAXBContext jc = JAXBContext.newInstance(HostnameCertificateStore.class);
                Marshaller m = jc.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                OutputStream os = hostnameAliasMapFile.getOutputStream();
                m.marshal(hostnameAliasMap, os);
                os.close();
            }
            hostnameAliasMap = null;
            userkeystore = null;
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (KeyStoreException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
