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
package org.patryk3211.powergrid.compat.computercraft;

import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jspecify.annotations.Nullable;
import org.patryk3211.powergrid.PowerGrid;
import org.patryk3211.powergrid.electricity.electricswitch.HvBreakerBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.CurrentGaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.GaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.PowerGaugeBlockEntity;
import org.patryk3211.powergrid.electricity.gauge.VoltageGaugeBlockEntity;

import java.util.Set;

/**
 * Forge-only CC:Tweaked integration for PowerGrid's gauge blocks and HV breaker.
 */
public final class PowerGridComputerCraftCompat {
    private PowerGridComputerCraftCompat() {
    }

    public static void init() {
        ForgeComputerCraftAPI.registerPeripheralProvider(new PowerGridPeripheralProvider());
    }

    private static final class PowerGridPeripheralProvider implements IPeripheralProvider {
        @Override
        public LazyOptional<IPeripheral> getPeripheral(Level world, BlockPos pos, Direction side) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity == null) return LazyOptional.empty();

            if (blockEntity instanceof VoltageGaugeBlockEntity voltageGauge) {
                return LazyOptional.of(() -> new VoltageGaugePeripheral(voltageGauge));
            }
            if (blockEntity instanceof CurrentGaugeBlockEntity currentGauge) {
                return LazyOptional.of(() -> new CurrentGaugePeripheral(currentGauge));
            }
            if (blockEntity instanceof PowerGaugeBlockEntity powerGauge) {
                return LazyOptional.of(() -> new PowerGaugePeripheral(powerGauge));
            }
            if (blockEntity instanceof HvBreakerBlockEntity breaker) {
                return LazyOptional.of(() -> new HvBreakerPeripheral(breaker));
            }

            return LazyOptional.empty();
        }
    }

    private abstract static class BasePeripheral<T extends BlockEntity> implements IPeripheral {
        private final T target;

        private BasePeripheral(T target) {
            this.target = target;
        }

        @Override
        public Object getTarget() {
            return target;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return other != null && other.getClass() == getClass() && other.getTarget() == target;
        }

        protected final T target() {
            return target;
        }
    }

    private abstract static class GaugePeripheral<T extends GaugeBlockEntity> extends BasePeripheral<T> {
        private final String type;
        private final Set<String> additionalTypes;

        private GaugePeripheral(T target, String type, String... additionalTypes) {
            super(target);
            this.type = type;
            this.additionalTypes = Set.of(additionalTypes);
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public Set<String> getAdditionalTypes() {
            return additionalTypes;
        }

        @LuaFunction(mainThread = true)
        public final float getValue() {
            return target().getValue();
        }

        @LuaFunction(mainThread = true)
        public final float getMaxValue() {
            return target().getMaxValue();
        }

        @LuaFunction(mainThread = true)
        public final float getProgress() {
            return target().getProgress();
        }

        @LuaFunction(mainThread = true)
        public final String getUnit() {
            return target().getUnit().component().getString();
        }
    }

    private static final class VoltageGaugePeripheral extends GaugePeripheral<VoltageGaugeBlockEntity> {
        private VoltageGaugePeripheral(VoltageGaugeBlockEntity target) {
            super(target, PowerGrid.MOD_ID + ":voltage_gauge", "gauge", "meter", "voltage");
        }
    }

    private static final class CurrentGaugePeripheral extends GaugePeripheral<CurrentGaugeBlockEntity> {
        private CurrentGaugePeripheral(CurrentGaugeBlockEntity target) {
            super(target, PowerGrid.MOD_ID + ":current_gauge", "gauge", "meter", "current");
        }
    }

    private static final class PowerGaugePeripheral extends GaugePeripheral<PowerGaugeBlockEntity> {
        private PowerGaugePeripheral(PowerGaugeBlockEntity target) {
            super(target, PowerGrid.MOD_ID + ":power_gauge", "gauge", "meter", "power");
        }
    }

    private static final class HvBreakerPeripheral extends BasePeripheral<HvBreakerBlockEntity> {
        private HvBreakerPeripheral(HvBreakerBlockEntity target) {
            super(target);
        }

        @Override
        public String getType() {
            return PowerGrid.MOD_ID + ":hv_breaker";
        }

        @Override
        public Set<String> getAdditionalTypes() {
            return Set.of("breaker", "switch", "hv");
        }

        @LuaFunction(mainThread = true)
        public final boolean isOpen() {
            return target().isOpen();
        }

        @LuaFunction(mainThread = true)
        public final boolean isClosed() {
            return target().isClosed();
        }

        @LuaFunction(mainThread = true)
        public final float getCharge() {
            return target().getCharge();
        }

        @LuaFunction(mainThread = true)
        public final void open() {
            target().open();
        }

        @LuaFunction(mainThread = true)
        public final void close() {
            target().close();
        }

        @LuaFunction(mainThread = true)
        public final void toggle() {
            target().toggle();
        }
    }
}