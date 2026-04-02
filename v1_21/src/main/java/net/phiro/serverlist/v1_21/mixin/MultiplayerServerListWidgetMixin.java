package net.phiro.serverlist.v1_21.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.phiro.serverlist.CategoryManager;
import net.phiro.serverlist.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiplayerServerListWidget.class)
public class MultiplayerServerListWidgetMixin {
    
    @ModifyVariable(method = "setServers", at = @At("HEAD"), argsOnly = true)
    private List<ServerInfo> filterServers(List<ServerInfo> servers) {
        CategoryManager manager = CategoryManager.getInstance();
        LanguageManager lang = LanguageManager.getInstance();
        String active = manager.getActiveCategory();
        
        // Handle "All" category - show all servers
        if (active.equals("All") || active.equals(lang.translate("category.all"))) {
            return servers;
        }

        // Filter by active category
        List<ServerInfo> filtered = new ArrayList<>();
        for (ServerInfo server : servers) {
            String id = CategoryManager.getServerId(server.name, server.address);
            if (manager.isServerInCategory(id, active)) {
                filtered.add(server);
            }
        }
        
        return filtered;
    }
}
