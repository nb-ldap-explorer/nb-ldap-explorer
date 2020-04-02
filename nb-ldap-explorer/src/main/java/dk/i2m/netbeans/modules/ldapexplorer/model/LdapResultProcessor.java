/*
 * Copyright 2020 Interactive Media Management.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.i2m.netbeans.modules.ldapexplorer.model;

import org.openide.util.Cancellable;

public abstract class LdapResultProcessor implements Cancellable {
    private volatile boolean canceled = false;

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public boolean cancel() {
        this.canceled = true;
        return true;
    }

    public abstract void addEntry(LdapEntry entry);

    public abstract void reset();
}
