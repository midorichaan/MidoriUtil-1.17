package midorichan.commands;

import midorichan.Main;
import midorichan.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hat implements CommandExecutor {

    private static Main plugin = Main.getInstance();
    private static Utils util = new Utils();
    private static String prefix = plugin.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(prefix + "コンソールからは使用できません。");
            return true;
        }

        if (!sender.hasPermission("midoriutil.commands.hat") || !sender.isOp()) {
            sender.sendMessage(prefix + "権限がありません。");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("hat")) {
            Player s = (Player) sender;

            if (args.length == 0) {
                ItemStack item = s.getInventory().getItemInMainHand();

                if (item.getType() == Material.AIR) {
                    s.sendMessage(prefix + "アイテムをかぶるにはそのアイテムを手に持つ必要があります。");
                    return true;
                } else {
                    if (s.getInventory().getHelmet() == null) {
                        s.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        s.getInventory().setHelmet(item);
                        s.updateInventory();
                        s.sendMessage(prefix + "アイテムをかぶりました。");
                        return true;
                    } else {
                        ItemStack i = s.getInventory().getHelmet();
                        s.getInventory().setHelmet(item);
                        s.getInventory().setItemInMainHand(i);
                        s.updateInventory();
                        s.sendMessage(prefix + "アイテムをかぶりました。");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(prefix + "引数が不正です。");
                return true;
            }

        }

        return true;
    }

}
