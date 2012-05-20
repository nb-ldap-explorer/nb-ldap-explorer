/*
 * Copyright 2012 Interactive Media Management.
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
package dk.i2m.netbeans.modules.ldapexplorer.ui;

import org.openide.util.RequestProcessor;

public class UIHelper {

    private static final RequestProcessor RP = new RequestProcessor(
            UIHelper.class.getName(), 2);
    
    public static RequestProcessor getRequestProcessor() {
        return RP;
    }
}
