package net.phiro.serverlist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ServerInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("server_categories.json");
    
    private static CategoryManager instance;
    private final List<String> categories = new ArrayList<>();
    private final Map<String, List<String>> serverTags = new HashMap<>(); // Key: Name|Address, Value: List of Categories
    private String activeCategory = "All";

    private CategoryManager() {
        load();
        if (categories.isEmpty()) {
            categories.add("All");
        }
    }

    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addCategory(String name) {
        if (!categories.contains(name)) {
            categories.add(name);
            save();
        }
    }

    public void removeCategory(String name) {
        if (!name.equals("All")) {
            categories.remove(name);
            // Remove from all servers
            for (List<String> tags : serverTags.values()) {
                tags.remove(name);
            }
            if (activeCategory.equals(name)) {
                activeCategory = "All";
            }
            save();
        }
    }

    public String getActiveCategory() {
        return activeCategory;
    }

    public void setActiveCategory(String category) {
        this.activeCategory = category;
    }

    public List<String> getServerCategories(String serverId) {
        return serverTags.getOrDefault(serverId, new ArrayList<>());
    }

    public void toggleServerCategory(String serverId, String category) {
        List<String> tags = serverTags.computeIfAbsent(serverId, k -> new ArrayList<>());
        if (tags.contains(category)) {
            tags.remove(category);
        } else {
            tags.add(category);
        }
        save();
    }

    public boolean isServerInCategory(String serverId, String category) {
        if (category.equals("All")) return true;
        List<String> tags = serverTags.get(serverId);
        return tags != null && tags.contains(category);
    }

    public List<String> getFilteredServers(List<ServerInfo> allServers) {
        if (activeCategory.equals("All")) {
            return allServers.stream()
                    .map(server -> CategoryManager.getServerId(server.name, server.address))
                    .toList();
        }
        
        return allServers.stream()
                .filter(server -> isServerInCategory(CategoryManager.getServerId(server.name, server.address), activeCategory))
                .map(server -> CategoryManager.getServerId(server.name, server.address))
                .toList();
    }

    private void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                Data data = GSON.fromJson(reader, Data.class);
                if (data != null) {
                    categories.clear();
                    categories.addAll(data.categories);
                    if (!categories.contains("All")) categories.add(0, "All");
                    serverTags.clear();
                    serverTags.putAll(data.serverTags);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(new Data(categories, serverTags), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Data {
        List<String> categories;
        Map<String, List<String>> serverTags;

        Data(List<String> categories, Map<String, List<String>> serverTags) {
            this.categories = categories;
            this.serverTags = serverTags;
        }
    }

    public static String getServerId(String name, String address) {
        return name + "|" + address;
    }
}
