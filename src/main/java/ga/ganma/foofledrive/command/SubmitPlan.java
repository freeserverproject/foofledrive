package ga.ganma.foofledrive.command;

import ga.ganma.foofledrive.FileRelationUtils;
import ga.ganma.foofledrive.inventoryRelation.InventoryAPI;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;

public class SubmitPlan {
    Plugin pl;
    Player p;

    public SubmitPlan(Plugin pl, Player player, plan plan) {
        this.pl = pl;
        p = player;
        boolean isSuccess;
        isSuccess = InventoryAPI.changePlan(player, plan);
        Playerdata pd = FileRelationUtils.readFile(p);
        if (pd.getFinish() == null) {
            pd.setFinish(Calendar.getInstance());
            FileRelationUtils.createFile(pd);
        }

        if (isSuccess) {
            p.sendMessage("[foofle drive]プランを" + plan + "プランに変更しました。");
        }
    }
}
