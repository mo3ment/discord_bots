package tomodachi.sakura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import tomodachi.sakura.HijackInventory;
import tomodachi.sakura.InitializeInventory;

import java.io.*;
import java.util.UUID;

public class InventoryListener implements Listener {
    private int prevOrNext;
    private UUID target;
    @EventHandler
    public void onClickInventory(InventoryClickEvent e) throws NullPointerException {

        if (e.getClickedInventory() == null) { return; }
        if (e.getClickedInventory().getHolder() instanceof InitializeInventory) {
            Player player = (Player) e.getWhoClicked();
            int maxPages = 0;
            for (PermissionAttachmentInfo permission: player.getEffectivePermissions()) {
                if (permission.getPermission().startsWith("sakura.inv.") && ((Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()))) > maxPages)) {
                    maxPages = Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()));
                }
            }
            //System.out.println(maxPages);

            if (e.getCurrentItem() == null) { return; }

            else if ((e.getSlot() == 53 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) && e.getCurrentItem().getAmount() > maxPages) { e.setCancelled(true); }

            else if ((e.getSlot() == 53 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) && !(e.getCurrentItem().getAmount() > maxPages)) {
                e.setCancelled(true);
                prevOrNext = 53;
                int currentPage = e.getCurrentItem().getAmount();
                player.closeInventory();
                InitializeInventory nextInventory = new InitializeInventory(player, currentPage);

                File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

                f_conf.set("" + player.getUniqueId() + "", true);
                try { f_conf.save(f); } catch (IOException ex) { throw new RuntimeException(ex); }
                //System.out.println(player.getName() + " is opening the next page in their inventory");

                player.openInventory(nextInventory.getInventory());
            }
            else if (e.getSlot() == 45 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lPrevious Page")) {
                e.setCancelled(true);
                prevOrNext = 45;
                int currentPage = e.getCurrentItem().getAmount();
                player.closeInventory();
                InitializeInventory nextInventory = new InitializeInventory(player, currentPage);

                File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

                f_conf.set("" + player.getUniqueId() + "", true);
                try { f_conf.save(f); } catch (IOException ex) { throw new RuntimeException(ex); }
                //System.out.println(player.getName() + " is opening the previous page in their inventory");

                player.openInventory(nextInventory.getInventory());
            }
        }
        else if (e.getClickedInventory().getHolder() instanceof HijackInventory) {
            Player moderator = (Player) e.getWhoClicked();
            target = ((HijackInventory) e.getClickedInventory().getHolder()).getTarget();
            int maxPages = 0;

            try {
                for (PermissionAttachmentInfo permission : Bukkit.getPlayer(target).getEffectivePermissions()) {
                    if (permission.getPermission().startsWith("sakura.inv.") && ((Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()))) > maxPages)) {
                        maxPages = Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()));
                    }
                }
            } catch(NullPointerException ee) {
                File f = new File("./plugins/Sakura/player_info/PlayerMaxPages.yml");
                FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

                maxPages = (int) f_conf.get("" + target + "");
            }
            //System.out.println(maxPages);

            if (e.getCurrentItem() == null) { return; }

            else if ((e.getSlot() == 53 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) && e.getCurrentItem().getAmount() > maxPages) { e.setCancelled(true); }

            else if ((e.getSlot() == 53 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) && !(e.getCurrentItem().getAmount() > maxPages)) {
                e.setCancelled(true);
                prevOrNext = 53;
                int currentPage = e.getCurrentItem().getAmount();
                moderator.closeInventory();
                HijackInventory nextInventory = new HijackInventory(target, currentPage);

                File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

                f_conf.set("" + target + "", true);

                try { f_conf.save(f); } catch (IOException ex) { throw new RuntimeException(ex); }
                //System.out.println(e.getWhoClicked().getName() + " is opening the next page in " + target + " inventory");

                moderator.openInventory(nextInventory.getInventory());
            }
            else if (e.getSlot() == 45 && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§lPrevious Page")) {
                e.setCancelled(true);
                prevOrNext = 45;
                int currentPage = e.getCurrentItem().getAmount();
                moderator.closeInventory();
                HijackInventory nextInventory = new HijackInventory(target, currentPage);

                File f = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
                FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

                f_conf.set("" + target + "", true);

                try { f_conf.save(f); } catch (IOException ex) { throw new RuntimeException(ex); }
                //System.out.println(e.getWhoClicked().getName() + " is opening the previous page in " + Bukkit.getOfflinePlayer(target).getName() + " inventory");

                moderator.openInventory(nextInventory.getInventory());
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) throws IOException, NullPointerException {

        if (e.getInventory().getHolder() instanceof InitializeInventory) {
            ItemStack[] items = new ItemStack[e.getInventory().getSize()];
            int changeSlot = 0;
            File f = new File("./plugins/Sakura/player_info/" + e.getPlayer().getUniqueId() + "_content.yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);

            for(int i = 0; i < e.getInventory().getSize(); i++){
                if(e.getInventory().getItem(i) != null){
                    items[i] = e.getInventory().getItem(i);
                    if (items[i].getItemMeta().getDisplayName().equalsIgnoreCase("§lPrevious Page")) { changeSlot = 45; }
                    else if (items[i].getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) { changeSlot = 53; }
                }else{
                    items[i] = new ItemStack(Material.AIR);
                }
            }
            if (prevOrNext == 45){
                prevOrNext = 0;
                c.set(e.getPlayer().getUniqueId() + ".content" + (items[45].getAmount()+1), items);
                c.save(f);
            }
            else if (prevOrNext == 53){
                prevOrNext = 0;
                c.set(e.getPlayer().getUniqueId() + ".content" + (items[53].getAmount()-1), items);
                c.save(f);
            }
            else if (prevOrNext == 0) {
                if (changeSlot == 45) {
                    c.set(e.getPlayer().getUniqueId() + ".content" + (items[45].getAmount()+1), items);
                    c.save(f);
                }
                else if (changeSlot == 53) {
                    c.set(e.getPlayer().getUniqueId() + ".content" + (items[53].getAmount()-1), items);
                    c.save(f);
                }
            }
            File ff = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
            FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

            f_conf.set("" + e.getPlayer().getUniqueId() + "", false);
            f_conf.save(ff);
            //System.out.println(e.getPlayer().getName() + " is closing down their inventory");
        }
        else if (e.getInventory().getHolder() instanceof HijackInventory) {
            ItemStack[] items = new ItemStack[e.getInventory().getSize()];
            int changeSlot = 0;

            File f = new File("./plugins/Sakura/player_info/" + target + "_content.yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);

            for(int i = 0; i < e.getInventory().getSize(); i++){
                if(e.getInventory().getItem(i) != null){
                    items[i] = e.getInventory().getItem(i);
                    if (items[i].getItemMeta().getDisplayName().equalsIgnoreCase("§lPrevious Page")) { changeSlot = 45; }
                    else if (items[i].getItemMeta().getDisplayName().equalsIgnoreCase("§lNext Page")) { changeSlot = 53; }
                }else{
                    items[i] = new ItemStack(Material.AIR);
                }
            }
            if (prevOrNext == 45){
                prevOrNext = 0;
                c.set(target + ".content" + (items[45].getAmount()+1), items);
                c.save(f);
            }
            else if (prevOrNext == 53){
                prevOrNext = 0;
                c.set(target + ".content" + (items[53].getAmount()-1), items);
                c.save(f);
            }
            else if (prevOrNext == 0) {
                if (changeSlot == 45) {
                    c.set(target + ".content" + (items[45].getAmount()+1), items);
                    c.save(f);
                }
                else if (changeSlot == 53) {
                    c.set(target + ".content" + (items[53].getAmount()-1), items);
                    c.save(f);
                }
            }
            File ff = new File("./plugins/Sakura/player_info/InventoryCurrentlyOpen.yml");
            FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

            f_conf.set("" + target + "", false);
            f_conf.save(ff);
            //System.out.println(e.getPlayer().getName() + " is closing " + target + "'s inventory");
        }
    }

    @EventHandler
    public void savePlayerPerms(PlayerQuitEvent e) throws IOException{
        File f = new File("./plugins/Sakura/player_info/PlayerMaxPages.yml");
        FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);



        int maxPages = 0;
        for (PermissionAttachmentInfo permission : e.getPlayer().getEffectivePermissions()) {
            if (permission.getPermission().startsWith("sakura.inv.") && ((Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()))) > maxPages)) {
                maxPages = Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()));
            }
        }

        f_conf.set("" + e.getPlayer().getUniqueId() + "", maxPages);
        f_conf.save(f);
    }
}