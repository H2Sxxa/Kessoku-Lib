package band.kessoku.lib.base;

import net.neoforged.fml.common.Mod;

@Mod(KessokuBase.MOD_ID)
public class KessokuBaseEntrypoint {
    public KessokuBaseEntrypoint() {
        ModUtils.getLogger().info(KessokuBase.MARKER, "KessokuLib-Base is loaded!");
    }
}