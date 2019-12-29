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

import com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui.CertificateCheckDialog;
import com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui.HostnameNotMatching;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.reverseArray;

public class HostnameVerifierImpl implements HostnameVerifier {

    private static HostnameCertificateStore hostnameAliasMap;
    private static HostnameVerifier originalHostnameVerifier;

    public static void install(HostnameCertificateStore hostnameAliasMap) {
        HostnameVerifierImpl.hostnameAliasMap = hostnameAliasMap;
        originalHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifierImpl());
    }

    public static void uninstall() {
        HttpsURLConnection.setDefaultHostnameVerifier(originalHostnameVerifier);
    }

    @Override
    public boolean verify(final String hostname, final SSLSession session) {
        try {
            if (originalHostnameVerifier.verify(hostname, session)) {
                return true;
            }
            final X509Certificate[] certificateChain =
                    reverseArray((X509Certificate[]) session.getPeerCertificates());

            if (isLastCertificateTrusted(certificateChain, hostname)) {
                return true;
            }

            final AtomicInteger resultRef = new AtomicInteger();
            try {
                Runnable edtUpdate = new Runnable() {

                    @Override
                    public void run() {
                        HostnameNotMatching ccd = new HostnameNotMatching(
                                certificateChain,
                                hostname);
                        ccd.setVisible(true);
                        resultRef.set(ccd.getResult());
                    }
                };
                if(SwingUtilities.isEventDispatchThread()) {
                    edtUpdate.run();
                } else {
                    SwingUtilities.invokeAndWait(edtUpdate);
                }
            } catch (InterruptedException | InvocationTargetException ex1) {
                Exceptions.printStackTrace(ex1);
            }
            int result = resultRef.get();
            if (result != CertificateCheckDialog.ACCEPT
                    && result != CertificateCheckDialog.PERMANENT) {
                return false;
            }
            if (result == CertificateCheckDialog.PERMANENT) {
                    addTrusted(certificateChain, hostname);
            }

            return true;
        } catch (SSLPeerUnverifiedException ex) {
            return false;
        }
    }

    private void addTrusted(X509Certificate[] certs, String hostname) {
        if (hostnameAliasMap != null && certs != null && certs.length > 0) {
            X509Certificate lastCert = certs[certs.length - 1];
            hostnameAliasMap.setTrusted(hostname, lastCert);
        }
    }

    private boolean isLastCertificateTrusted(X509Certificate[] certs, String hostname) {
        if (certs == null || certs.length == 0) {
            return false;
        }
        X509Certificate lastCert = certs[certs.length - 1];
        if (hostnameAliasMap != null) {
            return hostnameAliasMap.checkTrusted(hostname, lastCert);
        }
        return false;
    }
}
