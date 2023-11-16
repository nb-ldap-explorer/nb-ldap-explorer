/*
 * Copyright 2023 Interactive Media Management.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.i2m.netbeans.modules.ldapexplorer.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import org.openide.util.Exceptions;


public class TLSUncheckedSocketFactory extends SSLSocketFactory {

    private static SSLSocketFactory DELEGATING_FACTORY;

    static {
        try {
            TrustManager[] byPassTrustManagers = new TrustManager[]{
                new X509ExtendedTrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType) {
                        // Don't check
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType) {
                        // Don't check
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType, Socket socket) throws
                            CertificateException {
                        // Don't check
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType, Socket socket) throws
                            CertificateException {
                        // Don't check
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType, SSLEngine engine) throws
                            CertificateException {
                        // Don't check
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType, SSLEngine engine) throws
                            CertificateException {
                        // Don't check
                    }

                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            DELEGATING_FACTORY = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static SocketFactory getDefault() {
        return new TLSUncheckedSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return DELEGATING_FACTORY.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return DELEGATING_FACTORY.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port,
            boolean autoClose) throws IOException {
        return DELEGATING_FACTORY.createSocket(s, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException {
        return DELEGATING_FACTORY.createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost,
            int localPort) throws IOException, UnknownHostException {
        return DELEGATING_FACTORY.createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return DELEGATING_FACTORY.createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress address, int port,
            InetAddress localAddress, int localPort) throws IOException {
        return DELEGATING_FACTORY.createSocket(address, port, localAddress,
                localPort);
    }

    @Override
    public Socket createSocket(Socket s, InputStream consumed, boolean autoClose)
            throws IOException {
        return DELEGATING_FACTORY.createSocket(s, consumed, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return DELEGATING_FACTORY.createSocket();
    }

}
