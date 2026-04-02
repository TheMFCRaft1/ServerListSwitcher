package net.phiro.serverlist.v1_21.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;
import net.phiro.serverlist.LanguageManager;

import java.util.List;

public class CategoryAssignmentScreen extends Screen {
    private final ServerInfo server;
    private final Screen parent;
    private TextFieldWidget newCategoryField;

    public CategoryAssignmentScreen(ServerInfo server, Screen parent) {
        super(Text.of("Assign Categories"));
        this.server = server;
        this.parent = parent;
    }

    @Override
    protected void init() {
        LanguageManager lang = LanguageManager.getInstance();
        CategoryManager manager = CategoryManager.getInstance();
        String serverId = CategoryManager.getServerId(server.name, server.address);
        
        int y = 40;
        int centerX = this.width / 2;

        // Category checkboxes
        for (String category : manager.getCategories()) {
            if (category.equals("All")) continue;
            
            boolean active = manager.isServerInCategory(serverId, category);
            String displayText = category + (active ? " ✓" : " ☐");
            
            this.addDrawableChild(ButtonWidget.builder(Text.of(displayText), (button) -> {
                manager.toggleServerCategory(serverId, category);
                boolean newState = manager.isServerInCategory(serverId, category);
                button.setMessage(Text.of(category + (newState ? " ✓" : " ☐")));
            }).dimensions(centerX - 100, y, 200, 20).build());
            
            y += 25;
        }

        // Add new category section
        y += 10;
        this.addDrawableChild(ButtonWidget.builder(Text.of(lang.translate("category.new")), (button) -> {
            // Show/hide the new category input field
            newCategoryField.setVisible(!newCategoryField.isVisible());
            if (newCategoryField.isVisible()) {
                this.setFocused(newCategoryField);
            }
        }).dimensions(centerX - 100, y, 200, 20).build());

        // New category input field (initially hidden)
        this.newCategoryField = new TextFieldWidget(this.textRenderer, centerX - 100, y + 25, 150, 20, Text.of(lang.translate("category.name")));
        this.newCategoryField.setVisible(false);
        this.newCategoryField.setMaxLength(32);
        this.addDrawableChild(this.newCategoryField);

        // Add category button (initially hidden)
        ButtonWidget addCategoryButton = ButtonWidget.builder(Text.of(lang.translate("button.add")), (button) -> {
            String name = newCategoryField.getText().trim();
            if (!name.isEmpty()) {
                manager.addCategory(name);
                newCategoryField.setText("");
                newCategoryField.setVisible(false);
                this.clearAndInit(); // Refresh the screen
            }
        }).dimensions(centerX + 60, y + 25, 40, 20).build();
        addCategoryButton.visible = false;
        this.addDrawableChild(addCategoryButton);

        // Make the add button visible when the text field is visible
        this.newCategoryField.setChangedListener((text) -> {
            addCategoryButton.visible = newCategoryField.isVisible() && !text.trim().isEmpty();
        });

        // Bottom buttons
        this.addDrawableChild(ButtonWidget.builder(Text.of(lang.translate("button.done")), (button) -> {
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, this.height - 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        LanguageManager lang = LanguageManager.getInstance();
        String titleText = lang.translate("category.assign.for", server.name);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of(titleText), this.width / 2, 15, 0xFFFFFF);
        
        // Draw instructions
        String instruction = lang.translate("server.add.to.category");
        context.drawCenteredTextWithShadow(this.textRenderer, instruction, this.width / 2, this.height - 60, 0xAAAAAA);
        
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
