package net.phiro.serverlist.v1_21.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;
import net.phiro.serverlist.v1_21.ui.CategoryAssignmentScreen;
import net.phiro.serverlist.v1_21.ui.LanguageConfigScreen;
import net.phiro.serverlist.LanguageManager;
import org.spongepowered.asm.mixin.Final;
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

    private TextFieldWidget newCategoryField;
    private ButtonWidget addButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;
    private ButtonWidget languageButton;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        CategoryManager manager = CategoryManager.getInstance();
        LanguageManager lang = LanguageManager.getInstance();
        
        // Add category button (top right)
        this.addButton = ButtonWidget.builder(Text.of("+"), (button) -> {
            showAddCategoryDialog();
        }).dimensions(this.width - 60, 5, 25, 20).build();
        this.addDrawableChild(addButton);
        
        // Language button (top right)
        this.languageButton = ButtonWidget.builder(Text.of("🌐"), (button) -> {
            this.client.setScreen(new LanguageConfigScreen(this));
        }).dimensions(this.width - 30, 5, 25, 20).build();
        this.addDrawableChild(languageButton);
        
        // Navigation arrows
        this.prevButton = ButtonWidget.builder(Text.of("◄"), (button) -> {
            navigatePrevious();
        }).dimensions(5, 5, 25, 20).build();
        this.addDrawableChild(prevButton);
        
        this.nextButton = ButtonWidget.builder(Text.of("►"), (button) -> {
            navigateNext();
        }).dimensions(35, 5, 25, 20).build();
        this.addDrawableChild(nextButton);
        
        updateCategoryDisplay();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        CategoryManager manager = CategoryManager.getInstance();
        LanguageManager lang = LanguageManager.getInstance();
        
        // Draw current category in center
        String categoryText = manager.getActiveCategory();
        if (categoryText.equals("All")) {
            categoryText = lang.translate("category.all");
        }
        int textWidth = this.textRenderer.getWidth(categoryText);
        context.drawCenteredTextWithShadow(this.textRenderer, categoryText, this.width / 2, 10, 0xFFFFFF);
        
        // Draw tooltips
        if (mouseX >= this.width - 60 && mouseX <= this.width - 35 && mouseY >= 5 && mouseY <= 25) {
            context.drawTooltip(this.textRenderer, Text.of(lang.translate("category.add")), mouseX, mouseY);
        }
        
        if (mouseX >= this.width - 30 && mouseX <= this.width - 5 && mouseY >= 5 && mouseY <= 25) {
            context.drawTooltip(this.textRenderer, Text.of("Language"), mouseX, mouseY);
        }
        
        if (prevButton.visible && mouseX >= 5 && mouseX <= 30 && mouseY >= 5 && mouseY <= 25) {
            context.drawTooltip(this.textRenderer, Text.of(lang.translate("navigation.previous")), mouseX, mouseY);
        }
        
        if (nextButton.visible && mouseX >= 35 && mouseX <= 60 && mouseY >= 5 && mouseY <= 25) {
            context.drawTooltip(this.textRenderer, Text.of(lang.translate("navigation.next")), mouseX, mouseY);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == 262) { // Right Arrow
            navigateNext();
            cir.setReturnValue(true);
        } else if (keyCode == 263) { // Left Arrow
            navigatePrevious();
            cir.setReturnValue(true);
        }
    }

    private void navigatePrevious() {
        CategoryManager manager = CategoryManager.getInstance();
        List<String> categories = manager.getCategories();
        int currentIndex = categories.indexOf(manager.getActiveCategory());
        int prevIndex = (currentIndex - 1 + categories.size()) % categories.size();
        manager.setActiveCategory(categories.get(prevIndex));
        updateCategoryDisplay();
        refreshServerList();
    }

    private void navigateNext() {
        CategoryManager manager = CategoryManager.getInstance();
        List<String> categories = manager.getCategories();
        int currentIndex = categories.indexOf(manager.getActiveCategory());
        int nextIndex = (currentIndex + 1) % categories.size();
        manager.setActiveCategory(categories.get(nextIndex));
        updateCategoryDisplay();
        refreshServerList();
    }

    private void updateCategoryDisplay() {
        CategoryManager manager = CategoryManager.getInstance();
        LanguageManager lang = LanguageManager.getInstance();
        
        // Update button visibility based on category count
        boolean hasMultipleCategories = manager.getCategories().size() > 1;
        prevButton.visible = hasMultipleCategories;
        nextButton.visible = hasMultipleCategories;
    }

    private void showAddCategoryDialog() {
        // Simple input dialog for new category
        LanguageManager lang = LanguageManager.getInstance();
        
        // Create a simple text input overlay
        this.newCategoryField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Text.of(lang.translate("category.name")));
        this.newCategoryField.setMaxLength(32);
        this.setFocused(this.newCategoryField);
        
        // Add temporary buttons for the dialog
        ButtonWidget confirmButton = ButtonWidget.builder(Text.of(lang.translate("button.add")), (button) -> {
            String name = newCategoryField.getText().trim();
            if (!name.isEmpty()) {
                CategoryManager.getInstance().addCategory(name);
                // Remove dialog elements
                this.children().remove(newCategoryField);
                this.children().remove(button);
                this.children().remove(getDialogCancelButton());
                updateCategoryDisplay();
                refreshServerList();
            }
        }).dimensions(this.width / 2 - 90, this.height / 2 + 20, 80, 20).build();
        
        ButtonWidget cancelButton = ButtonWidget.builder(Text.of(lang.translate("button.cancel")), (button) -> {
            // Remove dialog elements
            this.children().remove(newCategoryField);
            this.children().remove(confirmButton);
            this.children().remove(button);
        }).dimensions(this.width / 2 + 10, this.height / 2 + 20, 80, 20).build();
        
        this.addDrawableChild(newCategoryField);
        this.addDrawableChild(confirmButton);
        this.addDrawableChild(cancelButton);
    }

    private ButtonWidget getDialogCancelButton() {
        // Find and return the cancel button from the dialog
        return this.children().stream()
                .filter(child -> child instanceof ButtonWidget && 
                              ((ButtonWidget) child).getMessage().getString().equals(LanguageManager.getInstance().translate("button.cancel")))
                .map(child -> (ButtonWidget) child)
                .findFirst()
                .orElse(null);
    }

    private void refreshServerList() {
        // Force refresh by clearing and reinitializing the screen
        if (this.client != null && this.client.currentScreen instanceof MultiplayerScreen) {
            // This will trigger a reload of the server list widget
            this.clearAndInit();
        }
    }
}
