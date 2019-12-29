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

package com.google.code.nb_ldap_explorer.ldif.parser;

import java.io.Reader;
import java.io.StringReader;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import com.google.code.nb_ldap_explorer.ldif.javacclexer.LDIF_Parser;

public class LDIFParser extends Parser {
    private Snapshot snapshot;
    private LDIF_Parser ldifParser;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        Reader reader = new StringReader(snapshot.getText().toString());
        ldifParser = new LDIF_Parser(reader);
    }

    @Override
    public Result getResult(Task task) {
        return new LDIFParserResult(snapshot, ldifParser);
    }

    @Override
    public void cancel() {}

    @Override
    public void addChangeListener(ChangeListener changeListener) {}

    @Override
    public void removeChangeListener(ChangeListener changeListener) {}

    public static class LDIFParserResult extends Result {
        private LDIF_Parser ldifparser;
        private boolean valid = true;

        LDIFParserResult(Snapshot snapshot, LDIF_Parser ldifparser) {
            super(snapshot);
            this.ldifparser = ldifparser;
        }

        public LDIF_Parser getLDIF_Parser() throws org.netbeans.modules.parsing.spi.ParseException {
            if(! valid) throw new org.netbeans.modules.parsing.spi.ParseException();
            return ldifparser;
        }

        @Override
        protected void invalidate() {
            valid = false;
        }
    }
}
