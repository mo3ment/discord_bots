package tomodachi.kohaku;

import org.bukkit.plugin.java.JavaPlugin;
import tomodachi.kohaku.listeners.MessageListener;

public final class Kohaku extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MessageListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
