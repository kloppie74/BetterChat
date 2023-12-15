package org.kloppie74.betterchat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.kloppie74.betterchat.ChatChannels.ChatChannelListener;
import org.kloppie74.betterchat.ChatChannels.ChatChannelSwitch;
import org.kloppie74.betterchat.CommandManagers.CommandManager;
import org.kloppie74.betterchat.Commands.PrivateMSG;
import org.kloppie74.betterchat.Commands.PrivateReply;
import org.kloppie74.betterchat.FileManager.ChatManager;
import org.kloppie74.betterchat.FileManager.Filemanager;
import org.kloppie74.betterchat.FileManager.PlayerDataManager;
import org.kloppie74.betterchat.Listeners.FirstJoinListener;
import org.kloppie74.betterchat.Listeners.JoinQuitListener;
import org.kloppie74.betterchat.Listeners.MotdListener;
import org.kloppie74.betterchat.Placeholder.Placeholders;
import org.kloppie74.betterchat.UpdateCheckerAPI.ConfigUpdater;
import org.kloppie74.betterchat.api.UpdateChecker;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class BetterChat extends JavaPlugin implements @NotNull Listener {

    public static BetterChat getBetterChat(){
        return BetterChat;
    }

    private static BetterChat BetterChat;

    Filemanager Filemanagersetup = Filemanager.getInstance();

    private static final List<String> ignoredSections = Arrays.asList();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if(Filemanager.getInstance().getSettings().getString("UpdateChecker").toLowerCase().equals("enabled")) {
            Player player = event.getPlayer();

            if (player.isOp()) {
                new UpdateChecker(this, 110701).getVersion(version -> {
                    if (!this.getDescription().getVersion().equals(version)) {

                        player.sendMessage(MSG.chatColors("&b&l&nBetterChat"));
                        player.sendMessage(MSG.chatColors("&f"));
                        player.sendMessage(MSG.chatColors("&7There is a new &a&nupdate&r &7available! &7Current version: &a&n" + this.getDescription().getVersion()));
                        player.sendMessage(MSG.chatColors("&7New Version &a&n" + version));
                        player.sendMessage(MSG.chatColors("&r&7Download now! &b» &e&nhttps://www.spigotmc.org/resources/betterchat-1-8-1-20-support.110701/"));
                    }
                });
            }
        }
    }

    @Override
    public void onEnable() {

        BetterChat = this;
        Filemanagersetup.setup(this);
        ChatManager.getInstance().setup(this);

        FileConfiguration ChatFormat = Filemanager.getInstance().getSettings();


        if(ChatFormat.getString("UpdateChecker").toLowerCase().equals("enabled")) {
            new UpdateChecker(this, 110701).getVersion(version -> {
                if (!this.getDescription().getVersion().equals(version)) {
                    getLogger().info(ChatColor.GREEN + "There is a new update available.");
                    getLogger().info(ChatColor.GREEN + "Download now! »" + ChatColor.YELLOW + " https://www.spigotmc.org/resources/betterchat-1-8-1-20-support.110701/");
                }
            });
        }


        int pluginId = 19555; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        // Optional: Add custom charts
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7§l---------------==§e§lBetterChat§7§l==---------------");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§e§lBetterChat");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§fa plugin build by §bKloppie74");
        Bukkit.getConsoleSender().sendMessage("§dMade with love, @Kloppie74");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§floading Latest version of §e§lBetterChat");
        Bukkit.getConsoleSender().sendMessage("§e§lBetterchat §a" + getBetterChat().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7§l---------------==§e§lBetterChat§7§l==---------------");
        Bukkit.getConsoleSender().sendMessage(" ");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
            getServer().getPluginManager().registerEvents(new MotdListener(), this);
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new FirstJoinListener(), this);
            getServer().getPluginManager().registerEvents(new ChatChannelListener(), this);
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }


        getCommand("r").setExecutor(new PrivateReply());
        getCommand("msg").setExecutor(new PrivateMSG());
        getCommand("chat").setExecutor(new ChatChannelSwitch());
        getCommand("betterchat").setExecutor(new CommandManager());

        try {
            if (Filemanager.getInstance().getSettings().getBoolean("Auto_Broadcast.Global_Messages.Enable"))
                globalMessages();
        } catch (Exception e) {
            getLogger().severe("There has been an error setting up auto broadcast. Stacktrace...");

            e.printStackTrace();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataManager.create(player);
        }


        File SettingsFile = new File(getDataFolder(), "Settings.yml");

        try {
            ConfigUpdater.update(getBetterChat(), "Settings.yml", SettingsFile, ignoredSections);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File ChatchannelsFile = new File(getDataFolder(), "Chatchannels.yml");

        try {
            ConfigUpdater.update(getBetterChat(), "Chatchannels.yml", ChatchannelsFile, ignoredSections);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File LangFile = new File(getDataFolder(), "Lang/lang_en_us.yml");

        try {
            ConfigUpdater.update(getBetterChat(), "Lang/lang_en_us.yml", LangFile, ignoredSections);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Filemanager.getInstance().getSettings().set("ConfigVersion", getBetterChat().getServer().getVersion());

        try {
            Filemanager.getInstance().reloadFiles();
            ChatManager.getInstance().reloadChatManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    // Auto Broadcast!
    public void globalMessages() {
        FileConfiguration autobroadcast = Filemanager.getInstance().getSettings();

        String sound = autobroadcast.getString("Auto_Broadcast.Global_Messages.Sound");
        String prefix = autobroadcast.getString("Auto_Broadcast.Global_Messages.Prefix");
        int interval = autobroadcast.getInt("Auto_Broadcast.Global_Messages.Interval");
        List<String> messages = autobroadcast.getStringList("Auto_Broadcast.Global_Messages.Messages");

        Placeholders pch = new Placeholders();

        new BukkitRunnable() {
            int line = 0;
            public void run() {
                if (autobroadcast.getBoolean("Auto_Broadcast.Global_Messages.Enable")) {
                    for (Player player : BetterChat.getServer().getOnlinePlayers()) {
                        if (autobroadcast.getBoolean("Auto_Broadcast.Global_Messages.Header_And_Footer")) {
                            //Autobroadcast


                            String AutobroadcastHeader = autobroadcast.getString("Auto_Broadcast.Global_Messages.Header");
                            AutobroadcastHeader = PlaceholderAPI.setPlaceholders(player, AutobroadcastHeader);

                            String AutobroadcastMessage = messages.get(line);
                            AutobroadcastMessage = PlaceholderAPI.setPlaceholders(player, AutobroadcastMessage);

                            String AutobroadcastFooter = autobroadcast.getString("Auto_Broadcast.Global_Messages.Footer");
                            AutobroadcastFooter = PlaceholderAPI.setPlaceholders(player, AutobroadcastFooter);

                            String AutobroadcastPrefix = autobroadcast.getString("Auto_Broadcast.Global_Messages.Prefix");
                            AutobroadcastPrefix = PlaceholderAPI.setPlaceholders(player, AutobroadcastPrefix);

                            player.sendMessage(MSG.chatColors(AutobroadcastHeader));
                            player.sendMessage(MSG.chatColors(AutobroadcastPrefix + AutobroadcastMessage));
                            player.sendMessage(MSG.chatColors(AutobroadcastFooter));


                        } else {

                            String AutobroadcastMessage = messages.get(line);
                            AutobroadcastMessage = PlaceholderAPI.setPlaceholders(player, AutobroadcastMessage);

                            String AutobroadcastPrefix = autobroadcast.getString("Auto_Broadcast.Global_Messages.Prefix");
                            AutobroadcastPrefix = PlaceholderAPI.setPlaceholders(player, AutobroadcastPrefix);

                            player.sendMessage(MSG.chatColors(AutobroadcastPrefix + AutobroadcastMessage));
                        }

                        try {
                            player.playSound(player.getLocation(), Sound.valueOf(sound), 10, 1);
                        } catch (IllegalArgumentException ignored) {}
                    }
                }
                line++;

                if (line >= messages.size()) line = 0;
            }
        }.runTaskTimer(BetterChat, 0L, 20L * interval);
    }



    @Override
    public void onDisable() {

        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7§l---------------==§e§lBetterChat§7§l==---------------");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§e§lBetterChat");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§fa plugin build by §bKloppie74");
        Bukkit.getConsoleSender().sendMessage("§dMade with love, @Kloppie74");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§fDisabling §e§lBetterChat §a" + getBetterChat().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7§l---------------==§e§lBetterChat§7§l==---------------");
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}
