package midorichan;

import midorichan.commands.*;
import midorichan.listeners.CommandLog;
import midorichan.listeners.MobListener;
import midorichan.listeners.PlayerListener;
import midorichan.chairs.ChairManager;
import midorichan.listeners.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin{

    public static Main instance;
    private double __version__ = 1.3;
    public PlayerListener playerlistener;
    public static String ver;
    public static String prefix = " §2>§a>§r ";
    public ChairManager chairManager;
    public MessageManager messageManager;
    public CommandLog commandLog;
    public MobListener mobManager;
    public static FileConfiguration config = null;

    public static Main getInstance() {
        return instance;
    }

    private void setInstance(Main plugin) {
        instance = plugin;
    }

    private void setVersion(Main plugin) {
        ver = Bukkit.getServer().getClass().getPackage().getName();
        ver = ver.substring(ver.lastIndexOf(".") + 1);
    }

    public static String getVersion() {
        return ver;
    }

    public static String getPrefix() {
        return prefix;
    }

    public void log(String info) {
        getLogger().info(info);
    }

    @Override
    public void onEnable() {
        //register Configs
        setInstance(this);
        setVersion(this);
        saveDefaultConfig();
        config = getConfig();

        //Events init
        playerlistener = new PlayerListener();
        playerlistener.__init__();
        chairManager = new ChairManager();
        chairManager.__init__();
        chairManager.clearFakeSeats();
        messageManager = new MessageManager();
        commandLog = new CommandLog();
        commandLog.__init__();
        mobManager = new MobListener();
        mobManager.__init__();

        //register listener
        getServer().getPluginManager().registerEvents(playerlistener, this);
        getServer().getPluginManager().registerEvents(chairManager, this);
        getServer().getPluginManager().registerEvents(messageManager, this);
        getServer().getPluginManager().registerEvents(commandLog, this);
        getServer().getPluginManager().registerEvents(mobManager, this);

        //register Command
        Bukkit.getPluginCommand("whois").setExecutor(new whois());
        Bukkit.getPluginCommand("hat").setExecutor(new hat());
        Bukkit.getPluginCommand("fly").setExecutor(new fly());
        Bukkit.getPluginCommand("reloadconfig").setExecutor(new reloadconfig());
        Bukkit.getPluginCommand("getpos").setExecutor(new getpos());

        log(" §2>§a>§r Enabled MidoriChairs v" + __version__);
    }

    @Override
    public void onDisable() {
        this.chairManager.shutdown();

        log(" §2>§a>§r Disabled MidoriChairs v1.0" + __version__);
    }

}
