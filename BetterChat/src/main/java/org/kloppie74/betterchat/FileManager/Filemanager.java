package org.kloppie74.betterchat.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;

public class Filemanager {
    private Filemanager() { }

    static Filemanager instance = new Filemanager();

    public static Filemanager getInstance() {
        return instance;
    }

    Plugin p;


    FileConfiguration Settings;
    File Settingsfile;
    File Settingstxt;

    FileConfiguration langSettings;
    File langSettingsfile;
    File langSettingstxt;

    public boolean setup(Plugin p) {

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        File LangFolder = new File(p.getDataFolder(), "Lang");
        if(!LangFolder.exists()) {
            LangFolder.mkdirs();
        }

        File PlayerDataFolder = new File(p.getDataFolder(), "PlayerData");
        if(!PlayerDataFolder.exists()) {
            PlayerDataFolder.mkdirs();
        }

        Settingsfile = new File(p.getDataFolder(), "Settings.yml");
        if(!Settingsfile.exists()){
            new FileCopy().copy(p.getResource("Settings.yml"), Settingsfile);
        }
        Settings = YamlConfiguration.loadConfiguration(Settingsfile);


        langSettingsfile = new File(p.getDataFolder(), "Lang/" + getSettings().getString("Language.File"));
        if(!langSettingsfile.exists()){
            new FileCopy().copy(p.getResource("Lang/lang_en_us.yml"), langSettingsfile);
        }
        langSettings = YamlConfiguration.loadConfiguration(langSettingsfile);

        return true;
    }




    public FileConfiguration getSettings() {
        return Settings;
    }


    public void saveSettings() {
        try {
            Settings.save(Settingsfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Settings.yml!");
        }
    }

    public FileConfiguration getlangSettings() {
        return langSettings;
    }

    public void savelangSettings() {
        try {
            langSettings.save(langSettingsfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save " + getSettings().getString("Language.File"));
        }
    }


    public void reloadFiles() throws IOException, InvalidConfigurationException {

        langSettings.load(langSettingsfile);
        Settings.load(Settingsfile);

    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }


}
