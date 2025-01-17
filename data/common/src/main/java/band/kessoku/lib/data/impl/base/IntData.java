package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class IntData extends BaseData<Integer> implements NBTSerializable {
    private IntData(String id, int defaultValue) {
        super(id, defaultValue);
    }

    public static IntData create(String id, int defaultValue) {
        return new IntData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getInt(id()));
    }
}
