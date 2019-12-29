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

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

public final class TrustProviderImpl extends java.security.Provider {

    private static Logger LOG = Logger.getLogger(TrustProviderImpl.class.getName());
    private static TrustManagerFactory originalTrustManagerFactory;
    private static String originalAlgorithm;
    private static KeyStore userKeyStore;
    private final static String NAME = "XTrustJSSE";
    private final static String INFO =
            "XTrust JSSE Provider (implements trust factory with truststore validation disabled)";
    private final static double VERSION = 1.0D;

    public TrustProviderImpl() {
        super(NAME, VERSION, INFO);

        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                put("TrustManagerFactory." + TrustManagerFactoryImpl.getAlgorithm(), TrustManagerFactoryImpl.class.getName());
                return null;
            }
        });
    }

    public static void install(KeyStore keystore) {
        userKeyStore = keystore;
        if (Security.getProvider(NAME) == null) {
            try {
                originalAlgorithm = Security.getProperty("ssl.TrustManagerFactory.algorithm");
                originalTrustManagerFactory = TrustManagerFactory.getInstance(originalAlgorithm);
            } catch (NoSuchAlgorithmException ex) {
                LOG.log(Level.WARNING, "Failed to load initial SSL Algorithm", ex);
            }
            Security.insertProviderAt(new TrustProviderImpl(), 1);
            Security.setProperty("ssl.TrustManagerFactory.algorithm",
                    TrustManagerFactoryImpl.getAlgorithm());
            LOG.log(Level.INFO, "{0} installed", TrustProviderImpl.class.getName());
        }
    }

    public static void uninstall() {
        if (originalAlgorithm != null) {
            Security.setProperty("ssl.TrustManagerFactory.algorithm",
                    originalAlgorithm);
        }
        Security.removeProvider(NAME);
    }

    public final static class TrustManagerFactoryImpl extends TrustManagerFactorySpi {

        public TrustManagerFactoryImpl() {
        }

        public static String getAlgorithm() {
            return "XTrust509";
        }

        @Override
        protected void engineInit(KeyStore keystore) throws KeyStoreException {
            originalTrustManagerFactory.init(keystore);
        }

        @Override
        protected void engineInit(ManagerFactoryParameters mgrparams)
                throws InvalidAlgorithmParameterException {
            originalTrustManagerFactory.init(mgrparams);
        }

        protected TrustManager[] engineGetTrustManagers() {
            X509TrustManager originalTrustManager = null;

            if (originalTrustManagerFactory != null) {
                for (TrustManager t : originalTrustManagerFactory.getTrustManagers()) {
                    if (t instanceof X509TrustManager) {
                        originalTrustManager = (X509TrustManager) t;
                        break;
                    }

                }
            }

            final X509TrustManager fOriginalTrustManager = originalTrustManager;

            return new TrustManager[]{new TrustManagerImpl(fOriginalTrustManager, userKeyStore)};

        }
    }
}
