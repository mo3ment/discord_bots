package tomodachi.sakura;

import org.bukkit.plugin.java.JavaPlugin;
import tomodachi.sakura.commands.OnTabCompleted;
import tomodachi.sakura.commands.OpenInventory;
import tomodachi.sakura.listeners.InventoryListener;

public final class Sakura extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("inv").setExecutor(new OpenInventory());
        getCommand("inv").setTabCompleter(new OnTabCompleted());
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
