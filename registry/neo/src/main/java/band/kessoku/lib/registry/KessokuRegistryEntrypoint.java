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
package band.kessoku.lib.registry;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.event.util.NeoEventUtils;
import band.kessoku.lib.registry.api.FuelRegistry;
import band.kessoku.lib.registry.impl.RegistryImpl;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(KessokuRegistry.MOD_ID)
public class KessokuRegistryEntrypoint {
    public KessokuRegistryEntrypoint(IEventBus modEventBus) {
        ModUtils.getLogger().info(KessokuRegistry.MARKER, "KessokuLib-Registry is loaded!");
        NeoEventUtils.registerEvent(modEventBus, RegisterEvent.class, RegistryImpl::onRegister);
        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, FurnaceFuelBurnTimeEvent.class, event -> {
            ItemStack stack = event.getItemStack();
            RecipeType<?> recipeType = event.getRecipeType();
            Integer burnTime;
            if (recipeType != null) burnTime = FuelRegistry.of(recipeType).get(stack);
            else burnTime = FuelRegistry.of(RecipeType.SMELTING).get(stack);
            if (burnTime != null) event.setBurnTime(burnTime);
        });
    }
}
