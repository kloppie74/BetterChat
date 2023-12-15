package org.kloppie74.betterchat.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kloppie74.betterchat.BetterChat;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;


public class FirstJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (Filemanager.getInstance().getSettings().getString("First_Join_Event.Enable").equalsIgnoreCase("true")) {
            if (!p.hasPlayedBefore()) {

                for (String CommandsString : Filemanager.getInstance().getSettings().getStringList("First_Join_Event.Commands")) {

                        String JoinCommands = CommandsString;
                        JoinCommands = PlaceholderAPI.setPlaceholders(p, JoinCommands);

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), JoinCommands);

                }
            }
        }

    }
}