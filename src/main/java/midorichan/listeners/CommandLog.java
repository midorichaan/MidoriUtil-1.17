package midorichan.listeners;

import midorichan.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandLog implements Listener {

    private boolean ready = false;
    private static Main plugin = Main.getInstance();
    private String prefix = plugin.getPrefix();

    public void __init__() {
        ready = true;
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public static void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (plugin.getConfig().getBoolean("admin-player-commandlog")) {
            String logmsg = String.format(" §a* §7%s : %s", e.getPlayer().getName(), e.getMessage());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("midoriutil.admin.cmdlog") || p.isOp()) {
                    p.sendMessage(logmsg);
                    plugin.log(logmsg);
                }
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public static void onServerCommand(ServerCommandEvent e){
        if (e.getSender() instanceof BlockCommandSender) {
            if (plugin.getConfig().getBoolean("admin-cmdblock-commandlog")) {
                BlockCommandSender s = (BlockCommandSender) e.getSender();

                if (s.getBlock() == null || !(s.getBlock().getState() instanceof CommandBlock)) {
                    return;
                }

                CommandBlock cmdb = (CommandBlock) s.getBlock().getState();
                String cmd = cmdb.getCommand();
                String logmsg = String.format(" §a* §7CMDBLOCK : %s [%s - X: %s Y: %s Z: %s]", cmd, cmdb.getLocation().getWorld().getName(), cmdb.getLocation().getBlockX(), cmdb.getLocation().getBlockY(), cmdb.getLocation().getBlockZ());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("midoriutil.admin.cmdlog") || p.isOp()) {
                        p.sendMessage(logmsg);
                        plugin.log(logmsg);
                    }
                }
            }
        } else {
            if (Main.config.getBoolean("admin-console-commandlog")) {
                String logmsg = String.format(" §a* §7CONSOLE : %s", e.getCommand());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("midoriutil.admin.cmdlog") || p.isOp()) {
                        p.sendMessage(logmsg);
                        plugin.log(logmsg);
                    }
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public static void onRemoteServerCommand(RemoteServerCommandEvent e){
        if (Main.config.getBoolean("admin-rcon-commandlog")) {
            String logmsg = String.format(" §a* §7RCON : %s", e.getCommand());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("midoriutil.admin.cmdlog") || p.isOp()) {
                    p.sendMessage(logmsg);
                    plugin.log(logmsg);
                }
            }
        }
    }

}
