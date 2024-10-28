/**
 * このクラスはBukkitのRunnableを拡張し、定期的にプレイヤーデータを処理するタスクを実行します。
 */
package ga.ganma.foofledrive.bukkitRunnable;

import ga.ganma.foofledrive.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.UUID;

/**
 * BukkitのRunnableを拡張したクラス。
 * プラグインのインスタンスとプレイヤーデータフォルダを保持し、定期的にプレイヤーデータを処理します。
 */
public class Runnable extends BukkitRunnable {
    private final Plugin plugin;
    private final File playerDataFolder;

    /**
     * コンストラクタ。
     *
     * @param plugin プラグインのインスタンス
     */
    public Runnable(Plugin plugin) {
        this.plugin = plugin;
        this.playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
    }

    /**
     * 定期的に実行されるタスク。
     * プレイヤーデータフォルダが存在する場合、その中のファイルを処理します。
     */
    @Override
    public void run() {
        if (playerDataFolder.exists()) {
            File[] playerDataFiles = playerDataFolder.listFiles();
            if (playerDataFiles != null) {
                for (File playerDataFile : playerDataFiles) {
                    processPlayerDataFile(playerDataFile);
                }
            }
        }
    }

    /**
     * プレイヤーデータファイルを処理します。
     * ファイル名からUUIDを取得し、対応するオフラインプレイヤーに対して支払い処理を行います。
     *
     * @param playerDataFile プレイヤーデータファイル
     */
    private void processPlayerDataFile(File playerDataFile) {
        String fileName = playerDataFile.getName();
        String uuidString = fileName.substring(0, fileName.lastIndexOf('.'));
        UUID playerUUID = UUID.fromString(uuidString);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        Economy.paymoney(offlinePlayer);
    }
}