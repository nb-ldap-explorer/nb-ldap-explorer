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

package com.google.code.nb_ldap_explorer.ldif.javacclexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.netbeans.spi.lexer.LexerInput;

public class JavaCharStream {

    private LexerInput input;

    static boolean staticFlag;

    public JavaCharStream(LexerInput li) {
        input = li;
    }

    public JavaCharStream(InputStream s, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    void ReInit(InputStream s, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public JavaCharStream(Reader s, int i, int i0) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    void ReInit(Reader s, int i, int i0) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    char BeginToken() throws IOException {
        char result = (char) input.read();
        if(result == 0xFFFF) {
            throw new IOException("LexerInput EOF");
        }
        return result;
    }

    String GetImage() {
        return input.readText().toString();
    }

    public char[] GetSuffix(int len) {
        if(len > input.readLength()) {
            throw new IllegalArgumentException();
        }
        return input.readText(input.readLength() - len, input.readLength()).toString().toCharArray();
    }

    void backup(int i) {
        input.backup(i);
    }

    int getBeginLine() {
        return 0;
    }

    int getBeginColumn() {
        return 0;
    }

    int getEndColumn() {
        return 0;
    }

    int getEndLine() {
        return 0;
    }

    char readChar() throws IOException {
        char result = (char) input.read();
        if(result == 0xFFFF) {
            throw new IOException("LexerInput EOF");
        }
        return result;
    }
}
