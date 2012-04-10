package com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.parseX500Name;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.fingerprint;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.asHexString;

public class CertificateDisplay extends JPanel {

    private static final Map<String, String> relevantNames = new HashMap<String, String>();
    private Integer additionalPaddingX = 0;
    private Integer additionalPaddingY = 0;
    private final DateFormat mediumDate = DateFormat.getDateInstance(DateFormat.MEDIUM);

    static {
        relevantNames.put("OU", "Organisational unit");
        relevantNames.put("O", "Organisation");
        relevantNames.put("CN", "Common Name");
    }

    public CertificateDisplay() {
        setLayout(new GridBagLayout());
    }

    public void setCertificate(X509Certificate lastCert) {
        this.removeAll();

        GridBagConstraints headGbc = new GridBagConstraints();
        headGbc.gridwidth = 2;
        headGbc.insets = new Insets(2, 2, 2, 2);
        headGbc.weightx = 1;
        headGbc.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.gridwidth = 1;
        labelGbc.insets = new Insets(2, 2, 2, 2);
        labelGbc.weightx = 0;
        labelGbc.gridy = 1;
        labelGbc.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints valueGbc = new GridBagConstraints();
        valueGbc.gridwidth = 1;
        valueGbc.insets = new Insets(2, 2, 2, 2);
        valueGbc.weightx = 0;
        valueGbc.gridy = 1;
        valueGbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel label = new JLabel();
        Font headerFont = label.getFont().deriveFont(label.getFont().getStyle() | java.awt.Font.BOLD);
        Font normalFont = label.getFont().deriveFont(label.getFont().getStyle() & (Integer.MAX_VALUE ^ java.awt.Font.BOLD));

        headGbc.gridy = row++;
        label = new JLabel("Issued for");
        label.setFont(headerFont);
        add(label, headGbc);

        List<Entry<String, String>> subjectParts = parseX500Name(
                lastCert.getSubjectX500Principal().getName());

        for (Entry<String, String> e : subjectParts) {
            if (relevantNames.containsKey(e.getKey())) {
                labelGbc.gridy = row;
                valueGbc.gridy = row;
                String key = relevantNames.get(e.getKey()) + " (" + e.getKey() + "): ";
                label = new JLabel(key);
                label.setFont(normalFont);
                add(label, labelGbc);
                add(selectableLabel(e.getValue(), normalFont), valueGbc);
                row++;
            }
        }

        labelGbc.gridy = row;
        valueGbc.gridy = row;
        label = new JLabel("Serial: ");
        label.setFont(normalFont);
        add(label, labelGbc);
        add(selectableLabel(lastCert.getSerialNumber().toString(), normalFont), valueGbc);
        row++;

        headGbc.gridy = row++;
        label = new JLabel("Issued by");
        label.setFont(headerFont);
        add(label, headGbc);

        List<Entry<String, String>> issuerParts = parseX500Name(
                lastCert.getIssuerX500Principal().getName());

        for (Entry<String, String> e : issuerParts) {
            if (relevantNames.containsKey(e.getKey())) {
                labelGbc.gridy = row;
                valueGbc.gridy = row;
                String key = relevantNames.get(e.getKey()) + " (" + e.getKey() + "): ";
                label = new JLabel(key);
                label.setFont(normalFont);
                add(label, labelGbc);
                add(selectableLabel(e.getValue(), normalFont), valueGbc);
                row++;
            }
        }

        headGbc.gridy = row++;
        label = new JLabel("Validity");
        label.setFont(headerFont);
        add(label, headGbc);

        labelGbc.gridy = row;
        valueGbc.gridy = row;
        label = new JLabel("Valid from: ");
        label.setFont(normalFont);
        add(label, labelGbc);
        add(selectableLabel(mediumDate.format(lastCert.getNotBefore()), normalFont), valueGbc);
        row++;

        labelGbc.gridy = row;
        valueGbc.gridy = row;
        label = new JLabel("Valid till: ");
        label.setFont(normalFont);
        add(label, labelGbc);
        add(selectableLabel(mediumDate.format(lastCert.getNotAfter()), normalFont), valueGbc);
        row++;

        headGbc.gridy = row++;
        label = new JLabel("Fingerprint");
        label.setFont(headerFont);
        add(label, headGbc);

        labelGbc.gridy = row;
        valueGbc.gridy = row;
        label = new JLabel("SHA1-Fingerprint: ");
        label.setFont(normalFont);
        add(label, labelGbc);
        add(selectableLabel(asHexString(fingerprint(lastCert, "SHA-1")), normalFont), valueGbc);
        row++;
        labelGbc.gridy = row;
        valueGbc.gridy = row;
        label = new JLabel("MD5-Fingerprint: ");
        label.setFont(normalFont);
        add(label, valueGbc);
        add(selectableLabel(asHexString(fingerprint(lastCert, "MD5")), normalFont), valueGbc);
        row++;

        Box.Filler filler = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        add(filler, gbc);

        additionalPaddingX = row * 2;
        additionalPaddingY = ((GridBagLayout) getLayout()).getLayoutDimensions()[0].length * 2;

        doLayout();
        revalidate();
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height += additionalPaddingX + 10;
        d.width += additionalPaddingY + 10;
        return d;
    }

    public static void main(String[] argv) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certCol = cf.generateCertificates(new FileInputStream("/home/mblaesing/banking.postbank.de"));
        X509Certificate[] certs = certCol.toArray(new X509Certificate[0]);
        CertificateDisplay cd = new CertificateDisplay();
        cd.setCertificate(certs[certs.length - 1]);
        JFrame f = new JFrame();
        f.add(cd);
        f.setSize(cd.getPreferredSize());
        f.setVisible(true);
    }

    private JTextField selectableLabel(String displayValue, Font font) {
        JTextField f = new JTextField(displayValue);
        f.setEditable(false);
        f.setBackground(null);
        f.setBorder(null);
        f.setFont(font);
        return f;
    }
}
