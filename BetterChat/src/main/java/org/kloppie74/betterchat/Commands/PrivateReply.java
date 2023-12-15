package org.kloppie74.betterchat.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;


public class PrivateReply implements CommandExecutor {



    FileConfiguration LangFormat = Filemanager.getInstance().getlangSettings();
    FileConfiguration ChatFormat = Filemanager.getInstance().getSettings();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {


        Player sender = (Player) commandSender;

        if (!(commandSender instanceof Player)){
            String SenderNotPlayerText = LangFormat.getString("MSG_Format.msg_not_a_player");
            SenderNotPlayerText = PlaceholderAPI.setPlaceholders(getPlayer(commandSender.getName()), SenderNotPlayerText);
            commandSender.sendMessage(MSG.chatColors(SenderNotPlayerText));
            return true;
        }


        if (!PrivateMSG.DataStorage.getlastmsg().containsKey(sender.getUniqueId())) {
            String SenderMsgText = LangFormat.getString("MSG_Format.msg_reply_null");
            SenderMsgText = PlaceholderAPI.setPlaceholders(sender, SenderMsgText);

            sender.sendMessage(MSG.chatColors(SenderMsgText));
            return true;
        }

        Player target = Bukkit.getPlayer(PrivateMSG.DataStorage.getlastmsg().get(sender.getUniqueId()));
        if (target == null) {
            String SenderMsgText = ChatFormat.getString("MSG_Format.msg_target_offline");
            SenderMsgText = PlaceholderAPI.setPlaceholders(sender, SenderMsgText);

            sender.sendMessage(MSG.chatColors(SenderMsgText));
            PrivateMSG.DataStorage.getlastmsg().remove(target);
            return true;
        }

        String sm = "";

        //combine the arguments the player typed
        for (String s : args) {
            String arg = (s + " ");
            sm = (sm + arg);
        }

        String SenderMsgText = ChatFormat.getString("MSG_format.Sender");
        SenderMsgText = PlaceholderAPI.setPlaceholders(sender, SenderMsgText);

        String TargetMsgText = ChatFormat.getString("MSG_format.Target");
        TargetMsgText = PlaceholderAPI.setPlaceholders(sender, TargetMsgText);

        String ConsoleLogMsgText = ChatFormat.getString("MSG_format.Console_log");
        ConsoleLogMsgText = PlaceholderAPI.setPlaceholders(sender, ConsoleLogMsgText);

        final String TargetText = TargetMsgText + sm;
        final String Targetplaceholder = TargetText.replace("{sender}", String.valueOf(sender.getName())).replace("{target}", String.valueOf(target.getName()));

        final String SenderText = SenderMsgText + sm;
        final String Senderplaceholder = SenderText.replace("{sender}", String.valueOf(sender.getName())).replace("{target}", String.valueOf(target.getName()));

        final String LogText = ConsoleLogMsgText + sm;
        final String LogSenderplaceholder = LogText.replace("{sender}", String.valueOf(sender.getName())).replace("{target}", String.valueOf(target.getName()));

        target.sendMessage(MSG.chatColors(Targetplaceholder));
        sender.sendMessage(MSG.chatColors(Senderplaceholder));



        PrivateMSG.DataStorage.getlastmsg().put(target.getUniqueId(), sender.getUniqueId());
        return true;
    }

}
