package org.kloppie74.betterchat.CommandManagers.Subcommands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kloppie74.betterchat.BetterChat;
import org.kloppie74.betterchat.CommandManagers.SubCommand;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

import static org.bukkit.Bukkit.getPlayer;

public class About extends SubCommand {


    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String getDescription() {
        return "Check the plugins information!";
    }

    @Override
    public String getSyntax() {
        return "/betterchat about";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(player.hasPermission("BetterChat.commands.about")) {

            player.sendMessage(MSG.chatColors("&3-----== &eBetterChat &3==-----"));
            player.sendMessage(MSG.chatColors("&7"));
            player.sendMessage(MSG.chatColors("&dVersion » &5" + BetterChat.getBetterChat().getDescription().getVersion()));
            player.sendMessage(MSG.chatColors("&dAuthor » &5Kloppie74"));
            player.sendMessage(MSG.chatColors("&dSpigot Link » &5https://www.spigotmc.org/resources/betterchat-1-8-1-20-support.110701/"));
            player.sendMessage(MSG.chatColors("&7"));

        } else {
            String AntiswearMSG = Filemanager.getInstance().getlangSettings().getString("Commands.No_permissions");
            AntiswearMSG = PlaceholderAPI.setPlaceholders(getPlayer(player.getName()), AntiswearMSG);

            player.sendMessage(MSG.chatColors(AntiswearMSG));
        }
    }

}
