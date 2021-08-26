package midorichan.listeners;

import midorichan.chairs.Chair;
import midorichan.Main;
import midorichan.utils.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class PlayerListener implements Listener {

    private static Main plugin = Main.getInstance();
    private static boolean ready = false;

    public static void __init__() {
        ready = true;
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!ready) {
            return;
        }

        if (plugin.getConfig().getBoolean("player-login-message")) {
            return;
        }

        Player player = e.getPlayer();

        if (player.isOp() || player.hasPermission("midoriutil.admin.op")) {
            e.setJoinMessage(plugin.getPrefix() + "§a[OP]§r" + player.getName() + " さんがサーバーに参加しました！");
        } else {
            e.setJoinMessage(plugin.getPrefix() + player.getName() + " さんがサーバーに参加しました！");
        }

        if(Double.isNaN(player.getLocation().getY())) {
            player.teleport(player.getWorld().getSpawnLocation());
        }

    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!ready) {
            return;
        }

        if (plugin.getConfig().getBoolean("player-logout-message")) {
            return;
        }

        Player player = e.getPlayer();

        if (player.isOp() || player.hasPermission("midoriutil.admin.op")) {
            e.setQuitMessage(plugin.getPrefix() + "§a[OP]§r" + player.getName() + " さんがサーバーから退出しました...");
        } else {
            e.setQuitMessage(plugin.getPrefix() + player.getName() + " さんがサーバーから退出しました...");
        }

    }

}

