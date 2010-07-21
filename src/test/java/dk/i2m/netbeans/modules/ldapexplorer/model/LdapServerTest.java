/*
 *  Copyright 2010 Interactive Media Management
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

import java.util.List;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author Allan Lykke Christensen
 */
public class LdapServerTest {

    public LdapServerTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    /**
     * Test of connect method, of class LdapServer.
     */
    @org.junit.Test
    @Ignore("Need a test harness")
    public void testConnect() throws Exception {
        System.out.println("connect");
        BaseLdapServer server = new BaseLdapServer();
        server.setHost("localhost");
        server.setPort(1636);
        server.setAuthentication(Authentication.NONE);
        server.setBaseDN("dc=mytimes,dc=com");
        server.setSecure(true);
        server.setTimeout(5000);

        try {
            System.out.println("Connect");
            server.connect();
            System.out.println("Get Tree");
            List<LdapEntry> entries = server.getTree("");
            for (LdapEntry entry : entries) {
                System.out.println(entry.getDn());
            }
            System.out.println("Disconnecting");
            server.disconnect();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
