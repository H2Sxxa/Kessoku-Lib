<<<<<<< Updated upstream
=======
<<<<<<< HEAD
package band.kessoku.lib.events.entity.api;

import band.kessoku.lib.event.api.Event;
=======
>>>>>>> Stashed changes
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
package band.kessoku.lib.events.entity.api;

import band.kessoku.lib.event.api.Event;

<<<<<<< Updated upstream
=======
>>>>>>> 4e466673b651ef2c6248fa5a9e055d5952613930
>>>>>>> Stashed changes
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerPlayerEvent {
    /**
     * An event that is called when the data from an old player is copied to a new player.
     *
     * <p>This event is typically called before a player is completely respawned.
     * Mods may use this event to copy old player data to a new player.
     */
    public static final Event<CopyFrom> COPY_FROM = Event.of(callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (CopyFrom callback : callbacks) {
            callback.copyFromPlayer(oldPlayer, newPlayer, alive);
        }
    });

    /**
     * An event that is called after a player has been respawned.
     *
     * <p>Mods may use this event for reference clean up on the old player.
     */
    public static final Event<ServerPlayerEvent.AfterRespawn> AFTER_RESPAWN = Event.of(callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (AfterRespawn callback : callbacks) {
            callback.afterRespawn(oldPlayer, newPlayer, alive);
        }
    });

    @FunctionalInterface
    public interface CopyFrom {
        /**
         * Called when player data is copied to a new player.
         *
         * @param oldPlayer the old player
         * @param newPlayer the new player
         * @param alive whether the old player is still alive
         */
        void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface AfterRespawn {
        /**
         * Called after player a has been respawned.
         *
         * @param oldPlayer the old player
         * @param newPlayer the new player
         * @param alive whether the old player is still alive
         */
        void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive);
    }

    private ServerPlayerEvent() {
    }
}
