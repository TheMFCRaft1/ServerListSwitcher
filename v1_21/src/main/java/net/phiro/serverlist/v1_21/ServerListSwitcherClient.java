package net.phiro.serverlist.v1_21;

import net.fabricmc.api.ClientModInitializer;
import net.phiro.serverlist.CategoryManager;
import net.phiro.serverlist.LanguageManager;

public class ServerListSwitcherClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize the category manager and language manager
        CategoryManager.getInstance();
        LanguageManager.getInstance();
    }
}
