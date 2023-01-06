package tomodachi.asuka;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.awt.*;
import java.util.Random;

public class Asuka extends JavaPlugin {
    private final Random random = new Random();

    @Override
    public void onEnable() {

        // Schedule a task to run every 10-20 seconds (randomized)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

            // Choose a random message from the list
            int message = random.nextInt(4);

            switch(message) {
                case 1:
                    Bukkit.getServer().broadcastMessage("§l[" + ChatColor.of(Color.pink) + "§lTomodachi" + ChatColor.WHITE + "§l] " + ChatColor.RED + "§lAsuka" + ChatColor.WHITE + "§l: Remember to vote for great prices!");
                    break;
                case 2:
                    Bukkit.getServer().broadcastMessage("§l[" + ChatColor.of(Color.pink) + "§lTomodachi" + ChatColor.WHITE + "§l] " + ChatColor.RED + "§lAsuka" + ChatColor.WHITE + "§l: Loving the server? Consider ($)donating($) to keep the server running!");
                    break;
                case 3:
                    Bukkit.getServer().broadcastMessage("§l[" + ChatColor.of(Color.pink) + "§lTomodachi" + ChatColor.WHITE + "§l] " + ChatColor.RED + "§lAsuka" + ChatColor.WHITE + "§l: I hope you are having fun on the Tomodachi Pixelmon Server!");
                    break;
            }
        }, 20 * 60 * 30, 20 * 60 * 60);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
