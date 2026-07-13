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
package org.patryk3211.powergrid.electricity.gauge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared behaviour for electric gauge blocks.
 * <p>
 * Gauge blocks expose a meter face that should only be rendered head-on from the correct viewing angle.
 */
public interface IGaugeBlock {
    /**
     * Determine whether the gauge head should be rendered for the given face.
     *
     * @param world The world the block is in.
     * @param pos   The block position.
     * @param state The block state.
     * @param dir   The side being queried.
     * @return {@code true} when the gauge head should be visible from this direction.
     */
    boolean shouldRenderHeadOnFace(Level world, BlockPos pos, BlockState state, Direction dir);
}
