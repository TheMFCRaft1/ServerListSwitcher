package net.phiro.serverlist.v1_19.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends net.minecraft.client.gui.screen.Screen {
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        CategoryManager manager = CategoryManager.getInstance();
        List<String> categories = manager.getCategories();
        
        int x = 10;
        int y = 5;
        
        for (String category : categories) {
            int width = this.textRenderer.getWidth(category) + 10;
            // In 1.19, ButtonWidget constructor is different
            this.addDrawableChild(new ButtonWidget(x, y, width, 16, Text.of(category), (button) -> {
                manager.setActiveCategory(category);
                refreshServerList();
            }));
            x += width + 5;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Buttons are already rendered by super.render
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        CategoryManager manager = CategoryManager.getInstance();
        List<String> categories = manager.getCategories();
        int currentIndex = categories.indexOf(manager.getActiveCategory());

        if (keyCode == 262) { // Right Arrow
            int nextIndex = (currentIndex + 1) % categories.size();
            manager.setActiveCategory(categories.get(nextIndex));
            refreshServerList();
            cir.setReturnValue(true);
        } else if (keyCode == 263) { // Left Arrow
            int prevIndex = (currentIndex - 1 + categories.size()) % categories.size();
            manager.setActiveCategory(categories.get(prevIndex));
            refreshServerList();
            cir.setReturnValue(true);
        }
    }

    private void refreshServerList() {
        if (this.client != null && this.client.currentScreen instanceof MultiplayerScreen) {
             // In 1.19, the widget might be accessible via accessor or field
        }
    }
}
