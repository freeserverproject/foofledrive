package ga.ganma.foofledrive.playerdata;


import ga.ganma.foofledrive.FoofleDrivePlan;
import ga.ganma.foofledrive.inventoryRelation.InventoryEncoder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Calendar;
import java.util.UUID;

public class PlayerDriveData {
    private final UUID mcid;
    private final FoofleDrivePlan plan;
    private final String inventorySt;
    private Calendar finish;

    public PlayerDriveData(Player pl, Inventory inv, FoofleDrivePlan plan) {
        this.mcid = pl.getUniqueId();
        this.plan = plan;
        this.inventorySt = InventoryEncoder.inventoryToString(inv);
    }

    public PlayerDriveData(OfflinePlayer pl, Inventory inv, FoofleDrivePlan plan) {
        this.mcid = pl.getUniqueId();
        this.plan = plan;
        this.inventorySt = InventoryEncoder.inventoryToString(inv);
    }

    public PlayerDriveData(UUID uuid, Inventory inv, FoofleDrivePlan plan) {
        this.mcid = uuid;
        this.plan = plan;
        this.inventorySt = InventoryEncoder.inventoryToString(inv);
    }

    public FoofleDrivePlan getPlan() {
        return plan;
    }

    public UUID getMcid() {
        return mcid;
    }

    public Inventory getInv() {
        return InventoryEncoder.stringToInventory(inventorySt);
    }

    public Calendar getFinish() {
        if (finish != null) {
            return finish;
        } else {
            Calendar cl = Calendar.getInstance();
            setFinish(cl);
        }
        return finish;
    }

    public void setFinish(Calendar cl) {
        cl.add(Calendar.DAY_OF_MONTH, +7);
        finish = cl;
    }
}
