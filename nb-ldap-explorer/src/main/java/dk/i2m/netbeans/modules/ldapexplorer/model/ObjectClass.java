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

/**
 * Enmeration containing the priority of object classes. The priority is used
 * to decide which icon to display if an {@link LdapEntry} has multiple
 * object classes.
 *
 * @author Allan Lykke Christensen
 */
public enum ObjectClass {

    domain, organization, organizationalUnit, groupOfUniqueNames, groupOfNames,
    inetOrgPerson, person, account, posixAccount, dominoServer, dominoGroup, WebSite, unknown;
}
