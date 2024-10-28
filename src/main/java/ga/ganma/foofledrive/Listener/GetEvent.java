package ga.ganma.foofledrive.Listener;

import ga.ganma.foofledrive.Filerelation;
import ga.ganma.foofledrive.Foofledrive;
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
    public static HashMap<Player, Boolean> isInventoryOpen = new HashMap<>();
    private final Plugin pl;

    public GetEvent(Plugin pl) {
        Bukkit.getPluginManager().registerEvents(this, pl);
        this.pl = pl;
    }

    @EventHandler
    public void getplayerloginEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Playerdata pd = Filerelation.readFile(p);

        if (Filerelation.nameCheck(p)) {
            if (pd.getPlan() == plan.FREE) {
                p.sendMessage("[foofle drive]あなたは現在" + pd.getPlan() + "プランに加入しています。");
            } else {
                double bal = Foofledrive.econ.getBalance(p);
                if (pd.getFinish() != null) {
                    Playerdata finalPd = pd;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            long diffTime = finalPd.getFinish().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                            int diffDayMillis = 1000 * 60 * 60 * 24;
                            int diffDays = (int) (diffTime / diffDayMillis);
                            p.sendMessage("[foofle drive]あなたは現在" + finalPd.getPlan() + "プランに加入しています。");
                            p.sendMessage("[foofle drive]支払日まであと" + diffDays + "日です。");
                        }
                    }.runTaskLater(pl, 60);
                } else {
                    pd.setFinish(Calendar.getInstance());
                    Filerelation.createFile(pd);
                }
            }
        } else {
            pd = new Playerdata(p, Bukkit.getServer().createInventory(null, 9, "foofle Drive"), plan.FREE);
            Filerelation.createFile(pd);
            p.sendMessage("[foofle drive]あなたは自動的に" + pd.getPlan() + "プランに加入しました。");
        }
    }

    @EventHandler
    public void getPlayerInventoryCloseEvent(InventoryCloseEvent e) {
        Player pl = (Player) e.getPlayer();
        if (e.getView().getTitle().equals("foofle drive")) {
            Playerdata pd = new Playerdata(pl, e.getView().getTopInventory(), Filerelation.readFile(pl).getPlan());
            Filerelation.createFile(pd);
        }
    }
}
