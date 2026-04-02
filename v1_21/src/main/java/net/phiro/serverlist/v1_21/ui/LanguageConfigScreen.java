package net.phiro.serverlist.v1_21.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.phiro.serverlist.LanguageManager;

public class LanguageConfigScreen extends Screen {
    private final Screen parent;

    public LanguageConfigScreen(Screen parent) {
        super(Text.of("Language Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        LanguageManager lang = LanguageManager.getInstance();
        int y = 40;
        int centerX = this.width / 2;

        // Title
        this.addDrawableChild(ButtonWidget.builder(Text.of("Server List Switcher - Language"), (button) -> {
            // Title button, no action
        }).dimensions(centerX - 100, y, 200, 20).build());
        y += 30;

        // Language options
        String[] languages = lang.getAvailableLanguages();
        String[] languageNames = {"English", "Deutsch", "Français", "Español", "Italiano", "Português", "Русский", "中文", "日本語"};
        
        for (int i = 0; i < languages.length; i++) {
            String languageCode = languages[i];
            String languageName = languageNames[i];
            boolean isSelected = lang.getCurrentLanguage().equals(languageCode);
            
            String displayText = languageName + (isSelected ? " ✓" : "");
            
            this.addDrawableChild(ButtonWidget.builder(Text.of(displayText), (button) -> {
                lang.setLanguage(languageCode);
                // Refresh all screens
                this.client.setScreen(new LanguageConfigScreen(parent));
            }).dimensions(centerX - 100, y, 200, 20).build());
            
            y += 25;
        }

        // Back button
        y += 20;
        this.addDrawableChild(ButtonWidget.builder(Text.of(lang.translate("button.done")), (button) -> {
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC key
            this.client.setScreen(parent);
            return true;
        }
        return false; // Don't call super.keyPressed as signature changed
    }
}
