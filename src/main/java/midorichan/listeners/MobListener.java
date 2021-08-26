package midorichan.listeners;

import midorichan.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobListener implements Listener {

    private static Main plugin = Main.getInstance();
    private static boolean ready = false;

    public static void __init__() {
        ready = true;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void EntitySpawnEvent(EntitySpawnEvent e) {
        if (!(plugin.getConfig().getBoolean("kill-phantom"))) {
            return;
        }

        Entity entity = e.getEntity();

        if (entity.getType() == EntityType.PHANTOM) {
            entity.remove();
        }
    }

}
