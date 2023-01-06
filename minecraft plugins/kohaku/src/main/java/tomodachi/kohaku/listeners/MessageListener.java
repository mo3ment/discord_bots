package tomodachi.kohaku.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.awt.Color;

public class MessageListener implements Listener {

    @EventHandler
    public void whenAmongUs(AsyncPlayerChatEvent e) {
        if (e.getMessage().toLowerCase().contains("among us") || e.getMessage().toLowerCase().contains("amongus") || e.getMessage().toLowerCase().contains("amon gus") || e.getMessage().toLowerCase().contains("amogus")) {
            e.getPlayer().sendMessage("§l[" + ChatColor.of(Color.pink) + "§lTomodachi" + ChatColor.WHITE + "§l] " + ChatColor.YELLOW + "§lKohaku" + ChatColor.WHITE + "§l: Hey! Stop being " + ChatColor.RED + "§lSUS" + ChatColor.WHITE + "§l!!");
        }
    }
}
