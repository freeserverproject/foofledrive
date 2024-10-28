package ga.ganma.foofledrive.economy;

import ga.ganma.foofledrive.Filerelation;
import ga.ganma.foofledrive.Foofledrive;
import ga.ganma.foofledrive.inventoryRelation.InventoryAPI;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.logging.Level;

public class Economy {

    public static void paymoney(Player p) {
        processPayment(p, Filerelation.readFile(p), Foofledrive.econ.getBalance(p));
    }

    public static void paymoney(OfflinePlayer p) {
        processPayment(p, Filerelation.readFile(p), Foofledrive.econ.getBalance(p));
    }

    private static void processPayment(OfflinePlayer p, Playerdata pd, double bal) {
        if (pd.getFinish() != null && pd.getFinish().before(Calendar.getInstance())) {
            int planCost = getPlanCost(pd.getPlan());
            if (bal >= planCost) {
                Foofledrive.econ.withdrawPlayer(p, planCost);
                logAndNotify(p, pd.getPlan() + "プランの料金を支払いました。", "[foofle drive]料金の支払いをしました。");
                pd.setFinish(Calendar.getInstance());
                Filerelation.createFile(pd);
            } else {
                InventoryAPI.planchange(p, plan.FREE);
                logAndNotify(p, "お金が足りないため、自動的にfreeプランへ移行しました。", "[foofle drive]お金が足りないため、自動的にfreeプランへ移行しました。");
                logAndNotify(p, "その際、2段目以降にあるアイテムを全消去しました。", "[foofle drive]その際、2段目以降にあるアイテムを全消去しました。");
            }
        }
    }

    private static void logAndNotify(OfflinePlayer p, String logMessage, String notifyMessage) {
        Bukkit.getLogger().log(Level.INFO, "[foofle drive]" + p.getName() + logMessage);
        if (p.isOnline()) {
            ((Player) p).sendMessage(notifyMessage);
        }
    }

    public static int getPlanCost(plan plan) {
        switch (plan) {
            case FREE:
                return Foofledrive.configamout[0];
            case LIGHT:
                return Foofledrive.configamout[1];
            case MIDDLE:
                return Foofledrive.configamout[2];
            case LARGE:
                return Foofledrive.configamout[3];
            default:
                return 0;
        }
    }
}