package tomodachi.sakura.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnTabCompleted implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) throws NullPointerException {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();

            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return null;
    }
}
