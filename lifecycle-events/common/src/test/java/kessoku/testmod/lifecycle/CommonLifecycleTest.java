/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.LifecycleEvent;

public class CommonLifecycleTest implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        LifecycleEvent.TAG_LOADED.register(((registries, client) -> {
            KessokuTestLifecycle.LOGGER.info("Tags (re)loaded on {} {}", client ? "client" : "server", Thread.currentThread());
        }));
    }
}
