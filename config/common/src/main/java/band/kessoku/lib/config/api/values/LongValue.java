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
package band.kessoku.lib.config.api.values;

import java.util.function.Supplier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class LongValue extends DefaultConfigValue<Long> {
    private LongValue(Supplier<Long> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.LONG;
    }

    @Contract("_ -> new")
    public static @NotNull LongValue of(long l) {
        return new LongValue(() -> l);
    }

    @Contract("_ -> new")
    public static @NotNull LongValue of(Supplier<Long> longSupplier) {
        return new LongValue(longSupplier);
    }
}
