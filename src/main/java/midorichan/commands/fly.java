package midorichan.commands;

import midorichan.Main;
import midorichan.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class fly implements CommandExecutor {

    private static Main plugin = Main.getInstance();
    private static Utils util = new Utils();
    private static String prefix = plugin.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "コンソールからは使用できません。");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("fly")) {
            Player s = (Player) sender;
            if (s.isOp() || s.hasPermission("midoriutil.commands.fly")) {
                if (args.length == 0) {
                    if (s.getAllowFlight()) {
                        s.setAllowFlight(false);
                        s.sendMessage(prefix + "Flyモードが無効になりました。");
                        return true;
                    } else {
                        s.setAllowFlight(true);
                        s.sendMessage(prefix + "Flyモードが有効になりました。");
                        return true;
                    }
                } else if (args.length == 1) {
                    if (!s.hasPermission("midoriutil.commands.fly.other") || !s.isOp()) {
                        s.sendMessage(prefix + "権限がありません。");
                        return true;
                    }

                    String otherplayer = args[0];
                    Player other = Bukkit.getPlayer(otherplayer);

                    if (other == null) {
                        sender.sendMessage(prefix + "プレイヤー " + args[0] + " はオンラインではありません。");
                        return true;
                    }

                    if (other.getAllowFlight()) {
                        other.setAllowFlight(false);
                        sender.sendMessage(prefix + other.getName() + " のFlyモードが無効になりました。");
                        other.sendMessage(prefix + "Flyモードが無効になりました。");
                        return true;
                    } else {
                        other.setAllowFlight(true);
                        sender.sendMessage(prefix + other.getName() + " のFlyモードが有効になりました。");
                        other.sendMessage(prefix + "Flyモードが有効になりました。");
                        return true;
                    }
                } else {
                    sender.sendMessage(prefix + "引数が不正です。");
                    return true;
                }
            } else {
                sender.sendMessage(prefix + "権限がありません。");
                return true;
            }
        }
        return true;
    }

}
