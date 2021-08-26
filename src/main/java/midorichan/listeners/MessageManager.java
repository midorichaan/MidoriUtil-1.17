package midorichan.listeners;

import midorichan.Main;
import midorichan.enums.MessageConstruct;
import midorichan.events.MessageEvent;
import midorichan.enums.MessageType;
import midorichan.utils.Messages;
import midorichan.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageManager implements Listener {

    private Main plugin = Main.getInstance();

    public List<UUID> muted = new ArrayList<UUID>();
    private List<UUID> tempMute = new ArrayList<UUID>();

    private void tempMute(Player player) {
        int duration = 0;
        tempMute.add(player.getUniqueId());
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (tempMuted(player)) tempMute.remove(player.getUniqueId());
            }
        };
        runnable.runTaskLater(plugin, duration);
    }

    private boolean tempMuted(Player player) {
        return tempMute.contains(player.getUniqueId());
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        Player player = event.getPlayer();
        if (tempMute.contains(player.getUniqueId())) event.setCancelled(true);
        if (event.isCancelled()) return;
        if (0 > 0) tempMute(player);
        if (false && !muted.contains(player.getUniqueId())) {
            String string = processString(event);
            if (!string.isEmpty()) player.sendMessage(string);
        }
    }

    private String processString(MessageEvent event) {
        MessageType type = event.getType();
        MessageConstruct construct = event.getConstruct();
        String string;

        String stringOutput = Messages.getMessage(type);
        if (stringOutput == null) {
            string = Messages.getDefault(type);
        } else {
            string = stringOutput;
        }

        String finalString = "";

        if (construct == MessageConstruct.SINGLE) finalString = Utils.replaceMessage(event.getPlayer(), string);
        else if (construct == MessageConstruct.DEFENSIVE) finalString = Utils.replaceMessage(event.getEntity(), event.getPlayer(), string);
        else if (construct == MessageConstruct.OFFENSIVE && event.getEntity() instanceof Player) finalString = Utils.replaceMessage(event.getPlayer(),(Player) event.getEntity(), string);
        return finalString;
    }

}