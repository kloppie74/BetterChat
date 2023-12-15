package org.kloppie74.betterchat.ChatChannels;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kloppie74.betterchat.FileManager.ChatManager;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.FileManager.PlayerDataManager;
import org.kloppie74.betterchat.MSG;


public class ChatChannelSwitch implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        FileConfiguration chat = ChatManager.getInstance().getChatchannels();
        FileConfiguration playerdata = PlayerDataManager.get();
        Player player = (Player) commandSender;

        FileConfiguration LangFormat = Filemanager.getInstance().getlangSettings();

        if(args.length == 1 && chat.isConfigurationSection("Channels." + args[0])) {



            if(player.hasPermission(chat.getString("Channels." + args[0] + ".Permission").toLowerCase())) {

                String pathswitch = LangFormat.getString("CustomChannel.switch_message");
                pathswitch = pathswitch.replace("{channel}", args[0]);
                String SwitchText = pathswitch;
                SwitchText = PlaceholderAPI.setPlaceholders(player, SwitchText);

                playerdata.set("Channel", args[0]);
                PlayerDataManager.save();
                player.sendMessage(MSG.chatColors(SwitchText));
            } else if(chat.getString("Channels." + args[0] + ".Permission").toLowerCase().equals("none")) {

                String pathswitch = LangFormat.getString("CustomChannel.switch_message");
                pathswitch = pathswitch.replace("{channel}", args[0]);
                String SwitchText = pathswitch;
                SwitchText = PlaceholderAPI.setPlaceholders(player, SwitchText);


                playerdata.set("Channel", args[0]);
                PlayerDataManager.save();
                player.sendMessage(MSG.chatColors(SwitchText));
            } else {

                String pathperm = LangFormat.getString("CustomChannel.no_permission");
                pathperm = pathperm.replace("{channel}", args[0]);
                String NoPermText = pathperm;
                NoPermText = PlaceholderAPI.setPlaceholders(player, NoPermText);

                player.sendMessage(MSG.chatColors(NoPermText));
            }
        } else {
            String channels = chat.getConfigurationSection("Channels").getKeys(false).toString();

            player.sendMessage(MSG.chatColors("&3-----== &eBetterChat &3==-----"));
            player.sendMessage(MSG.chatColors("&7"));
            player.sendMessage(MSG.chatColors("&7You can choose the following channels!"));
            player.sendMessage(MSG.chatColors("&7" + channels));


        }


        return true;
    }

}
