package tomodachi.sakura;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HijackInventory implements InventoryHolder {
    private Inventory inventory;
    private UUID _target;
    private int maxPages;

    public HijackInventory(UUID target, int currentPage) {
        _target = target;
        inventory = Bukkit.createInventory(this, 9*6, "(" + Bukkit.getOfflinePlayer(target).getName() + "'s inventory)");

        try {
            maxPages = 0;
            for (PermissionAttachmentInfo permission : Bukkit.getPlayer(target).getEffectivePermissions()) {
                if (permission.getPermission().startsWith("sakura.inv.") && ((Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()))) > maxPages)) {
                    maxPages = Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()));
                }
            }
        } catch (NullPointerException e) {
            File f = new File("./plugins/Sakura/player_info/PlayerMaxPages.yml");
            FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

            maxPages = (int) f_conf.get("" + target + "");
        }
        //System.out.println(maxPages);
        init(currentPage);
    }

    private void init(int currentPage) {
        //OpenInventory.isBeingMonitored = _target.getName();
        File f = new File("./plugins/Sakura/player_info/" + _target + "_content.yml");
        FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

        try{
            ItemStack[] inventoryContent = ((List<ItemStack>) f_conf.get(_target + ".content" + currentPage)).toArray(new ItemStack[0]);
            inventory.setContents(inventoryContent);
        }catch(Exception e){
            if (maxPages > 0) {
                if (currentPage == 1) {
                    ArrayList<String> nextInventoryLore = new ArrayList<>();
                    nextInventoryLore.add(ChatColor.WHITE + "Click here to go");
                    nextInventoryLore.add(ChatColor.WHITE + "to page " + (currentPage+1));

                    inventory.setItem(53, createNext("§lNext Page", nextInventoryLore,  currentPage+1));
                }
                else {
                    ArrayList<String> nextInventoryLore = new ArrayList<>();
                    nextInventoryLore.add(ChatColor.WHITE + "Click here to go");
                    nextInventoryLore.add(ChatColor.WHITE + "to page " + (currentPage+1));

                    ArrayList<String> backInventoryLore = new ArrayList<>();
                    backInventoryLore.add(ChatColor.WHITE + "Click here to go");
                    backInventoryLore.add(ChatColor.WHITE + "to page " + (currentPage-1));

                    inventory.setItem(53, createNext("§lNext Page", nextInventoryLore, currentPage+1));
                    inventory.setItem(45, createNext("§lPrevious Page", backInventoryLore, currentPage-1));
                }
            }
        }
    }

    private ItemStack createNext(String name, List<String> lore, int pageNumber) {
        ItemStack item = new ItemStack(Material.CHEST, pageNumber);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public UUID getTarget() { return _target; }
}
