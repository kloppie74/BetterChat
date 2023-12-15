package org.kloppie74.betterchat.CommandManagers.Subcommands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.kloppie74.betterchat.CommandManagers.SubCommand;
import org.kloppie74.betterchat.FileManager.ChatManager;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

import java.io.IOException;

import static org.bukkit.Bukkit.getPlayer;

public class ReloadFiles extends SubCommand {


    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin files!";
    }

    @Override
    public String getSyntax() {
        return "/betterchat reload";
    }

    @Override
    public void perform(Player player, String[] args) {

        if(player.hasPermission("BetterChat.commands.reload")) {
            try {
                Filemanager.getInstance().reloadFiles();
                ChatManager.getInstance().reloadChatManager();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

            String ReloadfilesText = Filemanager.getInstance().getlangSettings().getString("Reload.Reload_files");
            ReloadfilesText = PlaceholderAPI.setPlaceholders(getPlayer(player.getName()), ReloadfilesText);
            player.sendMessage(MSG.chatColors(ReloadfilesText));

        } else {
            String AntiswearMSG = Filemanager.getInstance().getlangSettings().getString("Commands.No_permissions");
            AntiswearMSG = PlaceholderAPI.setPlaceholders(getPlayer(player.getName()), AntiswearMSG);

            player.sendMessage(MSG.chatColors(AntiswearMSG));
        }
    }
}