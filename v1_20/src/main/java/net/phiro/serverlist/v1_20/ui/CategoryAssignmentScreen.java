package net.phiro.serverlist.v1_20.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;

import java.util.List;

public class CategoryAssignmentScreen extends Screen {
    private final ServerInfo server;
    private final Screen parent;
    private TextFieldWidget newCategoryField;

    public CategoryAssignmentScreen(ServerInfo server, Screen parent) {
        super(Text.of("Assign Categories for " + server.name));
        this.server = server;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = 40;
        CategoryManager manager = CategoryManager.getInstance();
        String serverId = CategoryManager.getServerId(server.name, server.address);

        for (String category : manager.getCategories()) {
            if (category.equals("All")) continue;
            
            boolean active = manager.isServerInCategory(serverId, category);
            this.addDrawableChild(ButtonWidget.builder(Text.of(category + (active ? " [X]" : " [ ]")), (button) -> {
                manager.toggleServerCategory(serverId, category);
                button.setMessage(Text.of(category + (manager.isServerInCategory(serverId, category) ? " [X]" : " [ ]")));
            }).dimensions(this.width / 2 - 100, y, 200, 20).build());
            
            y += 25;
        }

        // New Category Field
        this.newCategoryField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, y + 10, 150, 20, Text.of("New Category"));
        this.addDrawableChild(this.newCategoryField);
        this.addDrawableChild(ButtonWidget.builder(Text.of("+"), (button) -> {
            String name = newCategoryField.getText().trim();
            if (!name.isEmpty()) {
                manager.addCategory(name);
                this.init(this.client, this.width, this.height); // Refresh
            }
        }).dimensions(this.width / 2 + 60, y + 10, 40, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Done"), (button) -> {
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height - 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
