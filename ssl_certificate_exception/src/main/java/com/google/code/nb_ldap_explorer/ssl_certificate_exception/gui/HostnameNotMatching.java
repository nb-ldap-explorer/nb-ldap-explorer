package com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.util.Map.Entry;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.parseX500Name;
import static com.google.code.nb_ldap_explorer.ssl_certificate_exception.util.Utilities.asHexString;

public class HostnameNotMatching extends JDialog {

    public final static int ACCEPT = 1;
    public final static int PERMANENT = 3;
    public final static int DECLINE = 2;
    private int result = DECLINE;

    public HostnameNotMatching(X509Certificate[] certificateChain, String hostname) {
        setModal(true);
        setTitle("Certificate does not match hostname");
        setLocationByPlatform(true);
        setDefaultCloseOperation(
                JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                result = DECLINE;
                setVisible(false);
            }
        });
        setLayout(new BorderLayout());
        CertificateChainDisplay cd = new CertificateChainDisplay();
        cd.setCertificateChain(certificateChain);
        cd.setUserMessage(createMessage(certificateChain[certificateChain.length - 1], hostname));
        add(cd, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton declineButton = new JButton("Decline");
        JButton acceptButton = new JButton("Accept once");
        JButton acceptForeverButton = new JButton("Accept forever");
        buttons.add(declineButton);
        buttons.add(acceptButton);
        buttons.add(acceptForeverButton);
        acceptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = ACCEPT;
                setVisible(false);
            }
        });
        acceptForeverButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = PERMANENT;
                setVisible(false);
            }
        });
        declineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = DECLINE;
                setVisible(false);
            }
        });
        add(buttons, BorderLayout.SOUTH);
        setSize(getPreferredSize());
    }

    public int getResult() {
        return result;
    }

    private JPanel createMessage(X509Certificate lastCert, String hostname) {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JLabel label = new JLabel();
        Font labelFont = label.getFont().deriveFont(label.getFont().getStyle() | java.awt.Font.BOLD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel heading = new JLabel("Target hostname does not match certificate");
        heading.setFont(labelFont);
        p.add(heading, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        heading = new JLabel("Validated names from certificate:");
        p.add(heading, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(2, 2, 2, 2);


        JTextPane tv = new JTextPane();
        tv.setText(getHostNamesFromCert(lastCert));
        p.add(new JScrollPane(tv), gbc);
        return p;
    }

    private String getHostNamesFromCert(X509Certificate cert) {
        StringBuilder sb = new StringBuilder();

        String x509subject = cert.getSubjectX500Principal().getName();
        List<Entry<String, String>> nameparts = parseX500Name(x509subject);

        for (Entry<String, String> e : nameparts) {
            if (e.getKey().equals("CN")) {
                sb.append(e.getValue());
                sb.append("\n");
                break;
            }
        }
        try {
            if (cert.getSubjectAlternativeNames() != null) {
                for (List l : cert.getSubjectAlternativeNames()) {
                    Integer type = (Integer) l.get(0);
                    for (int i = 1; i < l.size(); i++) {
                        Object value = l.get(i);
                        sb.append(subjectName.values()[type]);
                        sb.append(":\t");
                        if (value instanceof byte[]) {
                            sb.append(asHexString((byte[]) value));
                        } else {
                            sb.append(value.toString());
                        }
                        sb.append("\n");
                    }
                }
            }
        } catch (CertificateParsingException ex) {
        }

        return sb.toString();
    }
}

enum subjectName {
    otherName,
    rfc822Name,
    dNSName,
    x400Address,
    directoryName,
    ediPartyName,
    uniformResourceIdentifier,
    iPAddress,
    registeredID
};
