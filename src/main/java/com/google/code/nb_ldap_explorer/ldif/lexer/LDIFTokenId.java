/*
 *  Copyright 2010 Matthias Bläsing.
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
