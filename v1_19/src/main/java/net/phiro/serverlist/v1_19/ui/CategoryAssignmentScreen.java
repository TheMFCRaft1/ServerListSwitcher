package net.phiro.serverlist.v1_19.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.phiro.serverlist.CategoryManager;

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
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y, 200, 20, Text.of(category + (active ? " [X]" : " [ ]")), (button) -> {
                manager.toggleServerCategory(serverId, category);
                button.setMessage(Text.of(category + (manager.isServerInCategory(serverId, category) ? " [X]" : " [ ]")));
            }));
            
            y += 25;
        }

        // New Category Field
        this.newCategoryField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, y + 10, 150, 20, Text.of("New Category"));
        this.addDrawableChild(this.newCategoryField);
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 60, y + 10, 40, 20, Text.of("+"), (button) -> {
            String name = newCategoryField.getText().trim();
            if (!name.isEmpty()) {
                manager.addCategory(name);
                this.init(this.client, this.width, this.height);
            }
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 30, 200, 20, Text.of("Done"), (button) -> {
            this.client.setScreen(parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
