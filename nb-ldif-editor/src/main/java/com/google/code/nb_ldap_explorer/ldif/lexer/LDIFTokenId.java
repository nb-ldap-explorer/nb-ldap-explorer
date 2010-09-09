/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.code.nb_ldap_explorer.ldif.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author mblaesing
 */
public class LDIFTokenId implements TokenId {
    private final String name;
    private final String primaryCategory;
    private final int id;

    public LDIFTokenId(String name, String pramaryCategory, int id) {
        this.name = name;
        this.primaryCategory = pramaryCategory;
        this.id = id;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public String name() {
        return name;
    }

    private static final Language<LDIFTokenId> language = new LDIFLanguageHierarchy().language();

    public static final Language<LDIFTokenId> getLanguage() {
        return language;
    }
}
