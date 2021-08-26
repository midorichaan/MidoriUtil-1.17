package midorichan.commands;

import midorichan.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class reloadconfig implements CommandExecutor {

    private String prefix = Main.getPrefix();
    private Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp() || sender.hasPermission("midoriutil.commands.reloadconfig")) {
            if (cmd.getName().equalsIgnoreCase("reloadconfig")) {
                if (args.length == 0) {
                    plugin.reloadConfig();
                    plugin.config = Main.getInstance().getConfig();
                    sender.sendMessage(prefix + "Configを再読み込みしました。");
                    return true;
                } else {
                    sender.sendMessage(prefix + "引数が不正です。");
                    return true;
                }
            }
        } else {
            sender.sendMessage(prefix + "権限がありません。");
            return true;
        }

        return true;
    }

}
