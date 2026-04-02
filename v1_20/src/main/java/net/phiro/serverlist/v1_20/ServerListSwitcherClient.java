package net.phiro.serverlist.v1_20;

import net.fabricmc.api.ClientModInitializer;
import net.phiro.serverlist.CategoryManager;

public class ServerListSwitcherClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CategoryManager.getInstance();
    }
}
