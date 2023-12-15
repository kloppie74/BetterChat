package org.kloppie74.betterchat.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.kloppie74.betterchat.BetterChat;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

public class MotdListener implements Listener {

    FileConfiguration motdformat = Filemanager.getInstance().getSettings();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (motdformat.getString("Motd_format.Enable") == "true") {

            int seconds = motdformat.getInt("Motd_format.Interval");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BetterChat.getBetterChat(), () -> {
                for(String MotdText : motdformat.getStringList("Motd_format.Motd")) {
                    MotdText = PlaceholderAPI.setPlaceholders(event.getPlayer(), MotdText);

                    player.sendMessage(MSG.chatColors(MotdText));
                }
            }, (seconds * 20L));


        } else if (motdformat.getString("Motd_disabled_log") == "true"){

            String ConsoleLogMotdText = Filemanager.getInstance().getSettings().getString("Logs.Motd_disabled_log");
            ConsoleLogMotdText = PlaceholderAPI.setPlaceholders(event.getPlayer(), ConsoleLogMotdText);
            Bukkit.getConsoleSender().sendMessage(ConsoleLogMotdText);
        }

    }
}
