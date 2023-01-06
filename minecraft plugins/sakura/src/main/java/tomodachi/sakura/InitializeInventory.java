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
import tomodachi.sakura.commands.OpenInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InitializeInventory implements InventoryHolder {

    private Inventory inventory;
    private Player _player;
    public int maxPages;

    public InitializeInventory(Player player, int currentPage) {
        _player = player;
        inventory = Bukkit.createInventory(this, 9*6, ChatColor.LIGHT_PURPLE + "Sakura's inventory");

        maxPages = 0;
        for (PermissionAttachmentInfo permission: player.getEffectivePermissions()) {
            if (permission.getPermission().startsWith("sakura.inv.") && ((Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()))) > maxPages)) {
                maxPages = Integer.parseInt(permission.getPermission().substring("sakura.inv.".length()));
            }
        }
        //System.out.println(maxPages);
        init(currentPage);
    }

    private void init(int currentPage) {
        File f = new File("./plugins/Sakura/player_info/" + _player.getUniqueId() + "_content.yml");
        FileConfiguration f_conf = YamlConfiguration.loadConfiguration(f);

        try{
            ItemStack[] inventoryContent = ((List<ItemStack>) f_conf.get(_player.getUniqueId() + ".content" + currentPage)).toArray(new ItemStack[0]);
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
}
