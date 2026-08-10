package net.mehvahdjukaar.heartstone.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.mehvahdjukaar.heartstone.Heartstone;
import net.mehvahdjukaar.moonlight.core.client.config.MoonlightConfigSelectScreen;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MoonlightConfigSelectScreen.create(Heartstone.MOD_ID, parent, null);
    }
}
