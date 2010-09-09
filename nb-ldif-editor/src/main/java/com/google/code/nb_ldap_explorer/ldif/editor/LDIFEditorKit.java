package com.google.code.nb_ldap_explorer.ldif.editor;

import org.netbeans.modules.editor.NbEditorKit;

public class LDIFEditorKit extends NbEditorKit {

    public LDIFEditorKit() {
        super();
    }

    public String getContentType() {
        return "text/ldif";
    }
}
