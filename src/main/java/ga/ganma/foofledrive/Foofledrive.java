package ga.ganma.foofledrive;

import ga.ganma.foofledrive.Listener.GUIEvent;
import ga.ganma.foofledrive.Listener.GetEvent;
import ga.ganma.foofledrive.bukkitRunnable.Runnable;
import ga.ganma.foofledrive.command.CommandMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Foofledrive extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static int[] configValues = new int[4];
    public static String unit;
    public static Plugin ender;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        ender = this;
        new GetEvent(this);
        new GUIEvent(this);
        CommandMain commandMain = new CommandMain(this);
        this.getCommand("fl").setExecutor(commandMain);
        this.getCommand("foofledrive").setExecutor(commandMain);
        saveDefaultConfig();
        loadConfigValues();
        if (!setupEconomy()) {
            Bukkit.getPluginManager().disablePlugin(this);
            log.warning("[foofle drive]Vaultが存在しません！");
            return;
        }
        new Runnable(this).runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        return (econ != null);
    }

    private void loadConfigValues() {
        configValues[0] = getConfig().getInt("amout.FREE");
        configValues[1] = getConfig().getInt("amout.LIGHT");
        configValues[2] = getConfig().getInt("amout.MIDDLE");
        configValues[3] = getConfig().getInt("amout.LARGE");
        unit = getConfig().getString("unit");
    }
}