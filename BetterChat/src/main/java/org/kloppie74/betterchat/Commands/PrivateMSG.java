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

public class PrivateMSG implements CommandExecutor {
    FileConfiguration LangFormat = Filemanager.getInstance().getlangSettings();
    FileConfiguration ChatFormat = Filemanager.getInstance().getSettings();


    public static class DataStorage {
        public static HashMap<UUID, UUID> lastmsg = new HashMap<>();

        public static HashMap<UUID, UUID> getlastmsg() {
            return lastmsg;
        }
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)){
            String SenderNotPlayerText = LangFormat.getString("MSG_Format.msg_not_a_player");
            SenderNotPlayerText = PlaceholderAPI.setPlaceholders(getPlayer(commandSender.getName()), SenderNotPlayerText);
            commandSender.sendMessage(MSG.chatColors(SenderNotPlayerText));
            return true;
        }


        if (ChatFormat.getString("MSG_format.Enable") == "true") {

            if (args.length > 1) {

                Player target = Bukkit.getServer().getPlayer(args[0]);
                Player sender = (Player) commandSender;

                if (target == null) {
                    String PlayerNotFoundText = LangFormat.getString("MSG_Format.msg_player_not_found");
                    PlayerNotFoundText = PlaceholderAPI.setPlaceholders(getPlayer(commandSender.getName()), PlayerNotFoundText);
                    commandSender.sendMessage(MSG.chatColors(PlayerNotFoundText));
                } else if (target == sender) {
                    String MSgYourSelfText = LangFormat.getString("MSG_Format.msg_player_is_target");
                    MSgYourSelfText = PlaceholderAPI.setPlaceholders(getPlayer(commandSender.getName()), MSgYourSelfText);
                    commandSender.sendMessage(MSG.chatColors(MSgYourSelfText));


                } else {
                    String sm = "";

                    //combine the arguments the player typed
                    for (int i = 1; i < args.length; i++) {
                        String arg = (args[i] + " ");
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

                    DataStorage.getlastmsg().put(target.getUniqueId(), sender.getUniqueId());



                    if (ChatFormat.getString("MSG_format.Console_log_Enable") == "true") {
                        Bukkit.getConsoleSender().sendMessage(LogSenderplaceholder);
                    }
                }
            } else {
                Player sender = (Player) commandSender;

                String UnknownCommandText = LangFormat.getString("MSG_Format.msg_command_unknown");
                UnknownCommandText = PlaceholderAPI.setPlaceholders(sender, UnknownCommandText);
                sender.sendMessage(MSG.chatColors(UnknownCommandText));
            }
        }
        return true;
    }
}