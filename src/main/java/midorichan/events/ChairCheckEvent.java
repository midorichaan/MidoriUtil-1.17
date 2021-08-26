package midorichan.events;

import midorichan.chairs.Chair;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChairCheckEvent extends Event {

    Chair c;
    Block b;
    Player p;

    public ChairCheckEvent(Chair c, Player p) {
        this.c = c;
        this.p = p;
    }

    public ChairCheckEvent(Block b, Player p) {
        this.b = b;
        this.p = p;
    }

    public Chair getChair() {
        return this.c;
    }

    public Block getBlock() {
        return this.b;
    }

    public Player getPlayer() {
        return this.p;
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
