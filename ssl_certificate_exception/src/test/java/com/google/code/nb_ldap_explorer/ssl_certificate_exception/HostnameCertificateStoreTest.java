package com.google.code.nb_ldap_explorer.ssl_certificate_exception;


import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;


public class HostnameCertificateStoreTest {
    @Test
    public void testCheck() throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        HostnameCertificateStore hcs = new HostnameCertificateStore();
        hcs.setKeystore(ks);

        CertificateFactory cf = CertificateFactory.getInstance("X509");
        try(InputStream is = HostnameCertificateStoreTest.class.getResourceAsStream("cert.pem")) {
            Certificate c = cf.generateCertificate(is);
            hcs.setTrusted("demohost1.local", c);

            assertTrue(hcs.checkTrusted("demohost1.local", c));
            assertFalse(hcs.checkTrusted("demohost2.local", c));
        }
    }
}
