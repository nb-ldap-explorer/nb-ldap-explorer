/*
 *  Copyright 2010 mblaesing.
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

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.security.auth.login.AppConfigurationEntry;

public enum Krb5LoginConf {
    SYSTEM_ACCOUNT("Existing Login/Ticket"),
    PRINCIPAL_PASSWORD("Principal/Password"),
    PRINCIPAL_KEYTAB("Principal/Keytab");

    private String title;
    private static Logger l = Logger.getLogger(Krb5LoginConf.class.getCanonicalName());

    private Krb5LoginConf(String title) {
        this.title = title;
    }

    public String toString() {
        return title;
    }

    public javax.security.auth.login.Configuration getLoginConfiguration(
            String principal,
            File keytab) {
        final HashMap<String, String> options = new HashMap<String, String>();

        switch(this) {
            case SYSTEM_ACCOUNT:
                options.put("useTicketCache", "true");
                options.put("doNotPrompt", "true");
                break;
            case PRINCIPAL_PASSWORD:
                options.put("useTicketCache", "false");
                options.put("doNotPrompt", "false");
                options.put("principal", principal != null ? principal : "");
                break;
            case PRINCIPAL_KEYTAB:
                options.put("doNotPrompt", "true");
                options.put("useTicketCache", "false");
                options.put("useKeyTab", "true");
                options.put("principal", principal != null ? principal : "");
                options.put("keyTab",  keytab != null ? keytab.toString() : "");
                break;
        }

        l.info("Options: " + options.toString());

        final javax.security.auth.login.Configuration c =
                new javax.security.auth.login.Configuration() {

                    @Override
                    public AppConfigurationEntry[] getAppConfigurationEntry(
                            String name) {
                        return new javax.security.auth.login.AppConfigurationEntry[]{
                                    new javax.security.auth.login.AppConfigurationEntry(
                                    "com.sun.security.auth.module.Krb5LoginModule",
                                    javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                    options)
                                };
                    }
                };
        return c;
    }

}
