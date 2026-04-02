package net.phiro.serverlist.v1_18.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.phiro.serverlist.CategoryManager;
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
        String active = manager.getActiveCategory();
        
        if (active.equals("All")) return servers;

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
