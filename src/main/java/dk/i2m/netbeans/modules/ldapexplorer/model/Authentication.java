/*
 *  Copyright 2010 Allan Lykke Christensen.
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
package dk.i2m.netbeans.modules.ldapexplorer.model;

import java.util.ResourceBundle;
import org.openide.util.NbBundle;

/**
 * Type of {@link Authentication} when connecting to an {@link LdapServer}.
 *
 * @author Allan Lykke Christensen
 */
public enum Authentication {

    /** Anonymous connection. */
    NONE,
    /** Simple username and password authentication. */
    SIMPLE,
    /** Kerberos 5  **/
    KERBEROS5
    ;
    private static final ResourceBundle bundle = NbBundle.getBundle(
            Authentication.class);

    @Override
    public String toString() {
        return this.name();
    }
}
