package net.phiro.serverlist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SwitcherConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("serverlist_switcher.json");
    
    private static SwitcherConfig instance;
    public boolean showTopBar = true;
    public boolean showHoverButton = true;
    public String lastSelectedCategory = "All";

    public static SwitcherConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static SwitcherConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                return GSON.fromJson(reader, SwitcherConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new SwitcherConfig();
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
