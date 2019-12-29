package com.google.code.nb_ldap_explorer.ssl_certificate_exception;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Test
    public void testSerialization() throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        HostnameCertificateStore hcs = new HostnameCertificateStore();
        hcs.setKeystore(ks);

        CertificateFactory cf = CertificateFactory.getInstance("X509");
        Certificate c;
        try(InputStream is = HostnameCertificateStoreTest.class.getResourceAsStream("cert.pem")) {
            c = cf.generateCertificate(is);
        }
        hcs.setTrusted("demohost1.local", c);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        hcs.writeToOutputStream(baos);

        HostnameCertificateStore hcs2 = new HostnameCertificateStore();
        hcs2.setKeystore(ks);
        hcs2.readFromInputStream(new ByteArrayInputStream(baos.toByteArray()));

        assertTrue(hcs2.checkTrusted("demohost1.local", c));
        assertFalse(hcs2.checkTrusted("demohost2.local", c));
    }
}
