package tomodachi.sakura.commands;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import tomodachi.sakura.HijackInventory;
import tomodachi.sakura.InitializeInventory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class OpenInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws NullPointerException {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("inv")) {
                if (args.length == 0) {

                    File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                    FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);
                    boolean isInvOpen;

                    try { isInvOpen = (boolean) f_conf.get("" + player.getUniqueId() + ""); } catch(Exception e) {
                        isInvOpen = false;
                        f_conf.set("" + player.getUniqueId() + "", false);
                    }
                    try { f_conf.save(f); } catch (IOException e) { throw new RuntimeException(e); }

                    //System.out.println("Checking if " + player.getName() + "'s inventory is currently open");
                    if (!isInvOpen) {
                        InitializeInventory inventory = new InitializeInventory(player, 1);
                        f_conf.set("" + player.getUniqueId() + "", true);

                        try { f_conf.save(f); } catch (IOException e) { throw new RuntimeException(e); }
                        //System.out.println(player.getName() + " is opening their inventory");
                        player.openInventory(inventory.getInventory());

                    } else { player.sendMessage("§l[" + ChatColor.of(Color.pink) + "§lTomodachi" + ChatColor.WHITE + "§l] " + ChatColor.LIGHT_PURPLE + "§lSakura" + ChatColor.WHITE + "§l: I apologize for the short notice but I'm trying to fix your inventory, sorry!"); }}

                else if(args.length == 1 && (player.hasPermission("bukkit.command.kick") || player.hasPermission("minecraft.command.kick"))) {
                    if (!(sender.getName().equals(args[0]))) {
                        UUID target_uuid = null;
                        for (OfflinePlayer t: Bukkit.getOfflinePlayers()) {
                            if (Objects.equals(t.getName(), args[0])) { target_uuid = t.getUniqueId(); }
                        }

                        if (target_uuid != null) {

                            File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                            FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);
                            boolean isInvOpen;

                            try { isInvOpen = (boolean) f_conf.get("" + target_uuid + ""); }catch(Exception e) {
                                isInvOpen = false;
                                f_conf.set("" + target_uuid + "", false);
                            }
                            try { f_conf.save(f); } catch (IOException e) { throw new RuntimeException(e); }
                                //System.out.println(player.getName() + " is trying to access " + target.getName() + "'s inventory");
                            if (!isInvOpen) {
                                HijackInventory inventory = new HijackInventory(target_uuid, 1);

                                f_conf.set("" + target_uuid + "", true);
                                try {
                                    f_conf.save(f);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                    //System.out.println(player.getName() + " is opening " + target.getName() + "'s inventory");
                                player.openInventory(inventory.getInventory());
                            }
                            else { player.sendMessage(ChatColor.RED + "The player you're currently trying to monitor has their inventory open!"); }
                        }
                        else if (target_uuid == null) { player.sendMessage(ChatColor.RED + "That player is not in the server records!"); }
                    }
                    else { player.sendMessage(ChatColor.RED + "Use /inv to access your own inventory"); }
                }
                else{ player.sendMessage(ChatColor.RED + "You don't have permission to use that command!"); }
            }
        }
        return true;
    }
}
