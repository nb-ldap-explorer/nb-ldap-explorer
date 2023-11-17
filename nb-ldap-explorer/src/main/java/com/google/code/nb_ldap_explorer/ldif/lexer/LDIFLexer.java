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

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;
import com.google.code.nb_ldap_explorer.ldif.javacclexer.JavaCharStream;
import com.google.code.nb_ldap_explorer.ldif.javacclexer.LDIF_ParserTokenManager;
import com.google.code.nb_ldap_explorer.ldif.javacclexer.Token;
import com.google.code.nb_ldap_explorer.ldif.javacclexer.TokenMgrError;

public class LDIFLexer implements Lexer<LDIFTokenId>{

    private Integer state = 0;
    private LexerInput li;
    private LexerRestartInfo<LDIFTokenId> lri;
    private LDIF_ParserTokenManager ldiftokenmanager;

    public LDIFLexer(LexerRestartInfo<LDIFTokenId> lri) {
        this.lri = lri;
        this.li = lri.input();
        this.state = (Integer) lri.state();
        if(this.state == null) this.state = 0;
        this.ldiftokenmanager = new LDIF_ParserTokenManager(new JavaCharStream(li));
    }

    @Override
    public org.netbeans.api.lexer.Token<LDIFTokenId> nextToken() {
        // We are in the error state => stop tokenizing, as we consumed the
        // stream completely
        if(state < 0) {
            while(li.read() != LexerInput.EOF) {}
            return null;
        }

        ldiftokenmanager.SwitchTo(state);
        Token token;
        try {
            token = ldiftokenmanager.getNextToken();
        } catch (TokenMgrError error) {
            // Fallback to Default Parsing State
            // => do as much recovery as possible
            ldiftokenmanager.SwitchTo(0);
            state = 0;
            try {
                token = ldiftokenmanager.getNextToken();
            } catch (TokenMgrError error2) {
                // No we got an unrecoverable error => Error State!
                state = -1;
                while(li.read() != LexerInput.EOF) {}
                return lri.tokenFactory().createToken(LDIFLanguageHierarchy.getToken(LDIF_ParserTokenManager.DN64_VALUE+1));
            }
        }

        if (lri.input().readLength() < 1) {
            return null;
        }
        // The statechange is modelled after the parser defined in the
        // Maybee, this could be made easier
        if (token.kind == LDIF_ParserTokenManager.DN64_START) {
            state = LDIF_ParserTokenManager.DN64_VALUE_S;
        } else if (token.kind == LDIF_ParserTokenManager.ATTVAL_SEP_64) {
            state = LDIF_ParserTokenManager.DN64_VALUE_S;
        } else if (token.kind == LDIF_ParserTokenManager.ATTVAL_SEP_URL) {
            state = LDIF_ParserTokenManager.ATTVALUE_S_URL;
        } else if (token.kind == LDIF_ParserTokenManager.URL) {
            state = LDIF_ParserTokenManager.DEFAULT;
        } else if (token.kind == LDIF_ParserTokenManager.ATTVAL_SEP_64) {
            state = LDIF_ParserTokenManager.ATTVALUE_S_64;
        } else if (token.kind == LDIF_ParserTokenManager.BASE64_ATTVALUE) {
            state = LDIF_ParserTokenManager.DEFAULT;
        } else if (token.kind == LDIF_ParserTokenManager.ATTVAL_SEP) {
            state = LDIF_ParserTokenManager.ATTVALUE_S;
        } else if (token.kind == LDIF_ParserTokenManager.ATTVALUE) {
            state = LDIF_ParserTokenManager.DEFAULT;
        }  else {
            state = ldiftokenmanager.getLexerState();
        }
        
        org.netbeans.api.lexer.Token t = lri.tokenFactory().createToken(LDIFLanguageHierarchy.getToken(token.kind));
        return t;
    }

    @Override
    public Object state() {
        return state;
    }

    @Override
    public void release() {

    }

}
