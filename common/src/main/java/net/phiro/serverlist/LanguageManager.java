package net.phiro.serverlist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageManager {
    private static final Gson GSON = new Gson();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("serverlist_language.json");
    
    private static LanguageManager instance;
    private Map<String, String> translations = new HashMap<>();
    private String currentLanguage = "en";

    private LanguageManager() {
        loadLanguageSetting();
        loadTranslations();
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String translate(String key) {
        return translations.getOrDefault(key, key);
    }

    public String translate(String key, Object... args) {
        String text = translate(key);
        for (int i = 0; i < args.length; i++) {
            text = text.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return text;
    }

    public void setLanguage(String language) {
        this.currentLanguage = language;
        saveLanguageSetting();
        loadTranslations();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String[] getAvailableLanguages() {
        return new String[]{"en", "de", "fr", "es", "it", "pt", "ru", "zh", "ja"};
    }

    private void loadTranslations() {
        translations.clear();
        
        // Try to load from file first
        Path langFile = FabricLoader.getInstance().getConfigDir().resolve("serverlist_lang_" + currentLanguage + ".json");
        if (Files.exists(langFile)) {
            try (Reader reader = Files.newBufferedReader(langFile)) {
                Map<String, String> fileTranslations = GSON.fromJson(reader, Map.class);
                if (fileTranslations != null) {
                    translations.putAll(fileTranslations);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Load built-in translations
        String resourcePath = "/lang/serverlist_" + currentLanguage + ".json";
        try (InputStream stream = LanguageManager.class.getResourceAsStream(resourcePath)) {
            if (stream != null) {
                try (Reader reader = new InputStreamReader(stream)) {
                    Map<String, String> builtinTranslations = GSON.fromJson(reader, Map.class);
                    if (builtinTranslations != null) {
                        translations.putAll(builtinTranslations);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to English if translation file doesn't exist
            if (!currentLanguage.equals("en")) {
                currentLanguage = "en";
                loadTranslations();
            }
        }
    }

    private void loadLanguageSetting() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                Map<String, String> config = GSON.fromJson(reader, Map.class);
                if (config != null && config.containsKey("language")) {
                    currentLanguage = config.get("language");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Detect system language
            String systemLang = Locale.getDefault().getLanguage();
            if (isLanguageSupported(systemLang)) {
                currentLanguage = systemLang;
            }
        }
    }

    private void saveLanguageSetting() {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("language", currentLanguage);
            Files.writeString(CONFIG_PATH, GSON.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLanguageSupported(String lang) {
        for (String supported : getAvailableLanguages()) {
            if (supported.equals(lang)) return true;
        }
        return false;
    }
}
