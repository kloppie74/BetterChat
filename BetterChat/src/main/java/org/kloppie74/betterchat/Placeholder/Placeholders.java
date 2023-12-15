package org.kloppie74.betterchat.Placeholder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.kloppie74.betterchat.Commands.PrivateMSG;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

import java.text.DecimalFormat;

public class Placeholders implements Listener {

    public String pch(String string, Player player) {

        FileConfiguration autobroadcast = Filemanager.getInstance().getSettings();
        DecimalFormat df = new DecimalFormat("#,###");


        string = ChatColor.translateAlternateColorCodes('&', string);
        string = string.replace("{username}", player.getName());
        string = string.replace("{player}", player.getName());
        string = string.replace("{online}", df.format(Bukkit.getServer().getOnlinePlayers().size()));



        return string;

    }
}