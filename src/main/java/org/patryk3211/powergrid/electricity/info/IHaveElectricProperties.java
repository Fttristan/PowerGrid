/*
 * Copyright 2025 patryk3211
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patryk3211.powergrid.electricity.info;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Adds tooltip information for an item or block that exposes electric properties.
 * <p>
 * Implementations are used by the Forge item tooltip integration to surface the same values that the matching block
 * or item uses in-world.
 */
public interface IHaveElectricProperties {
    /**
     * Append electrical property details to the tooltip.
     *
     * @param stack   The stack being inspected.
     * @param player  The player viewing the stack, or {@code null} when unavailable.
     * @param tooltip The tooltip lines to append to.
     */
    void appendProperties(ItemStack stack, Player player, List<Component> tooltip);

    /**
     * Whether the tooltip should always be shown, even when the item is not holding a modifier key.
     *
     * @return {@code true} if the tooltip should always be displayed.
     */
    default boolean alwaysDisplay() {
        return false;
    }
}
