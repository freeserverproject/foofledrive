package ga.ganma.foofledrive.Listener;

import ga.ganma.foofledrive.FileRelationUtils;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.HashMap;

public class GetEvent implements Listener {
    public static final HashMap<Player, Boolean> isInventoryOpen = new HashMap<>();
    private final Plugin plugin;

    public GetEvent(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Playerdata playerData = FileRelationUtils.readFile(player);

        if (FileRelationUtils.nameCheck(player)) {
            handleExistingPlayer(player, playerData);
        } else {
            handleNewPlayer(player);
        }
    }

    private void handleExistingPlayer(Player player, Playerdata playerData) {
        if (playerData.getPlan() == plan.FREE) {
            player.sendMessage("[foofle drive]あなたは現在" + playerData.getPlan() + "プランに加入しています。");
        } else {
            handlePaidPlanPlayer(player, playerData);
        }
    }

    private void handlePaidPlanPlayer(Player player, Playerdata playerData) {
        if (playerData.getFinish() != null) {
            sendPaymentReminder(player, playerData);
        } else {
            playerData.setFinish(Calendar.getInstance());
            FileRelationUtils.createFile(playerData);
        }
    }

    private void sendPaymentReminder(Player player, Playerdata playerData) {
        new BukkitRunnable() {
            @Override
            public void run() {
                long diffTime = playerData.getFinish().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                int diffDays = (int) (diffTime / (1000 * 60 * 60 * 24));
                player.sendMessage("[foofle drive]あなたは現在" + playerData.getPlan() + "プランに加入しています。");
                player.sendMessage("[foofle drive]支払日まであと" + diffDays + "日です。");
            }
        }.runTaskLater(plugin, 60);
    }

    private void handleNewPlayer(Player player) {
        Playerdata newPlayerData = new Playerdata(player, Bukkit.getServer().createInventory(null, 9, "foofle Drive"), plan.FREE);
        FileRelationUtils.createFile(newPlayerData);
        player.sendMessage("[foofle drive]あなたは自動的に" + newPlayerData.getPlan() + "プランに加入しました。");
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if ("foofle drive".equals(event.getView().getTitle())) {
            Playerdata playerData = new Playerdata(player, event.getView().getTopInventory(), FileRelationUtils.readFile(player).getPlan());
            FileRelationUtils.createFile(playerData);
        }
    }
}