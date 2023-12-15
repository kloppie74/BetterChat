package org.kloppie74.betterchat.CommandManagers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kloppie74.betterchat.CommandManagers.Subcommands.About;
import org.kloppie74.betterchat.CommandManagers.Subcommands.ReloadFiles;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.MSG;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getPlayer;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(){
        subcommands.add(new About());
        subcommands.add(new ReloadFiles());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            if(sender.hasPermission("Betterchat.commands.help")) {
                Player p = (Player) sender;

                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                            getSubcommands().get(i).perform(p, args);
                        }
                    }
                } else if (args.length == 0) {
                    p.sendMessage(MSG.chatColors("&3-----== &eBetterChat &3==-----"));
                    p.sendMessage(MSG.chatColors("&7"));
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        p.sendMessage(MSG.chatColors("&d" + getSubcommands().get(i).getSyntax() + " &7- &5" + getSubcommands().get(i).getDescription()));
                    }
                    p.sendMessage(MSG.chatColors("&d/msg &7- &5Send a private message to a player!"));
                    p.sendMessage(MSG.chatColors("&7"));
                }
            } else {
                Player p = (Player) sender;

                String AntiswearMSG = Filemanager.getInstance().getlangSettings().getString("Commands.No_permissions");
                AntiswearMSG = PlaceholderAPI.setPlaceholders(getPlayer(p.getName()), AntiswearMSG);

                p.sendMessage(MSG.chatColors(AntiswearMSG));

            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

}

