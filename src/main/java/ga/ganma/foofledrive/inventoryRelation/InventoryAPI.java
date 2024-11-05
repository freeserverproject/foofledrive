package ga.ganma.foofledrive.inventoryRelation;

import ga.ganma.foofledrive.FileRelationUtils;
import ga.ganma.foofledrive.Foofledrive;
import ga.ganma.foofledrive.economy.Economy;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class InventoryAPI {
    public static void changePlan(OfflinePlayer player, plan plan) {
        Inventory inv = createInventoryForPlan(plan);
        ItemStack[] is = trimInventoryItems(FileRelationUtils.readFile(player).getInv().getStorageContents(), plan);

        inv.setStorageContents(is);
        Foofledrive.econ.withdrawPlayer(player, Economy.getPlanCost(FileRelationUtils.readFile(player).getPlan()));
        Playerdata pd = new Playerdata(player, inv, plan);
        pd.setFinish(Calendar.getInstance());
        FileRelationUtils.createFile(pd);
    }

    public static boolean changePlan(Player player, plan plan) {
        Inventory inv = createInventoryForPlan(plan);
        ItemStack[] is = trimInventoryItems(FileRelationUtils.readFile(player).getInv().getStorageContents(), plan);

        if (Foofledrive.econ.getBalance(player) >= Economy.getPlanCost(plan)) {
            inv.setStorageContents(is);
            Playerdata pd = new Playerdata(player, inv, plan);
            pd.setFinish(Calendar.getInstance());
            Foofledrive.econ.withdrawPlayer(player, Economy.getPlanCost(plan));
            player.sendMessage("[foofle drive]このプランの一週間の利用料金を払いました。");
            FileRelationUtils.createFile(pd);
            return true;
        } else {
            player.sendMessage("[foofle drive]お金が足りないため、" + plan + "プランの契約ができませんでした。");
        }
        return false;
    }

    public static Inventory createInventoryForPlan(plan plan) {
        switch (plan) {
            case FREE:
                return Bukkit.createInventory(null, 9, "foofle drive");
            case LIGHT:
                return Bukkit.createInventory(null, 18, "foofle drive");
            case MIDDLE:
                return Bukkit.createInventory(null, 27, "foofle drive");
            case LARGE:
                return Bukkit.createInventory(null, 54, "foofle drive");
            default:
                throw new IllegalArgumentException("Unknown plan: " + plan);
        }
    }

    public static ItemStack[] trimInventoryItems(ItemStack[] items, plan plan) {
        int maxSize = getMaxSizeForPlan(plan);
        if (items.length > maxSize) {
            List<ItemStack> itemList = new ArrayList<>(Arrays.asList(items));
            itemList.subList(maxSize, items.length).clear();
            return itemList.toArray(new ItemStack[0]);
        }
        return items;
    }

    public static int getMaxSizeForPlan(plan plan) {
        switch (plan) {
            case FREE:
                return 9;
            case LIGHT:
                return 18;
            case MIDDLE:
                return 27;
            case LARGE:
                return 54;
            default:
                throw new IllegalArgumentException("Unknown plan: " + plan);
        }
    }

    public Inventory inventorySizeChange(Inventory oldInv, int setsize) {
        Inventory inv = Bukkit.createInventory(null, setsize, "foofle drive");
        inv.setStorageContents(oldInv.getStorageContents());
        return inv;
    }
}