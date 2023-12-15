package org.kloppie74.betterchat.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class PlayerDataManager {

    static File playerfile;
    static FileConfiguration playerconfig;

    public static void create(Player p) {
        playerfile = new File("plugins/BetterChat/PlayerData/" + p.getUniqueId() + ".yml");
        if (!playerfile.exists()) {
            try {
                playerfile.createNewFile();

                YamlConfiguration yaml = new YamlConfiguration();

                yaml.set("Channel", "global");
                yaml.set("Messages", 0);

                try {
                    yaml.save("plugins/BetterChat/PlayerData/" + p.getUniqueId() + ".yml");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch(Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + playerfile.getName() + "!");
            }
        }
        playerconfig = YamlConfiguration.loadConfiguration(playerfile);
    }

    public static File getplayerfile() {
        return playerfile;
    }

    public static void load(Player p) {
        playerconfig = YamlConfiguration.loadConfiguration(playerfile);
    }

    public static FileConfiguration get() {
        return playerconfig;
    }

    public static void save() {
        try {
            playerconfig.save(playerfile);
        } catch(Exception e) {
            Bukkit.broadcast(ChatColor.RED + "Error saving " + playerfile.getName() + "!", "ChatColor.ErrorMsgs");
        }
    }

}
