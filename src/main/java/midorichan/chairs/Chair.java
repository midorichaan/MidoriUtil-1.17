package midorichan.chairs;

import midorichan.utils.BlockFilter;
import midorichan.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Chair {

    private static Main plugin = Main.getInstance();

    private BukkitRunnable runnable;
    private int id;

    private Location loc;
    private Block b;
    private ArmorStand a = null;
    private Player p;

    public Chair(Player player, Location location) {
        this.loc = location;
        this.p = player;
        this.b = location.getBlock();

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (a == null) {
                    clear();
                    return;
                }

                if (a.getPassengers().size() == 0) {
                    clear();
                }

            }
        };

        id = runnable.runTaskTimer(plugin, 20, 20).getTaskId();

    }

    public ArmorStand getArmorStand() {
        return a;
    }

    public Player getPlayer() {
        return p;
    }

    public Block getBlock() {
        return b;
    }

    public BlockState getBlockState() {
        return b.getState();
    }

    public BlockData getBlockData() {
        return b.getState().getBlockData();
    }

    public BlockFace getBlockFace() {
        if (!BlockFilter.isStairsBlock(b.getType())) {
            return null;
        }

        return ((Stairs) b.getState().getData()).getFacing();
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isOccupied() {
        return !(a == null) && !a.isEmpty();
    }

    public void setPlayer(Player p) {
        this.p = p;
    }

    public void setChair(ArmorStand stand) {
        a = stand;
    }

    public void clear() {
        if (a != null) {
            a.remove();
        }

        loc = null;
        b = null;
        a = null;
        p = null;

        if (Bukkit.getScheduler().isCurrentlyRunning(id)) {
            runnable.cancel();
        }

    }

}
