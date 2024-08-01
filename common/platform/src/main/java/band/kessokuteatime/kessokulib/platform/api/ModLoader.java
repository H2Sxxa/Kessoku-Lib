package band.kessokuteatime.kessokulib.platform.api;

import band.kessokuteatime.kessokulib.platform.services.KessokuPlatformServices;

public interface ModLoader {
    static ModLoader getInstance() {
        return KessokuPlatformServices.getModLoader();
    }

    ModData getModData(String modid);
    boolean isFabric();
    boolean isNeoForge();
    Env getEnv();
}
