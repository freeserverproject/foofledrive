package ga.ganma.foofledrive.Listener;

import ga.ganma.foofledrive.Filerelation;
import ga.ganma.foofledrive.command.CommandMain;
import ga.ganma.foofledrive.inventoryRelation.InventoryAPI;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;
import java.util.HashMap;

public class GUIEvent implements Listener {

    private static final HashMap<Player, ga.ganma.foofledrive.plan> provisionalPlan = new HashMap<>();

    public GUIEvent(Plugin pl) {
        Bukkit.getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() == null) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        if (!CommandMain.isopenInventory.containsKey(player) || !CommandMain.isopenInventory.get(player)) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedItem == null || clickedInventory == null || !clickedItem.hasItemMeta()) {
            return;
        }

        if (e.getView().getType() != InventoryType.CREATIVE && e.getView().getTitle().contains("プラン選択画面")) {
            handlePlanSelection(e, player);
        } else if (e.getView().getTitle().contains("契約してよろしいですか？")) {
            handleContractConfirmation(e, player);
        }
    }

    private void handlePlanSelection(InventoryClickEvent e, Player player) {
        switch (e.getSlot()) {
            case 10:
                openConfirmationInventory(e, player, plan.FREE);
                break;
            case 12:
                openConfirmationInventory(e, player, plan.LIGHT);
                break;
            case 14:
                openConfirmationInventory(e, player, plan.MIDDLE);
                break;
            case 16:
                openConfirmationInventory(e, player, plan.LARGE);
                break;
            default:
                if (e.getCurrentItem().getType() == Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                }
                break;
        }
    }

    private void openConfirmationInventory(InventoryClickEvent e, Player player, plan selectedPlan) {
        e.setCancelled(true);
        player.openInventory(createConfirmationInventory());
        provisionalPlan.put(player, selectedPlan);
    }

    private void handleContractConfirmation(InventoryClickEvent e, Player player) {
        switch (e.getSlot()) {
            case 15:
                confirmPlanChange(e, player);
                break;
            case 11:
                e.setCancelled(true);
                player.closeInventory();
                break;
            default:
                if (e.getCurrentItem().getType() == Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                }
                break;
        }
    }

    private void confirmPlanChange(InventoryClickEvent e, Player player) {
        e.setCancelled(true);
        plan selectedPlan = provisionalPlan.get(player);
        boolean planChanged = InventoryAPI.planchange(player, selectedPlan);
        Playerdata playerData = Filerelation.readFile(player);

        if (playerData.getFinish() == null) {
            playerData.setFinish(Calendar.getInstance());
            Filerelation.createFile(playerData);
        }

        if (planChanged) {
            player.sendMessage("[foofle drive]プランを" + selectedPlan + "プランに変更しました。");
        }

        player.closeInventory();
    }

    private Inventory createConfirmationInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "契約してよろしいですか？");
        ItemStack grayPane = createItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ");
        ItemStack greenPane = createItemStack(Material.GREEN_STAINED_GLASS_PANE, "はい");
        ItemStack redPane = createItemStack(Material.RED_STAINED_GLASS_PANE, "いいえ");

        for (int i = 0; i <= 26; i++) {
            if (i == 11) {
                inv.setItem(i, redPane);
            } else if (i == 15) {
                inv.setItem(i, greenPane);
            } else {
                inv.setItem(i, grayPane);
            }
        }
        return inv;
    }

    private ItemStack createItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
