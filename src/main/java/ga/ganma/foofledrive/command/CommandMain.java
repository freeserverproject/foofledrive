package ga.ganma.foofledrive.command;

import ga.ganma.foofledrive.Filerelation;
import ga.ganma.foofledrive.Foofledrive;
import ga.ganma.foofledrive.economy.Economy;
import ga.ganma.foofledrive.plan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * CommandMainクラスは、/flまたは/foofledriveコマンドの実行を処理するクラスです。
 * このクラスは、プレイヤーがコマンドを入力した際に適切なアクションを実行します。
 */
public class CommandMain implements CommandExecutor {
    public static final HashMap<Player, Boolean> isopenInventory = new HashMap<>();
    private final Plugin plugin;

    public CommandMain(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * コマンドが実行されたときに呼び出されるメソッドです。
     *
     * @param sender  コマンドを実行したエンティティ
     * @param command 実行されたコマンド
     * @param label   コマンドのラベル
     * @param args    コマンドの引数
     * @return コマンドが正常に処理された場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("fl") || label.equalsIgnoreCase("foofledrive")) {
                handleCommand(player, args);
            }
        } else {
            plugin.getLogger().log(Level.INFO, "このコマンドはコンソールからではなくプレイヤーが入力するものです。");
        }
        return false;
    }

    /**
     * コマンドの引数に基づいて適切なアクションを実行します。
     *
     * @param player コマンドを実行したプレイヤー
     * @param args   コマンドの引数
     */
    private void handleCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "open":
                new Subopen(plugin, player);
                break;
            case "plan":
                handlePlanCommand(player, args);
                break;
            case "reload":
                handleReloadCommand(player);
                break;
            case "help":
                sendHelpMessage(player);
                break;
            default:
                sendHelpMessage(player);
                break;
        }
    }

    /**
     * /fl planコマンドの処理を行います。
     *
     * @param player コマンドを実行したプレイヤー
     * @param args   コマンドの引数
     */
    private void handlePlanCommand(Player player, String[] args) {
        if (args.length > 1) {
            plan selectedPlan = getPlanFromString(args[1]);
            if (selectedPlan != null) {
                new Subplan(plugin, player, selectedPlan);
                if (selectedPlan == plan.LIGHT) {
                    Filerelation.readFile(player).setFinish(Calendar.getInstance());
                }
            } else {
                sendPlanSelectionInventory(player);
            }
        } else {
            sendPlanSelectionInventory(player);
        }
    }

    /**
     * 文字列からplan列挙型を取得します。
     *
     * @param planString プランを表す文字列
     * @return 対応するplan列挙型、無効な場合はnull
     */
    private plan getPlanFromString(String planString) {
        try {
            return plan.valueOf(planString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * /fl reloadコマンドの処理を行います。
     *
     * @param player コマンドを実行したプレイヤー
     */
    private void handleReloadCommand(Player player) {
        if (player.isOp()) {
            plugin.reloadConfig();
            Foofledrive.configamout[0] = plugin.getConfig().getInt("amout.FREE");
            Foofledrive.configamout[1] = plugin.getConfig().getInt("amout.LIGHT");
            Foofledrive.configamout[2] = plugin.getConfig().getInt("amout.MIDDLE");
            Foofledrive.configamout[3] = plugin.getConfig().getInt("amout.LARGE");
            Foofledrive.unit = plugin.getConfig().getString("unit");
            player.sendMessage("[foofle drive]コンフィグをリロードしました。");
        } else {
            player.sendMessage("[foofle drive]このコマンドは管理者専用です。");
        }
    }

    /**
     * プレイヤーにヘルプメッセージを送信します。
     *
     * @param player メッセージを受け取るプレイヤー
     */
    private void sendHelpMessage(Player player) {
        player.sendMessage("[foofle drive] /fl open でfoofle driveを開くことができます。");
        player.sendMessage("[foofle drive] /fl plan で好きなプランに加入することができます。");
    }

    /**
     * プレイヤーにプラン選択インベントリを表示します。
     *
     * @param player インベントリを表示するプレイヤー
     */
    private void sendPlanSelectionInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "プラン選択画面");
        ItemStack glassPane = createItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ");
        ItemStack freePlan = createPlanItemStack(Material.PAPER, "FREEプラン", plan.FREE);
        ItemStack lightPlan = createPlanItemStack(Material.IRON_INGOT, "LIGHTプラン", plan.LIGHT);
        ItemStack middlePlan = createPlanItemStack(Material.GOLD_INGOT, "MIDDLEプラン", plan.MIDDLE);
        ItemStack largePlan = createPlanItemStack(Material.DIAMOND, "LARGEプラン", plan.LARGE);

        for (int i = 0; i <= 26; i++) {
            switch (i) {
                case 10:
                    inventory.setItem(i, freePlan);
                    break;
                case 12:
                    inventory.setItem(i, lightPlan);
                    break;
                case 14:
                    inventory.setItem(i, middlePlan);
                    break;
                case 16:
                    inventory.setItem(i, largePlan);
                    break;
                default:
                    inventory.setItem(i, glassPane);
                    break;
            }
        }

        isopenInventory.put(player, true);
        player.openInventory(inventory);
    }

    /**
     * 指定された素材と表示名でItemStackを作成します。
     *
     * @param material    アイテムの素材
     * @param displayName アイテムの表示名
     * @return 作成されたItemStack
     */
    private ItemStack createItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 指定されたプランの情報を含むItemStackを作成します。
     *
     * @param material    アイテムの素材
     * @param displayName アイテムの表示名
     * @param planType    プランの種類
     * @return 作成されたItemStack
     */
    private ItemStack createPlanItemStack(Material material, String displayName, plan planType) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(Economy.getPlanCost(planType) + Foofledrive.unit);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}