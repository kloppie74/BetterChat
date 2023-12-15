package org.kloppie74.betterchat.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private ChatManager() { }

    static ChatManager instance = new ChatManager();

    public static ChatManager getInstance() {
        return instance;
    }

    Plugin p;

    FileConfiguration Chatchannels_settings;
    File Chatchannels_settingsfile;
    File Chatchannels_settingstxt;

    public boolean setup(Plugin p) {

        Chatchannels_settingsfile = new File(p.getDataFolder(), "Chatchannels.yml");
        if(!Chatchannels_settingsfile.exists()){
            new FileCopy().copy(p.getResource("Chatchannels.yml"), Chatchannels_settingsfile);
        }
        Chatchannels_settings = new YamlConfiguration().loadConfiguration(Chatchannels_settingsfile);

        if(Filemanager.getInstance().getSettings().getString("Updated_File").equalsIgnoreCase("true")) {
            if(Chatchannels_settingsfile.exists()){
                Chatchannels_settingstxt = new File(p.getDataFolder(), "Chatchannels.txt");
                new FileCopy().copy(p.getResource("Chatchannels.yml"), Chatchannels_settingstxt);
            }

        }

        return true;
    }

    public boolean update(Plugin p) {

        Chatchannels_settings.options().copyDefaults(true);
        saveChatchannels();
        return true;
    }

    public FileConfiguration getChatchannels() {
        return Chatchannels_settings;
    }

    public void reloadChatManager() throws IOException, InvalidConfigurationException {

        Chatchannels_settings.load(Chatchannels_settingsfile);


    }

    public void saveChatchannels(){
        try {
            Chatchannels_settings.save(Chatchannels_settingsfile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
