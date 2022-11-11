package co.teamloops.perks;

import co.teamloops.commons.chat.MessageCache;
import co.teamloops.commons.config.ConfigManager;
import co.teamloops.perks.commands.PerkCommand;
import co.teamloops.perks.listeners.JoinListener;
import co.teamloops.perks.listeners.PlayerListener;
import co.teamloops.perks.perk.Perk;
import co.teamloops.perks.perk.menu.PerksMenu;
import co.teamloops.perks.perk.registry.PerksRegistry;
import co.teamloops.perks.player.PerkPlayer;
import co.teamloops.perks.player.registry.PlayerRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;


@Getter
public class LoopPerks extends JavaPlugin {

    public LoopPerks plugin;
    public PerksRegistry perksRegistry;
    public PlayerRegistry playerRegistry;
    public PerksMenu perksMenu;
    private MessageCache messageCache;
    private ConfigManager<LoopPerks> fileManager;

    private FileConfiguration settingsConfig;

    public void onEnable() {

        this.init();
        this.loadFiles();
        this.loadData();
        this.loadMessages();
        this.loadPerks();
        this.loadListeners();
        this.loadCommands();
        this.perksMenu = new PerksMenu(this);
    }

    public void onDisable() {
        this.saveData();
    }

    private void loadFiles() {
        this.fileManager = new ConfigManager<>(this);

        this.fileManager.loadConfiguration("settings");
        this.settingsConfig = this.fileManager.getConfig("settings");
    }

    private void init() {
        this.plugin = this;
        this.perksRegistry = new PerksRegistry();
        this.playerRegistry = new PlayerRegistry();
    }


    private void loadPerks() {
        for(final String key : this.settingsConfig.getConfigurationSection("Perks").getKeys(false)) {
            this.perksRegistry.getRegistry().put(key, new Perk(key, settingsConfig));
        }
    }


    private void loadMessages() {
        this.messageCache = new MessageCache(settingsConfig);

        for (final String key : this.settingsConfig.getConfigurationSection("Messages").getKeys(false)) {
            this.messageCache.loadMessage("Messages." + key);
        }

    }

    private void loadListeners() {
        new PlayerListener(this);
        new JoinListener(this);
    }

    private void loadCommands() {
        new PerkCommand(this).register();
    }


    @SneakyThrows
    private void saveData() {
        final File file = new File(getDataFolder(), "data.json");

        if (!file.exists()) {
            file.getParentFile().mkdir();
            saveResource("data.json", false);
        }

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (final Writer writer = new FileWriter(file)) {
            gson.toJson(this.playerRegistry.getRegistry().values(), writer);
            writer.flush();
        }
    }

    @SneakyThrows
    private void loadData() {
        final File file = new File(getDataFolder(), "data.json");

        if (!file.exists()) {
            file.getParentFile().mkdir();
            saveResource("data.json", false);
            return;
        }

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final PerkPlayer[] profiles = gson.fromJson(new FileReader(file), PerkPlayer[].class);

        for (final PerkPlayer profile : profiles) {
            this.playerRegistry.getRegistry().put(profile.getUuid(), profile);
        }
    }
}
