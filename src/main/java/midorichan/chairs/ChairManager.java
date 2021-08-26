package midorichan.chairs;

import midorichan.Main;
import midorichan.enums.MessageType;
import midorichan.utils.BlockFilter;
import midorichan.enums.MessageConstruct;
import midorichan.events.*;
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
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class ChairManager implements Listener {

    private Map<UUID, Chair> chairMap = new HashMap<>();
    public List<Chair> chairs = new ArrayList<>();
    private List<String> fakeSeats = new ArrayList<>();
    private List<UUID> leaving = new ArrayList<>();
    private List<UUID> orienting = new ArrayList<>();

    private static boolean exitWhereFacing;
    public static Vector stairSeatingPosition;
    private static boolean ready = false;
    private static double maxDistance;
    private static boolean checkForSigns;
    private static boolean trapSeats;
    private static boolean requireEmptyHand;
    private static boolean faceAttacker;

    public static void __init__() {
        faceAttacker = false;
        exitWhereFacing = false;
        maxDistance = 2D;
        checkForSigns = false;
        trapSeats = false;
        requireEmptyHand = false;
        stairSeatingPosition = new Vector(0.5D,0.3D,0.5);
        ready = true;
    }

    public void reload(Main plugin) {
        ready = true;
        __init__();
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!ready) {
            return;
        }

        Player p = e.getEntity();
        Chair chair = chairMap.get(p.getUniqueId());
        ChairLeaveEvent le = new ChairLeaveEvent(chair, p);
        Utils.callEvent(le);
    }

    @EventHandler
    public void onChairSit(ChairSitEvent event) {
        if (!ready) {
            return;
        }

        Chair chair = event.getChair();
        if (chair == null) {
            return;
        }

        if (chair.getLocation() == null) {
            return;
        }

        if (event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();

        if (Utils.playerIsSeated(player.getUniqueId(), chairMap)) {
            clearPlayer(player);
        }

        boolean done = sitPlayer(chair, player);

        if (!done) {
            return;
        }
        chairs.add(chair);
        chairMap.put(player.getUniqueId(), chair);
    }

    @EventHandler
    public void onChairLeave(ChairLeaveEvent event) {
        if (!ready) {
            return;
        }

        Chair chair = event.getChair();
        if (chair == null) {
            return;
        }

        if (chair.getLocation() == null) {
            return;
        }

        Block block = chair.getLocation().getBlock();
        Player player = event.getPlayer();
        boolean flag = event.getFacing();

        Location exit = block.getLocation();

        clearPlayer(player);
        if (flag) {
            exit = findExitPoint(player.getLocation(), block).getLocation();
        }

        if (Utils.surroundedBlock(block) || Utils.nearLiquid(player.getLocation().getBlock())) {
            exit = block.getRelative(BlockFace.UP).getLocation();
        }

        exit.setDirection(player.getEyeLocation().getDirection());
        exit.add(0.5,0,0.5);
        player.teleport(exit);
        if (!flag) {
            Vector v = player.getEyeLocation().getDirection();
            v.setY(0);
            v.normalize();
            v.setY(1);
            player.setVelocity(v.multiply(0.25));
        }
    }

    @EventHandler
    public void onChairReplace(ChairReplaceEvent event) {
        if (!ready) {
            return;
        }

        Chair chair = event.getChair();
        if (chair == null) {
            return;
        }

        if (chair.getLocation() == null) {
            return;
        }

        Block block = chair.getBlock();
        Player player = event.getPlayer();
        Player seated = event.getReplaced();

        if (player.getUniqueId() == seated.getUniqueId()) {
            return;
        }

        ChairSitEvent sitEvent = new ChairSitEvent(new Chair(player, block.getLocation()), player);
        Utils.callEvent(sitEvent);
    }

    @EventHandler
    public void onChairCheck(ChairCheckEvent event) {
        if (!ready) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (player.getLocation().distance(block.getLocation().add(0.5, 0, 0.5)) >= maxDistance) {
            Utils.callEvent(new MessageEvent(MessageType.TOOFAR, player));
            return;
        }

        if (!Utils.blockIsChair(block, chairs)) {
            ChairSitEvent sitEvent = new ChairSitEvent(new Chair(player, block.getLocation()), player);
            Utils.callEvent(sitEvent);
        } else {
            Chair chair = Utils.getChairFromBlock(block, chairs);
            if (chair == null) {
                return;
            }

            if (chair.getArmorStand() != null && chair.getArmorStand().getPassengers().size() == 0) {
                chair.setPlayer(player);
                chair.getArmorStand().addPassenger(player);
                return;
            }

            if (chair.getPlayer() == null) {
                return;
            }

            if (player.getUniqueId().equals(chair.getPlayer().getUniqueId())) {
                orienting.add(player.getUniqueId());
                Location loc = chair.getArmorStand().getLocation();
                loc.setDirection(player.getEyeLocation().getDirection().setY(0));
                chair.getArmorStand().remove();
                ArmorStand newFakeSeat = Utils.generateFakeSeatDir(chair, loc.getDirection());
                chair.setChair(newFakeSeat);
                chair.getArmorStand().addPassenger(player);
            } else {
                MessageEvent occupied = new MessageEvent(MessageType.OCCUPIED, MessageConstruct.OFFENSIVE, player, chair.getPlayer());
                Utils.callEvent(occupied);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!ready) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getPlayer().isSneaking()) {
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (block == null) {
            return;
        }

        if (requireEmptyHand && item != null) {
            return;
        }

        if (item != null && item.getType() != Material.AIR) {
            if (item.getType().isBlock()) {
                return;
            }
        }

        if (!BlockFilter.isStairsBlock(block.getType())){
            return;
        }

        if (BlockFilter.isStairsBlock(block.getType())) {
            if (!Utils.validStair(block)) {
                return;
            } else {
                if (checkForSigns) {
                    if (!Utils.validCouch(block)) {
                        if (!trapSeats) {
                            return;
                        }

                        if (!Utils.throneChair(block)) {
                            Utils.callEvent(new MessageEvent(MessageType.NOSIGNS, player));
                            return;
                        }
                    } else if (block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                        return;
                    }
                }

                if (trapSeats && block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                    if (!Utils.throneChair(block)) return;
                }
            }
        } else if (block.getRelative(BlockFace.UP).getType() != Material.AIR) {
            return;
        }

        if (event.getBlockFace() == BlockFace.DOWN) {
            return;
        }

        Utils.callEvent(new ChairCheckEvent(block, player));
    }

    @EventHandler
    public void onChangeGameMode(PlayerGameModeChangeEvent event) {
        if (!ready) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        GameMode gameMode = event.getNewGameMode();
        Chair chair = chairMap.get(event.getPlayer().getUniqueId());

        if (gameMode == GameMode.SPECTATOR && chair != null) {
            clearChair(chair);
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!ready) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Chair chair = chairMap.get(player.getUniqueId());
        event.getDismounted();

        if (chair == null || event.getDismounted().getType() != EntityType.ARMOR_STAND) {
            return;
        }

        if (leaving.contains(player.getUniqueId())) {
            return;
        }

        if (orienting.contains(player.getUniqueId())) {
            orienting.remove(player.getUniqueId());
            return;
        }
        leaving.add(player.getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!ready) {
            return;
        }

        Player player = event.getPlayer();
        if (leaving.contains(player.getUniqueId())) {
            ChairLeaveEvent leaveEvent = new ChairLeaveEvent(chairMap.get(player.getUniqueId()), player, exitWhereFacing);
            Utils.callEvent(leaveEvent);
            leaving.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void takeDamage(EntityDamageByEntityEvent event) {
        if (!ready) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Entity attacker = event.getDamager();
        ProjectileSource source = null;

        if (attacker instanceof Projectile) {
            source = ((Projectile) attacker).getShooter();
        }

        Chair chair = chairMap.get(player.getUniqueId());
        if (chair == null) {
            return;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ready) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        chairs.stream()
                .filter(Objects::nonNull)
                .filter(c -> c.getLocation() != null)
                .filter(c -> Utils.samePosition(event.getBlock(), c.getBlock()))
                .findAny();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void quit(PlayerQuitEvent event) {
        if (!ready) {
            return;
        }

        Player player = event.getPlayer();
        Chair chair = chairMap.get(player.getUniqueId());
        ChairLeaveEvent leaveEvent = new ChairLeaveEvent(chair, player, exitWhereFacing);
        Utils.callEvent(leaveEvent);
    }

    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent event) {
        if (!ready) {
            return;
        }

        for (Block block : event.getBlocks()) {
            for (Chair chair: chairs) {
                if (chair == null) {
                    continue;
                }

                if (chair.getLocation() == null) {
                    continue;
                }

                if (Utils.samePosition(block, chair.getBlock())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent event) {
        if (!ready) {
            return;
        }

        for (Block block : event.getBlocks()) {
            for (Chair chair: chairs) {
                if (chair == null) continue;
                if (chair.getLocation() == null) continue;
                if (Utils.samePosition(block, chair.getBlock())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!ready) {
            return;
        }

        Player player = event.getEntity();
        Chair chair = chairMap.get(player.getUniqueId());
        ChairLeaveEvent leaveEvent = new ChairLeaveEvent(chair, player);
        Utils.callEvent(leaveEvent);
    }

    private void clearPlayer(Player player) {
        Chair chair = chairMap.get(player.getUniqueId());
        if (chair == null) {
            return;
        }

        chairMap.remove(player.getUniqueId());
        chairs.remove(chair);
        chair.clear();
    }

    private boolean sitPlayer(Chair chair, Player player) {
        if (player == null) {
            return false;
        }

        if (chair == null || chair.isOccupied()) {
            return false;
        }

        ArmorStand fakeSeat = chair.getArmorStand();

        if (fakeSeat == null) {
            fakeSeat = Utils.generateFakeSeat(chair);
        }

        fakeSeat.addPassenger(player);
        chair.setChair(fakeSeat);
        fakeSeats.add(fakeSeat.getUniqueId().toString());
        return true;
    }

    private void ejectPlayer(Block block, Player player, Entity entity) {
        Block exit = block;

        if (exitWhereFacing) {
            exit = findExitPoint(entity.getLocation(), block);
        }

        Location exitLoc = exit.getLocation().add(0.5,0.5,0.5);

        if (!faceAttacker) {
            exitLoc.setPitch(player.getLocation().getPitch());
            exitLoc.setYaw(player.getLocation().getYaw());
        } else {
            exitLoc.setDirection(Utils.getVectorDir(player.getLocation(), entity.getLocation()));
        }

    }

    private Block findExitPoint(Location entity, Block block) {
        Block blockToCheck = Utils.getBlockFromDirection(block, Utils.getCardinalDirection(entity));
        boolean foundValidExit = Utils.canFitPlayer(blockToCheck) && Utils.safePlace(blockToCheck);

        String[] directions = {"north", "east", "south", "west"};
        for (String direction: directions) {
            if (foundValidExit) {
                break;
            }

            blockToCheck = Utils.getBlockFromDirection(block, direction);
            foundValidExit = Utils.canFitPlayer(blockToCheck) && Utils.safePlace(blockToCheck);
        }

        if (!foundValidExit) {
            blockToCheck = block.getRelative(BlockFace.UP);
        }

        return blockToCheck;
    }

    public void shutdown() {
        clearChairs();
        clearFakeSeats();
    }

    private void clearChair(Chair chair) {
        if (chair == null) {
            return;
        }

        chair.clear();
    }

    private void clearChairs() {
        for (Chair chair: chairs) {
            clearChair(chair);
        }
    }

    public void clearFakeSeats() {
        for (Chair chair : chairs) {
            clearChair(chair);
        }
    }

}
