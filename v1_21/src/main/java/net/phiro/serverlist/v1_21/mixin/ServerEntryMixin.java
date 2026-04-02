package net.phiro.serverlist.v1_21.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;
import net.phiro.serverlist.LanguageManager;
import net.phiro.serverlist.v1_21.ui.CategoryAssignmentScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class ServerEntryMixin {
    @Shadow @Final private ServerInfo server;
    @Shadow @Final private MinecraftClient client;

    private int lastRenderX;
    private int lastRenderY;
    private int lastRenderWidth;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        // Store render coordinates for click detection
        lastRenderX = x;
        lastRenderY = y;
        lastRenderWidth = entryWidth;
        
        if (hovered) {
            LanguageManager lang = LanguageManager.getInstance();
            
            // Draw a "+" icon in top right of entry
            int buttonX = x + entryWidth - 20;
            int buttonY = y + 5;
            int buttonSize = 15;
            
            // Draw button background
            context.fill(buttonX, buttonY, buttonX + buttonSize, buttonY + buttonSize, 0x88000000);
            context.fill(buttonX, buttonY, buttonX + buttonSize, buttonY + 1, 0xFFFFFFFF);
            context.fill(buttonX, buttonY, buttonX + 1, buttonY + buttonSize, 0xFFFFFFFF);
            context.fill(buttonX + buttonSize - 1, buttonY, buttonX + buttonSize, buttonY + buttonSize, 0xFFFFFFFF);
            context.fill(buttonX, buttonY + buttonSize - 1, buttonX + buttonSize, buttonY + buttonSize, 0xFFFFFFFF);
            
            // Draw plus sign
            context.fill(buttonX + 3, buttonY + 7, buttonX + 12, buttonY + 8, 0xFFFFFF);
            context.fill(buttonX + 7, buttonY + 3, buttonX + 8, buttonY + 12, 0xFFFFFF);
            
            // Store button position for tooltip
            if (mouseX >= buttonX && mouseX <= buttonX + buttonSize && 
                mouseY >= buttonY && mouseY <= buttonY + buttonSize) {
                // Tooltip will be handled in the parent screen
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 0) { // Left click
            int buttonX = lastRenderX + lastRenderWidth - 20;
            int buttonY = lastRenderY + 5;
            int buttonSize = 15;
            
            // Check if click is on the "+" button
            if (mouseX >= buttonX && mouseX <= buttonX + buttonSize && 
                mouseY >= buttonY && mouseY <= buttonY + buttonSize) {
                
                // Open category assignment screen
                if (client != null && client.currentScreen != null) {
                    client.setScreen(new CategoryAssignmentScreen(server, client.currentScreen));
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
