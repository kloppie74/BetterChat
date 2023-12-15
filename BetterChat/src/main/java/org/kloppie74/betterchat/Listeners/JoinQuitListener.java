package org.kloppie74.betterchat.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.FileManager.PlayerDataManager;
import org.kloppie74.betterchat.MSG;
import org.kloppie74.betterchat.Placeholder.Placeholders;

public class JoinQuitListener implements Listener {
    FileConfiguration Joinformat = Filemanager.getInstance().getSettings();
    Placeholders pch = new Placeholders();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        PlayerDataManager.create(event.getPlayer());

        Player p =event.getPlayer();

        if (Joinformat.getString("Join_format.Enable").equalsIgnoreCase("true")) {

            String joinText = Joinformat.getString("Join_format.Format");
            joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);

            event.setJoinMessage(pch.pch(joinText, event.getPlayer()));

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {

        if (Joinformat.getString("Quit_format.Enable").equalsIgnoreCase("true")) {

            String QuitText = Joinformat.getString("Quit_format.Format");
            QuitText = PlaceholderAPI.setPlaceholders(event.getPlayer(), QuitText);

            event.setQuitMessage(pch.pch(QuitText, event.getPlayer()));

        }
    }
}
