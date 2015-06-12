package com.google.code.nb_ldap_explorer.ssl_certificate_exception.util;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utilities {

    public static List<Map.Entry<String, String>> parseX500Name(String stringForm) {
        List<Map.Entry<String, String>> result = new ArrayList<>();
        for (String s : stringForm.split("(?<!\\\\),")) {
            String[] part = s.split("=", 2);
            AbstractMap.SimpleImmutableEntry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>(
                    part[0].replaceAll("\\\\(.)", "$1"),
                    part[1].replaceAll("\\\\(.)", "$1"));
            result.add(entry);
        }
        return result;
    }

    public static byte[] fingerprint(X509Certificate cert, String digestAlg) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlg);
            byte[] der = cert.getEncoded();
            md.update(der);
            byte[] digest = md.digest();
            return digest;
        } catch (CertificateEncodingException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String asHexString(byte bytes[]) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            short value = (short) (((short) bytes[i]) & 255);
            String hexRepresentation = Integer.toString(value, 16).toUpperCase();
            if (buf.length() != 0) {
                buf.append(":");
            }
            if (hexRepresentation.length() < 2) {
                buf.append("0");
            }
            buf.append(hexRepresentation);
        }

        return buf.toString();
    }
    
    public static <E> E reverseArray(E input) {
        if(input == null) {
            return null;
        }
        if(! input.getClass().isArray()) {
            throw new IllegalArgumentException("Only arrays are supported");
        }
        int length = Array.getLength(input);
        E result = (E) Array.newInstance(input.getClass().getComponentType(), length);
        for(int i = 0; i < length; i++) {
            Array.set(result, i, Array.get(input, length - i - 1));
        }
        return result;
    }
}
