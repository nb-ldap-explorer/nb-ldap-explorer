package com.google.code.nb_ldap_explorer.ssl_certificate_exception.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.swing.*;

public class CertificateCheckDialog extends JDialog {

    public final static int ACCEPT = 1;
    public final static int PERMANENT = 3;
    public final static int DECLINE = 2;
    private int result = DECLINE;

    public CertificateCheckDialog(X509Certificate[] certificateChain, CertificateException ex) {
        setModal(true);
        setTitle("Invalid Certificate");
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
        cd.setUserMessage(createMessage(ex));
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

    private JPanel createMessage(CertificateException ex) {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        JLabel label = new JLabel();
        Font labelFont = label.getFont().deriveFont(label.getFont().getStyle() | java.awt.Font.BOLD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel heading = new JLabel("Error checking certificate");
        heading.setFont(labelFont);
        p.add(heading, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        JTextPane tv = new JTextPane();
        tv.setText(ex.getLocalizedMessage());
        p.add(new JScrollPane(tv), gbc);
        return p;
    }
}
