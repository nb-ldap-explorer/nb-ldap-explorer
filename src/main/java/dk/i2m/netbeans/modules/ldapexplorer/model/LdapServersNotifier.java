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

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Notifier for changes to the collection of {@link LdapServer}s.
 *
 * @author Allan Lykke Christensen
 */
public class LdapServersNotifier {

    private static Set<ChangeListener> listeners = new HashSet<ChangeListener>();

    /**
     * Adds a {@link ChangeListener} to the notifier.
     *
     * @param listener
     *          {@link ChangeListener} to add
     */
    public static void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link ChangeListener} from the notifier.
     *
     * @param listener
     *          {@link ChangeListener} to remove
     */
    public static void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the subscribed {@link ChangeListener}s that a change has
     * occured to the collection of {@link MailServer}s.
     */
    public static void changed() {
        ChangeEvent ev = new ChangeEvent(LdapServersNotifier.class);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(ev);
        }
    }
}
