package midorichan.commands;

import midorichan.Main;
import midorichan.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getpos implements CommandExecutor {

    private static Main plugin = Main.getInstance();
    private static Utils util = new Utils();
    private static String prefix = plugin.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("midoriutil.commands.getpos") || !sender.isOp()) {
            sender.sendMessage(prefix + "権限がありません。");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("getpos")) {
            if (args.length == 0) {
                sender.sendMessage(prefix + "対象プレイヤーを入力してください。");
                return true;
            }

            String targetname = args[0];
            Player targetplayer = Bukkit.getPlayer(targetname);

            if (targetplayer == null) {
                sender.sendMessage(prefix + "プレイヤー " + args[0] + " はオンラインではありません。");
                return true;
            }

            Location loc = targetplayer.getLocation();
            String[] str = {
                    " §2--------------|" + " §aLocation " + "§2|--------------",
                    "   Player: " + targetplayer.getName(),
                    "   UUID: " + targetplayer.getUniqueId().toString(),
                    "   World: " + loc.getWorld().getName(),
                    "   Location: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(),
                    " §2-----------------------------------"
            };

            sender.sendMessage(str);
            return true;

        }

        return true;
    }

}
