/*
 * The contents of this file are subject to the "END USER LICENSE AGREEMENT FOR F5
 * Software Development Kit for iControl"; you may not use this file except in
 * compliance with the License. The License is included in the iControl
 * Software Development Kit.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is iControl Code and related documentation
 * distributed by F5.
 *
 * Portions created by F5 are Copyright (C) 1996-2004 F5 Networks
 * Inc. All Rights Reserved.  iControl (TM) is a registered trademark of
 * F5 Networks, Inc.
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the GNU General Public License (the "GPL"), in which case the
 * provisions of GPL are applicable instead of those above.  If you wish
 * to allow use of your version of this file only under the terms of the
 * GPL and not to allow others to use your version of this file under the
 * License, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the GPL.
 * If you do not delete the provisions above, a recipient may use your
 * version of this file under either the License or the GPL.
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
