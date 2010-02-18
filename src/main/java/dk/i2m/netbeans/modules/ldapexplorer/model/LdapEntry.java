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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an entry in an LDAP tree.
 *
 * @author Allan Lykke Christensen
 */
public class LdapEntry {

    private String label;
    private String dn;
    private Map<String, ArrayList<Object>> attributes =
            new HashMap<String, ArrayList<Object>>();

    /**
     * Creates a new instance of {@link LdapEntry}.
     */
    public LdapEntry() {
        this("", "");
    }

    /**
     * Creates a new instance of {@link LdapEntry}.
     *
     * @param dn
     *          Distinguished name of the entry
     * @param label
     *          Label of the entry
     */
    public LdapEntry(String dn, String label) {
        this.dn = dn;
        this.label = label;
    }

    /**
     * Gets the distinguished name of the entry relative to the base.
     *
     * @return Distinguished name of the entry relative to the base.
     */
    public String getDn() {
        return dn;
    }

    /**
     * Sets the distinguished name of the entry relative to the base.
     *
     * @param dn
     *          Distinguished name of the entry relative to the base
     */
    public void setDn(String dn) {
        this.dn = dn;
    }

    /**
     * Gets the label of the entry. The label is usually the first part of the
     * distinguished name, e.g. <code>dn: uid:alc,ou=people</code> would have a
     * label: <code>uid:alc</code>.
     *
     * @return Label of the entry
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the entry. The label should be the first part of the
     * distinguished name.
     *
     * @param label
     *          Label of the entry
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the attributes of the entry. As each attribute can contain multiple
     * values, the attributes are stored in a map with the key being the name
     * of the attribute, and the value being an {@link ArrayList} of values
     * for the attribute.
     *
     * @return Map containing the attributes of the entry.
     */
    public Map<String, ArrayList<Object>> getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes of the entry.
     *
     * @param attributes
     *          Map containing the attributes of the entry
     */
    public void setAttributes(Map<String, ArrayList<Object>> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets the values of a given attribute.
     *
     * @param name
     *          Name of the attribute
     * @return {@link List} of values stored for the given attribute
     * @throws NotSuchAttributeException
     *          If the attribute does not exist
     */
    public List<Object> getAttribute(String name) throws
            NotSuchAttributeException {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        } else {
            throw new NotSuchAttributeException(name + " does not exist");
        }
    }

    /**
     * Sets an attribute on the entry. If the entry already has an attribute
     * with the same name, the value is added to the list of values for the
     * attribute. If the entry does not already have the given attribute, it
     * is added to the entry with a list containing the given value.
     *
     * @param attribute
     *          Name of the attribute
     * @param value
     *          Value of the attribute
     */
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