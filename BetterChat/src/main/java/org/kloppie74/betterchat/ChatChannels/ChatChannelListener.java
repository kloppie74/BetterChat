package org.kloppie74.betterchat.ChatChannels;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.kloppie74.betterchat.Commands.PrivateMSG;
import org.kloppie74.betterchat.FileManager.ChatManager;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.FileManager.PlayerDataManager;
import org.kloppie74.betterchat.MSG;

import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;


public class ChatChannelListener implements Listener {

    private HashMap<String, Long> timing = new HashMap<String, Long>();

    public static class DataStorage {
        public static HashMap<UUID, String> DoubleMSG = new HashMap<UUID, String>();

        public static HashMap<UUID, String> getDoubleMSG() {
            return DoubleMSG;
        }
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(Filemanager.getInstance().getSettings().getString("Antiswear.enabled").equalsIgnoreCase("true")) {
            for (String Message : Filemanager.getInstance().getSettings().getStringList("Antiswear.Blacklisted_Words")) {

                String Message2 = event.getMessage().toLowerCase();

                if(Message2.contains(Message.toLowerCase())){
                    event.setCancelled(true);

                    String AntiswearMSG = Filemanager.getInstance().getlangSettings().getString("Moderator.Antiswear");
                    AntiswearMSG = PlaceholderAPI.setPlaceholders(getPlayer(event.getPlayer().getName()), AntiswearMSG);

                    final String AntiSwearText = AntiswearMSG;
                    final String AntiSwearplaceholder = AntiSwearText.replace("{blocked}", String.valueOf(Message));

                    event.getPlayer().sendMessage(MSG.chatColors(AntiSwearplaceholder));
                    return;
                }
            }
        }

      //  allow-double-message: false

        if(Filemanager.getInstance().getSettings().getString("Antispam.enabled").equalsIgnoreCase("true")) {

            FileConfiguration settings = Filemanager.getInstance().getSettings();
            FileConfiguration Lang = Filemanager.getInstance().getlangSettings();
            String NewMSG = event.getMessage();

            Long lastUse = this.timing.get(player.getName());
            Long time = System.currentTimeMillis();
            int cooldown = settings.getInt("Antispam.cooldown");
            if(lastUse == null) {
                timing.put(player.getName(), time);
            } else if (lastUse + cooldown * 1000 < time) {
                timing.put(player.getName(), time);
            } else {

                double wait = lastUse + cooldown * 1000 - time;
                double wait2 = wait / 1000;
                double wait3 = Math.round(wait2);

                String AntispamMSG = Lang.getString("Moderator.AntiSpam.Cooldown");
                AntispamMSG = PlaceholderAPI.setPlaceholders(getPlayer(event.getPlayer().getName()), AntispamMSG);

                final String AntiSpamText = AntispamMSG;
                final String AntiSpamplaceholder = AntiSpamText.replace("{seconds}", Integer.toString((int) wait3));

                event.getPlayer().sendMessage(MSG.chatColors(AntiSpamplaceholder));
                DataStorage.getDoubleMSG().put(player.getUniqueId(), NewMSG);
                event.setCancelled(true);
                return;
            }
        }

        if(Filemanager.getInstance().getSettings().getString("Antispam.allow-double-message").equalsIgnoreCase("false")) {

            FileConfiguration Lang = Filemanager.getInstance().getlangSettings();
            String MSGOld = DataStorage.getDoubleMSG().get(player.getUniqueId());
            String NewMSG = event.getMessage();

            if(MSGOld == null) {
                DataStorage.getDoubleMSG().put(player.getUniqueId(), NewMSG);
            } else if(NewMSG.toLowerCase().equals(MSGOld.toLowerCase())) {
                String AntispamMSG = Lang.getString("Moderator.AntiSpam.Double_message");
                AntispamMSG = PlaceholderAPI.setPlaceholders(getPlayer(event.getPlayer().getName()), AntispamMSG);

                player.sendMessage(MSG.chatColors(AntispamMSG));
                event.setCancelled(true);
                return;
            }
        }

        for(Player p : Bukkit.getOnlinePlayers()){

            FileConfiguration chat = ChatManager.getInstance().getChatchannels();
            FileConfiguration playerdata = PlayerDataManager.get();
            String channel = playerdata.getString("Channel");

            // Permissions
            String permission = chat.getString("Channels." + channel + ".Permission");

            //Format
            String format = chat.getString("Channels." + channel + ".Format");

            //Player Message
            String message = event.getMessage();

            //PlaceHolders
            format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);

            FileConfiguration settings = Filemanager.getInstance().getSettings();

            if (p.hasPermission(permission)) {
                event.setCancelled(true);

                if(settings.getString("HoverMessage.Enable").equalsIgnoreCase("true")) {
                    TextComponent Component = new TextComponent(MSG.chatColors(format + message));
                    Component.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, settings.getString("HoverMessage.Suggest_Command") ) );

                    String CompoText = MSG.chatColors(settings.getString("HoverMessage.Layout"));
                    CompoText = MSG.chatColors(PlaceholderAPI.setPlaceholders(event.getPlayer(), CompoText));
                    Component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( CompoText ).create() ) );

                    p.sendMessage(Component);
                } else {
                    p.sendMessage(MSG.chatColors(format + message));
                }
                double AddMessage = Double.parseDouble(playerdata.getString("Messages")) + 1;
                playerdata.set("Messages", AddMessage);

                PlayerDataManager.save();
                Bukkit.getConsoleSender().sendMessage(MSG.chatColors(format + message));
            } else if(permission.equalsIgnoreCase("None")) {
                event.setCancelled(true);
                if(settings.getString("HoverMessage.Enable").equalsIgnoreCase("true")) {
                    TextComponent Component = new TextComponent(MSG.chatColors(format + message));
                    Component.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, settings.getString("HoverMessage.Suggest_Command") ) );

                    String CompoText = MSG.chatColors(settings.getString("HoverMessage.Layout"));
                    CompoText = MSG.chatColors(PlaceholderAPI.setPlaceholders(event.getPlayer(), CompoText));
                    Component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( CompoText ).create() ) );

                    p.sendMessage(Component);
                } else {
                    p.sendMessage(MSG.chatColors(format + message));
                }

                double AddMessage = Double.parseDouble(playerdata.getString("Messages")) + 1;
                playerdata.set("Messages", AddMessage);

                PlayerDataManager.save();
                Bukkit.getConsoleSender().sendMessage(MSG.chatColors(format + message));
            }

        }

        event.setCancelled(true);

    }

}
