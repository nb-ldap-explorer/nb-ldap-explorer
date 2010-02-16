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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Allan Lykke Christensen
 */
public class LdapEntry {

    private String dn;
    private Map<String, ArrayList<Object>> attributes =
            new HashMap<String, ArrayList<Object>>();

    /**
     * Creates a new instance of {@link LdapEntry}.
     */
    public LdapEntry() {
        this("");
    }

    /**
     * Creates a new instance of {@link LdapEntry}.
     *
     * @param dn
     *          Distinguished name of the entry
     */
    public LdapEntry(String dn) {
        this.dn = dn;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public Map<String, ArrayList<Object>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, ArrayList<Object>> attributes) {
        this.attributes = attributes;
    }

    public List<Object> getAttribute(String name) throws
            NotSuchAttributeException {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        } else {
            throw new NotSuchAttributeException(name + " does not exist");
        }
    }

    public void setAttribute(String attribute, Object value) {
        if (attributes.containsKey(attribute)) {
            List<Object> objs = attributes.get(attribute);
            objs.add(value);
        } else {
            ArrayList<Object> objs = new ArrayList<Object>();
            objs.add(value);
            attributes.put(attribute, objs);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LdapEntry other = (LdapEntry) obj;
        if ((this.dn == null) ? (other.dn != null) : !this.dn.equals(other.dn)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.dn != null ? this.dn.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.dn;
    }

    /**
     * Gets the LDIF representation of the entry.
     *
     * @return LDIF representation of the entry
     */
    public String toLDIF() {
        StringBuilder ldif = new StringBuilder("dn: ");
        ldif.append(this.dn);
        ldif.append(System.getProperty("line.separator"));

        for (String key : this.attributes.keySet()) {

            for (Object val : this.attributes.get(key)) {
                ldif.append(key);
                ldif.append(": ");
                ldif.append(val);
                ldif.append(System.getProperty("line.separator"));
            }
        }

        return ldif.toString();
    }
}
