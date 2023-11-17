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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.function.Consumer;
import org.openide.util.Cancellable;

public class SearchContext implements Cancellable {
    private final IdentityHashMap<Consumer<SearchContext>,Object> cancelListener = new IdentityHashMap<>();
    private volatile boolean canceled;

    @Override
    public boolean cancel() {
        canceled = true;
        synchronized(cancelListener) {
            for(Consumer<SearchContext> listener: new ArrayList<>(cancelListener.keySet())) {
                listener.accept(this);
            }
        }
        return true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean registerCancelListener(Consumer<SearchContext> listener) {
        synchronized(cancelListener) {
            if(! cancelListener.containsKey(listener)) {
                cancelListener.put(listener, true);
                return true;
            } else {
                return false;
            }
        }
    }

    public void removeCancelListener(Consumer<SearchContext> listener) {
        synchronized (cancelListener) {
            cancelListener.remove(listener);
        }
    }
}
