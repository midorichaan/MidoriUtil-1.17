package midorichan.events;

import midorichan.chairs.Chair;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChairLeaveEvent extends Event {

    private Chair c;
    private Player p;
    private boolean facing = false;

    public ChairLeaveEvent(Chair c, Player p, boolean flag) {
        this.c = c;
        this.p = p;
        this.facing = flag;
    }

    public ChairLeaveEvent(Chair c, Player p) {
        this.c = c;
        this.p = p;
    }

    public Chair getChair() {
        return this.c;
    }

    public Player getPlayer() {
        return this.p;
    }

    public boolean getFacing() {
        return this.facing;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    static public HandlerList getHandlerList() {
        return handlers;
    }

}
