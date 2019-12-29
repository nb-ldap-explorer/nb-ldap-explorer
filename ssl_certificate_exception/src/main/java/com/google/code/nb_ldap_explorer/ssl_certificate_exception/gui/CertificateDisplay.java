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

package com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.parseX500Name;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.fingerprint;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.asHexString;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CertificateDisplay extends JPanel {
    private static final Logger LOG = Logger.getLogger(CertificateDisplay.class.getName());
    private static final Set<String> warnedMissingAlgorithms = Collections.synchronizedSet(new HashSet<String>());
    private static final String[] fingerprintingAlgorithms = new String[]{"SHA-256", "SHA-1", "MD5"};
    private static final Map<String, String> relevantNames = new HashMap<>();
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
        add(selectableLabel(asHexString(lastCert.getSerialNumber().toByteArray()), normalFont), valueGbc);
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

        for (String fingerprintingAlgorithm : fingerprintingAlgorithms) {
            try {
                String fingerprint = asHexString(fingerprint(lastCert, fingerprintingAlgorithm));
                labelGbc.gridy = row;
                valueGbc.gridy = row;
                label = new JLabel(String.format("%s-Fingerprint: ", fingerprintingAlgorithm));
                label.setFont(normalFont);
                add(label, labelGbc);
                add(selectableLabel(fingerprint, normalFont), valueGbc);
                row++;
            } catch (NoSuchAlgorithmException ex) {
                if(! warnedMissingAlgorithms.contains(fingerprintingAlgorithm)) {
                    LOG.log(Level.INFO, "Digest Algorithm " + fingerprintingAlgorithm + " could not be found", ex);
                    warnedMissingAlgorithms.add(fingerprintingAlgorithm);
                }
            }
        }

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

    private JTextField selectableLabel(String displayValue, Font font) {
        JTextField f = new JTextField(displayValue);
        f.setEditable(false);
        f.setBackground(null);
        f.setBorder(null);
        f.setFont(font);
        return f;
    }
}
